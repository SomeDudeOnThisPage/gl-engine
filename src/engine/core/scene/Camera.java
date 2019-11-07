package engine.core.scene;

import engine.core.entity.Entity;
import engine.core.gfx.Shader;
import org.joml.Matrix4f;

public abstract class Camera extends Entity
{
  private Matrix4f projection;

  /**
   * Updates the matrices of the camera.
   * This is mostly used to update projection matrices upon window size change.
   */
  public abstract void updateMatrices();

  /**
   * Binds the camera to a shader.
   * This method should set the cameras' uniform values in the given shader.
   * @param shader Shader to be used
   */
  public abstract void bind(Shader shader);

  /**
   * Returns the current projection matrix of the camera.
   * @return Projection Matrix
   */
  public Matrix4f getProjection()
  {
    return this.projection;
  }

  /**
   * Sets the current projection matrix of the camera.
   * @param projection
   */
  public void setProjection(Matrix4f projection) { this.projection = projection; }
}
