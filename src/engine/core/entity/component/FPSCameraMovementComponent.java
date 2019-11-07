package engine.core.entity.component;

import engine.core.Input;
import engine.core.entity.Entity;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class FPSCameraMovementComponent extends PositionComponent
{
  @Override
  public void update(Entity entity, int dt)
  {
    Vector3f rotation = ((FPSCameraRotationComponent) entity.getComponent("fps_camera_rotation")).get();

    double rad = Math.toRadians(90.0f);
    float speed = dt * 0.05f;

    // translate on x-plane
    if (Input.keyDown(GLFW_KEY_W)) { this.translate((float) Math.sin(rotation.y) * -speed, 0.0f, (float) Math.cos(rotation.y) * speed); }
    if (Input.keyDown(GLFW_KEY_A)) { this.translate((float) Math.sin(rotation.y -rad) * -speed, 0.0f, (float) Math.cos(rotation.y -rad) * speed); }
    if (Input.keyDown(GLFW_KEY_S)) { this.translate((float) Math.sin(rotation.y) * speed, 0.0f, (float) Math.cos(rotation.y) * -speed); }
    if (Input.keyDown(GLFW_KEY_D)) { this.translate((float) Math.sin(rotation.y -rad) * speed, 0.0f, (float) Math.cos(rotation.y -rad) * -speed); }

    // translate on z-plane
    if (Input.keyDown(GLFW_KEY_SPACE)) { this.translate(0.0f, -speed, 0.0f); }
    if (Input.keyDown(GLFW_KEY_LEFT_CONTROL)) { this.translate(0.0f, speed, 0.0f); }
  }

  @Override
  public String name()
  {
    return "fps_camera_movement";
  }
}
