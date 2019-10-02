package engine.gui.text;

import engine.core.gfx.InstancedRenderingVertexArray;
import engine.core.gfx.TextureAtlas;
import engine.util.Resource;
import org.joml.Vector2f;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TrueTypeFont
{
  private static float BMP_FONT_SCALE = 42;

  private TextureAtlas texture;
  private InstancedRenderingVertexArray vao;

  private float[] offsets = new float[256];
  private float scale;

  private float fontBias = 10;

  private float[] positions;
  private float[] texcoords;
  public void generateMesh(String text, float x, float y, InstancedRenderingVertexArray vao)
  {
    int current;
    Vector2f texcoord;

    positions = new float[text.length() * 3];
    int cp = 0;

    texcoords = new float[text.length() * 2];
    int ct = 0;

    int pos = 0;

    for (int i = 0; i < text.length(); i++)
    {
      current = text.charAt(i);

      positions[cp] = x + pos;
      positions[cp + 1] = y;
      positions[cp + 2] = 0;

      texcoord = texture.getTextureCoordinates(current);
      texcoords[ct] = texcoord.x;
      texcoords[ct + 1] = texcoord.y;

      cp += 3;
      ct += 2;

      if (i < text.length() - 1)
      {
        pos += offsets[current] * scale / 2 / BMP_FONT_SCALE;
      }
    }

    vao.setAmount(cp / 3);
    vao.updateInstancedAttributeData(2, positions);
    vao.updateInstancedAttributeData(3, texcoords);
  }

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

  public TrueTypeFont(String name, int scale)
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
