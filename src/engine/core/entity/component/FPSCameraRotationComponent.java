package engine.core.entity.component;

import engine.core.scene.prefabs3D.Camera3D;
import engine.core.Input;
import engine.core.entity.Entity;
import org.joml.Vector2d;

public class FPSCameraRotationComponent extends RotationComponent
{
  @Override
  public void update(Entity entity, int dt)
  {
    Vector2d drag = Input.getDrag();
    float sensitivity = ((Camera3D) entity).getSensitivity();

    // clamp pitch
    float rotationX = Math.max(Math.min((float) drag.y * (1 / sensitivity), (float) Math.toRadians(89.0f)), (float) Math.toRadians(-89.0f));
    float rotationY = (float) drag.x * (1 / sensitivity);

    this.rotate(rotationX, rotationY, 0.0f);
  }

  @Override
  public String name()
  {
    return "fps_camera_rotation";
  }
}