package engine;

import engine.core.*;
import engine.core.scene.Scene;
import engine.util.Window;

public class Engine
{
  public static final Window window = new Window();

  private void loop()
  {
    Input.initialize(window);

    Scene scene = new GameScene();

    while (!window.shouldClose())
    {
      Input.update();

      scene.render();
      scene.update();
      scene.updateObjects();

      window.update();

      try
      {
        Thread.sleep(1);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args)
  {
    new Engine().loop();
  }
}