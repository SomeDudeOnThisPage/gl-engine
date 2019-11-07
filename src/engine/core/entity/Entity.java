package engine.core.entity;

import engine.core.scene.prefabs3D.Camera3D;
import engine.core.entity.component.EntityComponent;
import engine.core.scene.Scene;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity<T extends EntityComponent>
{
  protected HashMap<String, T> components = new HashMap<>();

  public void add(T component)
  {
    if (this.components.get(component.name()) != null)
    {
      System.err.println("cannot add component of type " + component.name() + " to entity (component already exists)");
      return;
    }

    this.components.put(component.name(), component);
  }

  public T getComponent(String component)
  {
    return this.components.get(component);
  }

  public void remove(String component)
  {
    if (this.components.get(component) == null)
    {
      System.err.println("cannot remove component of type " + component + " from entity (component does not exist)");
    }

    this.components.remove(component);
  }

  public void updateComponents(int dt)
  {
    for(Map.Entry<String, T> entry : this.components.entrySet())
    {
      entry.getValue().update(this, dt);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////
  // BEGIN DEPRECATED
  // todo: remove
  //////////////////////////////////////////////////////////////////////////////////////////
  protected Vector3f position = new Vector3f(0.0f);
  protected Vector3f rotation = new Vector3f(0.0f); // todo: rotation via quats
  protected float scale = 1.0f;
  protected Scene scene;
  public abstract void render(Camera3D camera);
  public void setScene(Scene scene) { this.scene = scene; }
  public Vector3f getRotation() { return this.rotation; }
  public Vector3f getPosition() { return this.position; }
  public void scale(float scale) { this.scale = scale; }
  public float getScale() { return scale; }
  public void translate(float x, float y, float z) { this.position.add(x, y, z); }
  public void rotate(float x, float y, float z) { this.rotation.add(x, y, z); }
  //////////////////////////////////////////////////////////////////////////////////////////
  // END DEPRECATED
  //////////////////////////////////////////////////////////////////////////////////////////
}
