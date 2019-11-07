package engine;

import engine.core.*;
import engine.core.gfx.FrameBuffer;
import engine.core.gfx.VertexArray;
import engine.core.scene.Scene;
import engine.core.scene.SceneManager;
import engine.core.Window;

public class Engine
{
  public static int THREADS = 1;

  public static Window window;
  private static SceneManager scene_manager;
  private static int cores;

  public static int FPS;
  public static int UPS;

  private static void initialize()
  {
    Engine.window = new Window();
    Input.initialize(window);

    Engine.scene_manager = new SceneManager();

    Engine.FPS = 0;
    Engine.UPS = 0;

    Engine.cores = Runtime.getRuntime().availableProcessors();
    if (Engine.cores <= 0) { System.err.println("how did you even manage to do this"); }
  }

  private static void terminate()
  {
    Engine.scene_manager.terminate();
    FrameBuffer.terminate();
    VertexArray.terminate();
  }

  private static void loop()
  {
    // todo: load game scene as requested by user
    Scene scene = new GameScene();
    scene_manager.setScene(scene);

    long lfps = System.nanoTime();
    int fps = 0;

    while (!window.shouldClose())
    {
      long time = System.nanoTime();

      if (time - lfps > 1000000000)
      {
        Engine.FPS = fps;
        System.out.println("FPS: " + Engine.FPS + " UPS: " + Engine.UPS);
        lfps = System.nanoTime();
        fps = 0;
      }

      Input.update();

      Engine.window.clear();
      Engine.scene_manager.render();
      Engine.window.update();

      fps++;

      try { Thread.sleep(1); }
      catch (InterruptedException e) { e.printStackTrace(); }
    }
  }

  public static void main(String[] args)
  {
    Engine.initialize();
    Engine.loop();
    Engine.terminate();
  }
}