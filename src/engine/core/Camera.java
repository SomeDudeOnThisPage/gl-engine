package engine.core;

import org.joml.Matrix4f;
import org.joml.Vector2d;

import static org.lwjgl.glfw.GLFW.*;

public class Camera extends GameObject
{
  private static final float sensitivity = 300;

  private Matrix4f projection;

  /**
   * Returns the current projection matrix of the camera.
   * @return Projection Matrix
   */
  public Matrix4f getProjection()
  {
    return this.projection;
  }

  /**
   * Calculates and returns a view matrix for the current camera.
   * @return View Matrix
   */
  public Matrix4f getView()
  {
    Matrix4f yaw = new Matrix4f().rotateLocalX(this.rotation.x);
    Matrix4f roll = new Matrix4f().rotateLocalY(this.rotation.y);
    Matrix4f pitch = new Matrix4f().rotateLocalZ(this.rotation.z);
    Matrix4f rotation = yaw.mul(pitch.mul(roll));
    return rotation.translate(this.position);
  }

  /**
   * Updates the projection matrix of the camera.
   * This function should be called whenever the aspect-ratio of the viewport changes
   */
  public void updateMatrices()
  {
    // todo
  }

  @Override
  public void render(Camera camera) {}

  @Override
  public void update()
  {
    Vector2d drag = Input.getDrag();
    this.rotate((float) drag.y * (1 / sensitivity), (float) drag.x * (1 / sensitivity), 0.0f);

    // clamp pitch
    this.rotation.x = Math.min(this.rotation.x, (float) Math.toRadians(89.0f));
    this.rotation.x = Math.max(this.rotation.x, (float) Math.toRadians(-89.0f));


    // first person movement dependent on the current rotation vector
    // todo: should probably be in its' own class
    double rad = Math.toRadians(90.0f);
    float speed = 0.1f;
    if (Input.keyDown(GLFW_KEY_W)) { this.translate((float) Math.sin(rotation.y) * -speed, 0.0f, (float) Math.cos(rotation.y) * speed); }
    if (Input.keyDown(GLFW_KEY_A)) { this.translate((float) Math.sin(rotation.y -rad) * -speed, 0.0f, (float) Math.cos(rotation.y -rad) * speed); }
    if (Input.keyDown(GLFW_KEY_S)) { this.translate((float) Math.sin(rotation.y) * speed, 0.0f, (float) Math.cos(rotation.y) * -speed); }
    if (Input.keyDown(GLFW_KEY_D)) { this.translate((float) Math.sin(rotation.y -rad) * speed, 0.0f, (float) Math.cos(rotation.y -rad) * -speed); }

    // translate y
    if (Input.keyDown(GLFW_KEY_SPACE)) { this.translate(0.0f, -0.1f, 0.0f); }
    if (Input.keyDown(GLFW_KEY_LEFT_CONTROL)) { this.translate(0.0f, 0.1f, 0.0f); }
  }

  public Camera()
  {
    this.projection = new Matrix4f().perspective(1.04f, 1600.0f / 900.0f, 0.1f, 100.0f);
  }
}
