package engine.core.scene.prefabs2D;

import engine.Engine;
import engine.core.gfx.Shader;
import engine.core.scene.Camera;
import engine.core.scene.prefabs3D.Camera3D;
import org.joml.Matrix4f;

public class Camera2D extends Camera
{
  @Override
  public void updateMatrices()
  {

  }

  @Override
  public void bind(Shader shader)
  {

  }

  @Override
  public void render(Camera3D camera)
  {

  }

  public Camera2D()
  {
    int width = Engine.window.getWidth() / 2;
    int height = Engine.window.getHeight() / 2;
    this.setProjection(new Matrix4f().ortho2D(-width, width, -height, height));
  }
}
