package engine.terrain;

import engine.core.Camera;
import engine.core.GameObject;
import engine.core.gfx.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11C.*;

public class Terrain extends GameObject
{
  private TerrainChunk[] chunks;
  private int size;
  private Shader shader;
  private BufferedImage heightmap;

  private BufferedImage splitHeightmap(int x, int y)
  {
    return this.heightmap.getSubimage(x * 16, y * 16, 17, 17);
  }

  @Override
  public void render(Camera camera)
  {
    glEnable(GL_CULL_FACE);
    glCullFace(GL_FRONT);

    for (TerrainChunk chunk:chunks)
    {
      this.shader.setUniform("projection", camera.getProjection());
      this.shader.setUniform("view", camera.getView());

      Vector2f position = chunk.getPosition();
      this.shader.setUniform("model", new Matrix4f().identity().translate(position.x, 0.0f, position.y));

      this.shader.bind();
      chunk.getVAO().render();
    }
    this.shader.unbind();

    glDisable(GL_CULL_FACE);
    glCullFace(GL_BACK);
  }

  @Override
  public void update()
  {
    // maybe some sort of real-time vertex terrain editing idk
  }

  public Terrain(String map)
  {
    this.shader = new Shader("terrain");
    this.size = 4;
    this.chunks = new TerrainChunk[this.size * this.size];

    try
    {
      this.heightmap = ImageIO.read(new File("resources/heightmaps/" + map + ".png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    for (int i = 0; i < this.size; i++)
    {
      for (int j = 0; j < this.size; j++)
      {
        this.chunks[j * this.size + i] = new TerrainChunk(this.splitHeightmap(j, i), j, i);
        this.chunks[j * this.size + i].generate();
      }
    }
  }
}
