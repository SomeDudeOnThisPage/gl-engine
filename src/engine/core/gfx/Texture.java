package engine.core.gfx;

import engine.util.Resource;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
  private static int current = 0;

  private int id;

  public void bind()
  {
    this.bind(0);
  }

  public void bind(int slot)
  {
    if (Texture.current != this.id)
    {
      glActiveTexture(GL_TEXTURE0 + slot);
      glBindTexture(GL_TEXTURE_2D, this.id);
      Texture.current = this.id; // todo: current for slot
    }
  }

  public void unbind()
  {
    this.unbind(0);
  }

  public void unbind(int slot)
  {
    glActiveTexture(GL_TEXTURE0 + slot);
    glBindTexture(GL_TEXTURE_2D, 0);
    Texture.current = 0;
  }

  public Texture(String name)
  {
    this(name, GL_NEAREST);
  }

  public Texture(String name, int filter)
  {
    try
    {
      IntBuffer width = BufferUtils.createIntBuffer(1);
      IntBuffer height = BufferUtils.createIntBuffer(1);
      IntBuffer components = BufferUtils.createIntBuffer(1);

      // STB is a blessing
      ByteBuffer data = STBImage.stbi_load_from_memory(Resource.ioLoadResource("resources/textures/" + name + ".png"), width, height, components, 4);

      this.id = glGenTextures();
      glBindTexture(GL_TEXTURE_2D, this.id);

      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

      // Cleanup
      glBindTexture(GL_TEXTURE_2D, 0);
      if (data != null)
        stbi_image_free(data);
    }
    catch (IOException e)
    {
      System.err.println("could not load texture " + name + ": " + e.getMessage());
    }
  }
}