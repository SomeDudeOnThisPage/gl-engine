package engine.core;

import engine.core.scene.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL30C.GL_CLIP_DISTANCE0;

public class Camera extends GameObject
{
  private static final float sensitivity = 300;

  private Matrix4f projection;

  private boolean clip;
  private Vector4f clippingPlane;

  /**
   * Returns the current projection matrix of the camera.
   * @return Projection Matrix
   */
  public Matrix4f getProjection()
  {
    return this.projection;
  }

  public boolean shouldClip()
  {
    return this.clip;
  }

  public void clip(boolean clip, Vector4f plane)
  {
    this.clip = clip;
    glEnable(GL_CLIP_DISTANCE0);

    if (this.clip)
    {
      this.clippingPlane = plane;
    }
  }

  public void unclip()
  {
    this.clip = false;
    glDisable(GL_CLIP_DISTANCE0);
  }

  public Vector4f getClippingPlane()
  {
    return this.clippingPlane;
  }

  /**
   * Calculates and returns a view matrix for the current camera.
   * @return View Matrix
   */
  public Matrix4f getView()
  {
    Matrix4f pitch = new Matrix4f().rotateLocalX(this.rotation.x);
    Matrix4f yaw = new Matrix4f().rotateLocalY(this.rotation.y);
    Matrix4f roll = new Matrix4f().rotateLocalZ(this.rotation.z);
    Matrix4f rotation = pitch.mul(roll.mul(yaw));
    return rotation.translate(this.position);
  }

  public void invert()
  {
    this.rotation.x = -this.rotation.x;
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
    float speed = 1.0f;
    if (Input.keyDown(GLFW_KEY_W)) { this.translate((float) Math.sin(rotation.y) * -speed, 0.0f, (float) Math.cos(rotation.y) * speed); }
    if (Input.keyDown(GLFW_KEY_A)) { this.translate((float) Math.sin(rotation.y -rad) * -speed, 0.0f, (float) Math.cos(rotation.y -rad) * speed); }
    if (Input.keyDown(GLFW_KEY_S)) { this.translate((float) Math.sin(rotation.y) * speed, 0.0f, (float) Math.cos(rotation.y) * -speed); }
    if (Input.keyDown(GLFW_KEY_D)) { this.translate((float) Math.sin(rotation.y -rad) * speed, 0.0f, (float) Math.cos(rotation.y -rad) * -speed); }

    // translate y
    if (Input.keyDown(GLFW_KEY_SPACE)) { this.translate(0.0f, -speed, 0.0f); }
    if (Input.keyDown(GLFW_KEY_LEFT_CONTROL)) { this.translate(0.0f, speed, 0.0f); }
  }

  public Camera()
  {
    this.projection = new Matrix4f().perspective(1.04f, 1600.0f / 900.0f, 0.1f, 1000.0f);
    this.clip = false;
    this.clippingPlane = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f);
  }
}
