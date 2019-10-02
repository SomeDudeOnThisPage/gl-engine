package engine.core.gfx;

import engine.util.Resource;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.*;
import static org.lwjgl.opengl.GL14C.GL_DEPTH_COMPONENT32;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
  private static int current = 0;

  private int id;
  private int width;
  private int height;

  public BufferedImage toImage()
  {
    int channels = 4;

    ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * channels);
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

    for (int x = 0; x < width; ++x)
    {
      for (int y = 0; y < height; ++y)
      {
        int i = (x + y * width) * channels;

        int r = buffer.get(i) & 0xFF;
        int g = buffer.get(i + 1) & 0xFF;
        int b = buffer.get(i + 2) & 0xFF;
        int a = 255;
        if (channels == 4)
          a = buffer.get(i + 3) & 0xFF;

        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
      }
    }

    return image;
  }

  public int getID() { return this.id; }

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
      Texture.current = this.id;
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

  public Texture(int width, int height, int type)
  {
    int mode = GL_UNSIGNED_BYTE;

    this.id = glGenTextures();
    this.width = width;
    this.height = height;

    glBindTexture(GL_TEXTURE_2D, this.id);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    if (type == GL_DEPTH_COMPONENT)
    {
      mode = GL_FLOAT;
    }

    glTexImage2D(GL_TEXTURE_2D, 0, type, width, height, 0, type, mode, (ByteBuffer) null);
  }

  public Texture(String name)
  {
    this(name, "textures");
  }

  public Texture(String name, String superfolder)
  {
    IntBuffer width = BufferUtils.createIntBuffer(1);
    IntBuffer height = BufferUtils.createIntBuffer(1);
    IntBuffer components = BufferUtils.createIntBuffer(1);

    ByteBuffer data = null;
    try
    {
      // STB is a blessing
      data = STBImage.stbi_load_from_memory(Resource.ioLoadResource("resources/" + superfolder + "/" + name + ".png"), width, height, components, 4);
    }
    catch (IOException e)
    {
      System.err.println("could not load texture " + name + ": " + e.getMessage());
    }

    this.id = glGenTextures();
    this.width = width.get(0);
    this.height = height.get(0);

    glBindTexture(GL_TEXTURE_2D, this.id);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

    // Cleanup
    glBindTexture(GL_TEXTURE_2D, 0);
    if (data != null)
      stbi_image_free(data);
  }
}