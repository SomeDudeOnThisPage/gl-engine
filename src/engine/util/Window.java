package engine.util;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13C.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GLCapabilities;

public class Window
{
  private long window;
  private int width = 1600;
  private int height = 900;

  public long getWindow()
  {
    return window;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public boolean shouldClose()
  {
    return glfwWindowShouldClose(window);
  }

  public void update()
  {
    glfwSwapBuffers(window);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  public Window()
  {
    if (!glfwInit())
    {
      System.err.println("[FATAL] Could not initialize GLFW.");
      System.exit(1);
    }

    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_SAMPLES, 4);

    window = glfwCreateWindow(width, height, "OpenGL Testing", NULL, NULL);

    glfwSetWindowSizeCallback(window, (window, w, h) ->
    {
        width = w;
        height = h;

        glfwSetWindowSize(window, w, h);
        glViewport(0, 0, w, h);

        //Player.getPlayers().forEach((k,v) -> v.getCamera().updateMatrices(w, h));
    });

    glfwMakeContextCurrent(window);
    glfwSwapInterval(1);
    createCapabilities();

    glClearColor(0.01f, 0.01f, 0.01f, 1.0f);

    glEnable(GL_MULTISAMPLE);

    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  }
}