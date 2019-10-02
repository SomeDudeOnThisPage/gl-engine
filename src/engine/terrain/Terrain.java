package engine.terrain;

import engine.core.Camera;
import engine.core.scene.GameObject;
import engine.core.gfx.Shader;
import engine.core.gfx.VertexArray;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Terrain extends GameObject
{
  private VertexArray vao;
  private Shader shader;
  private BufferedImage heightmap;

  private int size;
  private int baseLOD;

  private ArrayList<TerrainChunk> chunks;
  private ArrayList<Node> traversed;
  private TerrainChunk root;

  @Override
  public void render(Camera camera)
  {
    this.shader.setCamera(camera);
    for (Node chunk:this.traversed)
    {
      TerrainChunk current = (TerrainChunk) chunk;
      if (!chunk.subdivided())
      {
        int[] indices = current.getIndices();
        if (indices != null)
        {
          this.shader.setUniform("model", new Matrix4f().identity());
          this.shader.setUniform("col", current.color);

          this.vao.setIndices(current.getIndices());

          this.shader.bind();
          this.vao.render();
        }
      }
    }

    this.shader.unbind();
  }

  @Override
  public void update()
  {
    this.traversed.clear();
    this.root.traverse(this.traversed);
  }

  public void updateQuadTree(Camera camera)
  {
    float RADIUS = 50.0f;

    for (Node chunk:this.traversed)
    {
      TerrainChunk current = (TerrainChunk) chunk;

      // get distance to camera
      Vector2f position_quad_2d = current.getCenter();
      Vector3f position_quad = new Vector3f(-position_quad_2d.x, camera.getPosition().y, -position_quad_2d.y);

      // get distance of parent to camera
      float distance_parent = 0.0f;
      TerrainChunk parent = (TerrainChunk) current.getParent();
      if (parent != null)
      {
        Vector2f position_quad_parent_2d = parent.getCenter();
        Vector3f position_quad_parent = new Vector3f(-position_quad_parent_2d.x, camera.getPosition().y, -position_quad_parent_2d.y);
        distance_parent = position_quad_parent.distance(camera.getPosition());
      }

      float distance = position_quad.distance(camera.getPosition());

      for (int i = 1; i < this.baseLOD; i+=i)
      {
        if (distance <= RADIUS * i && current.getLOD() >= i)
        {
          current.subdivide();
        }
        else if (parent != null && distance_parent > RADIUS * i && parent.getLOD() <= i)
        {
          parent.join();
        }
      }
    }
  }

  public Terrain(String map)
  {
    int size = 256 + 1;
    int lod = 16;

    try
    {
      this.heightmap = ImageIO.read(new File("resources/heightmaps/" + map + ".png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    // add test chunks
    this.root = new TerrainChunk(null, size, 0, 0, size - 1, lod);
    this.root.subdivide();
    this.traversed = new ArrayList<>();

    this.shader = new Shader("terrain");
    this.vao = TerrainGenerator.generate(this.heightmap, size);
    this.size = size;
    this.baseLOD = lod;
  }

}
