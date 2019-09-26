package engine.core;

import java.util.ArrayList;

public abstract class Scene
{
  private ArrayList<GameObject> objects = new ArrayList<>();
  protected Camera camera = new Camera();

  public void add(GameObject object)
  {
    objects.add(object);
  }

  public void render()
  {
    for (GameObject object:this.objects)
    {
      object.render(this.camera);
    }
  }

  public void updateObjects()
  {
    for (GameObject object:this.objects)
    {
      object.update();
    }

    this.camera.update();
  }

  public abstract void update();
  public abstract void initialize();
}