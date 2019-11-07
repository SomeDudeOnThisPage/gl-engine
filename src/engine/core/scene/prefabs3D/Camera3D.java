package engine.core.scene.prefabs3D;

import engine.core.entity.component.FPSCameraMovementComponent;
import engine.core.entity.component.FPSCameraRotationComponent;
import engine.core.gfx.Shader;
import engine.core.scene.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL30C.GL_CLIP_DISTANCE0;

/**
 * Prefab camera class with 3D-projection matrix and 3D-FPS like movement.
 */
public class Camera3D extends Camera
{
  private float sensitivity = 250;

  /**
   * Whether the camera should clip or not.
   */
  private boolean clip;

  /**
   * The clipping plane for the camera to clip.
   */
  private Vector4f clippingPlane;

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

  @Override
  public void bind(Shader shader)
  {
    // set clip values in shader
    if (this.clip) { shader.setUniform("clip", this.getClippingPlane()); }

    shader.setUniform("projection", this.getProjection());
    shader.setUniform("view", this.getView());
    shader.setUniform("view_position", this.getPosition());
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
    Vector3f rotation = ((FPSCameraRotationComponent) this.components.get("fps_camera_rotation")).get();
    Vector3f position = ((FPSCameraMovementComponent) this.components.get("fps_camera_movement")).get();

    Matrix4f pitch = new Matrix4f().rotateLocalX(rotation.x);
    Matrix4f yaw = new Matrix4f().rotateLocalY(rotation.y);
    Matrix4f roll = new Matrix4f().rotateLocalZ(rotation.z);
    Matrix4f rotationMatrix = pitch.mul(roll.mul(yaw));
    return rotationMatrix.translate(position);
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

  public float getSensitivity()
  {
    return this.sensitivity;
  }

  @Override
  public void render(Camera3D camera) {}

  public Camera3D()
  {
    this.add(new FPSCameraMovementComponent());
    this.add(new FPSCameraRotationComponent());

    this.setProjection(new Matrix4f().perspective(1.04f, 1600.0f / 900.0f, 0.1f, 10000.0f));
    this.clip = false;
    this.clippingPlane = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f);
  }
}
