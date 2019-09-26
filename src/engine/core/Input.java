package engine.core;

import engine.util.Window;
import org.joml.Vector2d;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Input
{
  private static long window = -1;
  private static boolean dragging = false;

  private static Vector2d mouseLast = new Vector2d(0.0, 0.0);
  private static Vector2d mouseNow = new Vector2d(0.0, 0.0);

  public static void initialize(Window windowObject)
  {
    window = windowObject.getWindow();

    // set callbacks
    glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
    {
      if (button == GLFW_MOUSE_BUTTON_1)
      {
        if (action == GLFW_PRESS)
        {
          dragging = true;
        }
        else if (action == GLFW_RELEASE)
        {
          dragging = false;
        }
      }
    });

    glfwSetCursorPosCallback(window, (window, x, y) ->
    {
      mouseLast = new Vector2d(mouseNow);
      mouseNow = new Vector2d(x, y);
    });
  }

  public static void update()
  {
    mouseLast = mouseNow;
    glfwPollEvents();
  }

  /**
   * Returns the difference on the x- and y-axis from the last frame.
   * @return 2-Component vector. The first component is the difference on the x-axis, the second component is the position  is the difference on the y-axis
   */
  public static Vector2d getDrag()
  {
    if (dragging)
    {
      return new Vector2d(mouseNow.x - mouseLast.x, mouseNow.y - mouseLast.y);
    }

    return new Vector2d(0.0, 0.0);
  }

  public static boolean keyDown(int key)
  {
    if (window == -1)
    {
      System.err.println("Input module was not initialized.");
      return false;
    }

    return glfwGetKey(window, key) == 1;
  }
}
