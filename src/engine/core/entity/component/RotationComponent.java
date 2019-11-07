package engine.core.entity.component;

import engine.core.entity.Entity;
import org.joml.Vector3f;

public class RotationComponent extends EntityComponent
{
  private Vector3f rotation;

  @Override
  public void update(Entity entity, int dt) {}

  @Override
  public String name()
  {
    return "rotation";
  }

  public void setRotation(float x, float y, float z)
  {
    this.rotation.x = x;
    this.rotation.y = y;
    this.rotation.z = z;
  }

  public void rotate(float x, float y, float z)
  {
    this.rotation.add(x, y, z);
  }

  public Vector3f get()
  {
    return this.rotation;
  }

  public RotationComponent(float x, float y, float z)
  {
    this.rotation = new Vector3f(x, y, z);
  }

  public RotationComponent()
  {
    this.rotation = new Vector3f(0.0f);
  }
}
