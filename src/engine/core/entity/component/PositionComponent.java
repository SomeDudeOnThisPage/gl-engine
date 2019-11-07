package engine.core.entity.component;

import engine.core.entity.Entity;
import org.joml.Vector3f;

public class PositionComponent extends EntityComponent
{
  private String NAME = "position";

  private Vector3f position;

  @Override
  public void update(Entity entity, int dt) {}

  @Override
  public String name()
  {
    return "position";
  }

  public void translate(float x, float y, float z)
  {
    this.position.add(x, y, z);
  }

  public void translate(Vector3f vector)
  {
    this.position.add(vector);
  }

  public void set(Vector3f position)
  {
    this.position = position;
  }

  public void set(float x, float y, float z)
  {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
  }

  public Vector3f get()
  {
    return this.position;
  }

  public PositionComponent()
  {
    this(new Vector3f(0.0f));
  }

  public PositionComponent(float x, float y, float z)
  {
    this(new Vector3f(x, y, z));
  }

  public PositionComponent(Vector3f position)
  {
    this.position = position;
  }

}
