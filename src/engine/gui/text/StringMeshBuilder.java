package engine.gui.text;

import engine.core.gfx.InstancedRenderingVertexArray;
import org.joml.Vector2f;

/**
 * Static utility class for creating text messages.
 * <br> For now only works with bitmap fonts, TTF support is planned.
 */
public class StringMeshBuilder
{
  private static final float TEXT_SPACING = 0.01f;

  /**
   * Generates a mesh from a given string, scale and font and sets the given VAOs' information accordingly.
   *
   * @param text The text to be generated. Can include '\n' and '\t'.
   * @param font The bitmap font to be used.
   * @param vao The VAO of the calling object that the text mesh is written to.
   */
  public static void generate(String text, BitmapFont font, InstancedRenderingVertexArray vao)
  {
    char current;
    Vector2f charTexture;

    float[] vertexPositions = new float[text.length() * 3];
    int cp = 0;

    float[] textureCoordinates = new float[text.length() * 2];
    int ct = 0;

    int pos = 0;

    for (int i = 0; i < text.length(); i++)
    {
      current = text.charAt(i);

      vertexPositions[cp] = pos;
      vertexPositions[cp + 1] = 0;
      vertexPositions[cp + 2] = 0;

      charTexture = font.getCharPosition(current);
      textureCoordinates[ct] = charTexture.x;
      textureCoordinates[ct + 1] = charTexture.y;

      cp += 3;
      ct += 2;

      if (i < text.length() - 1)
      {
        pos += font.getCharOffset(current) / 2 + TEXT_SPACING * font.getScale();
      }
    }

    vao.setAmount(cp / 3);
    vao.updateInstancedAttributeData(2, vertexPositions);
    vao.updateInstancedAttributeData(3, textureCoordinates);
  }
}
