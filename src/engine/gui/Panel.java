package engine.gui;

import org.joml.Vector2f;

import java.util.ArrayList;

public class Panel extends GUIObject
{
  protected Vector2f size;
  private ArrayList<GUIObject> children;

  public void addChild(GUIObject child)
  {
    children.add(child);
  }

  public void setPosition(int x, int y)
  {
    position.x = x;
    position.y = y;

    // Update child positions
    for (GUIObject child : children)
    {
      int childX = (int) child.position.x;
      int childY = (int) child.position.y;

      child.setPosition(x + childX, y + childY);
    }
  }

  @Override
  public void render()
  {
    
  }

  public Panel(int x, int y, int sizeX, int sizeY)
  {
    super(x, y);
    size = new Vector2f(sizeX, sizeY);
    children = new ArrayList<>();
  }
}
