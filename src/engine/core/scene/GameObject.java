package engine.core.scene;

import engine.core.Camera;
import engine.core.scene.Scene;
import org.joml.Vector3f;

public abstract class GameObject
{
  protected Vector3f position = new Vector3f(0.0f);
  protected Vector3f rotation = new Vector3f(0.0f); // todo: rotation via quats
  protected float scale = 1.0f;

  protected Scene scene;

  public abstract void render(Camera camera);
  public abstract void update();

  public void setScene(Scene scene)
  {
    this.scene = scene;
  }

  public Vector3f getRotation()
  {
    return this.rotation;
  }

  public Vector3f getPosition()
  {
    return this.position;
  }

  public void scale(float scale)
  {
    this.scale = scale;
  }

  public float getScale()
  {
    return scale;
  }

  public void translate(float x, float y, float z)
  {
    this.position.add(x, y, z);
  }

  public void rotate(float x, float y, float z)
  {
    this.rotation.add(x, y, z);
  }
}
