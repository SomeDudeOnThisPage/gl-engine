package engine.gui.text;

import engine.core.gfx.TextureAtlas;
import engine.util.Resource;
import org.joml.Vector2f;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapFont
{
  public static float BMP_FONT_SCALE = 42;

  private TextureAtlas texture;

  private float[] offsets = new float[256];
  private float scale;

  public float getScale()
  {
    return scale;
  }

  public void bind()
  {
    texture.bind();
  }

  public void unbind()
  {
    texture.unbind();
  }

  public Vector2f getCharPosition(char character)
  {
    return texture.getTextureCoordinates(character);
  }

  public float getCharOffset(char character)
  {
    return offsets[character];
  }

  public BitmapFont(String name, int scale)
  {
    this.scale = scale * 2;

    // Load font texture
    texture = new TextureAtlas(name, "fonts", 16, 16);

    // Load font metrics
    ByteBuffer buffer;
    try
    {
      buffer = Resource.ioLoadResource("resources/fonts/" + name + ".dat");
      for (int i = 0; i < 256; i++)
      {
        offsets[i] = buffer.get(i);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
