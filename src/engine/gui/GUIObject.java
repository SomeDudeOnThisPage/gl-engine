package engine.gui;

import org.joml.Vector2f;

public abstract class GUIObject
{
  protected Vector2f position;

  public abstract void render();

  public void setPosition(int x, int y)
  {
    position.x = x;
    position.y = y;
  }

  public GUIObject(int x, int y)
  {
    position = new Vector2f(x, y);
  }
}
