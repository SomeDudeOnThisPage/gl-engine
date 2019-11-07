package engine.terrain;

import engine.core.scene.prefabs3D.Camera3D;
import engine.core.entity.Entity;
import engine.core.entity.component.FPSCameraMovementComponent;
import engine.core.gfx.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11C.*;

public class Terrain extends Entity
{
  public static final int DIRECTION_NORTH = 0;
  public static final int DIRECTION_EAST = 1;
  public static final int DIRECTION_SOUTH = 2;
  public static final int DIRECTION_WEST = 3;

  private Vector2i size;
  private Heightfield heightfield;
  private TerrainChunk[] chunks;
  private Shader shader;
  private LODAlgorithm lod;

  public Vector2i getSize()
  {
    return this.size;
  }

  @Override
  public void render(Camera3D camera)
  {
    // set LOD of chunks depending on distance to the camera
    for (int i = 0; i < this.size.x * this.size.y; i++)
    {
      TerrainChunk chunk = this.chunks[i];

      int lod = this.lod.getLOD(((FPSCameraMovementComponent) camera.getComponent("fps_camera_movement")).get(), chunk);
      if (lod != chunk.getLOD())
      {
        int[] neighbors = new int[4];
        for (int n = 0; n < 4; n++)
        {
          TerrainChunk neighbor = this.getNeighbor(chunk.getPosition(), n);
          if (neighbor != null)
          {
            neighbors[n] = neighbor.getLOD();
          }
          else
          {
            neighbors[n] = 0;
          }
        }
        //chunk.setLOD(lod, new int[4]);
      }
    }

    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

    camera.bind(this.shader);
    this.shader.setUniform("model", new Matrix4f().identity());

    // render each chunk
    for (int i = 0; i < this.size.x * this.size.y; i++)
    {
      this.shader.setUniform("col", this.chunks[i].getColor());
      this.shader.bind();
      this.chunks[i].getVAO().render();
    }
  }

  public TerrainChunk getNeighbor(Vector2i position, int direction)
  {
    switch(direction)
    {
      case DIRECTION_NORTH:
        if (position.x >= 1) return this.chunks[(position.x - 1) * this.size.x + position.y];
      case DIRECTION_EAST:
        if (position.y < this.size.y - 1) return this.chunks[position.x * this.size.x + position.y + 1];
      case DIRECTION_SOUTH:
        if (position.x < this.size.x - 1) return this.chunks[(position.x + 1) * this.size.x + position.y];
      case DIRECTION_WEST:
        if (position.y >= 1) return this.chunks[position.x * this.size.x + position.y - 1];
      default:
        return null;
    }
  }

  public Terrain(String name, int x, int y)
  {
    this.size = new Vector2i(x, y);
    this.heightfield = new Heightfield(name, this.size.x * TerrainChunk.CHUNK_SIZE + 1, this.size.y * TerrainChunk.CHUNK_SIZE + 1);
    this.shader = new Shader("terrain");
    this.lod = new ExpLOD(1, 8);

    // populate chunk array
    this.chunks = new TerrainChunk[this.size.x * this.size.y];
    for (int i = 0; i < this.size.x; i++)
    {
      for (int j = 0; j < this.size.y; j++)
      {
        this.chunks[i * this.size.x + j] = new TerrainChunk(i, j, this.heightfield, this);
      }
    }
  }
}