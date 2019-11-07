package engine.core.scene;

import engine.core.scene.prefabs3D.Camera3D;
import engine.core.entity.Entity;

import java.util.ArrayList;

public abstract class Scene
{
  private ArrayList<Entity> objects = new ArrayList<>();
  protected Camera3D camera = new Camera3D();
  private boolean fin = false;

  public void add(Entity object)
  {
    objects.add(object);
    object.setScene(this);
  }

  public Camera3D getCamera()
  {
    return this.camera;
  }

  public ArrayList<Entity> getGameObjects()
  {
    return this.objects;
  }

  public void render()
  {
    for (Entity object:this.objects)
    {
      object.render(this.camera);
    }
  }

  public void updateObjects(int dt)
  {
    for (Entity object:this.objects)
    {
      object.updateComponents(dt);
    }
    this.camera.updateComponents(dt);
  }

  public boolean finished()
  {
    return fin;
  }

  public void finish()
  {
    this.fin = true;
  }

  public abstract void update(int dt);

  ///////////////////////////////////////////////////////////////////////////
  // Hooks
  ///////////////////////////////////////////////////////////////////////////
  public /* abstract */ void onEnter() {}
  public /* abstract */ void onExit() {}
}