package engine.core.gfx;

import org.joml.Vector2f;

public class TextureAtlas extends Texture
{
  private int rows;
  private int columns;

  public int getRows()
  {
    return rows;
  }

  /**
   * Returns texture coordinates of a sprite on a texture atlas.
   * @param texture sprite
   * @return texture coordinates
   */
  public Vector2f getTextureCoordinates(int texture)
  {
    return new Vector2f(texture % rows / (float) rows, (float) Math.floor(texture / (float) columns) / columns);
  }

  public TextureAtlas(String name, int rows, int columns)
  {
    this(name, "textures", rows, columns);
  }

  public TextureAtlas(String name, String superfolder, int rows, int columns)
  {
    super(name, superfolder);

    this.rows = rows;
    this.columns = columns;
  }
}
