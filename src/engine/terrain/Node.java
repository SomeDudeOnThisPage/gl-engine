package engine.terrain;

import java.util.ArrayList;
import java.util.List;

public abstract class Node
{
  protected Node parent;
  private List<Node> children;

  public void traverse(ArrayList<Node> list)
  {
    if (this.children.size() > 0)
    {
      for (Node child:this.children)
      {
        child.traverse(list);
      }
    }
    else
    {
      list.add(this);
    }
  }

  public void update()
  {
    for (Node child:this.children)
    {
      child.update();
    }
  }

  public void render()
  {
    for (Node child:children)
    {
      child.render();
    }
  }

  public boolean subdivided()
  {
    return this.children.size() != 0;
  }

  public void add(Node child)
  {
    child.setParent(this);
    this.children.add(child);
  }

  public Node()
  {
    this.children = new ArrayList<>();
  }

  public Node getParent()
  {
    return this.parent;
  }

  public void setParent(Node parent)
  {
    this.parent = parent;
  }

  public List<Node> getChildren()
  {
    return this.children;
  }
}
