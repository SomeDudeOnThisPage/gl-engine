package engine.core.scene;

import engine.Engine;
import engine.core.Input;

public class SceneThread implements Runnable
{
  /**
   * Scene of which this thread is managing the object updating methods.
   */
  private Scene scene;

  @Override
  public void run()
  {
    long last = System.nanoTime();
    long lastUPS = System.nanoTime();
    int ups = 0;

    while (!this.scene.finished())
    {
      // calculate delta time since last update (in ms)
      long time = System.nanoTime();
      int dt = (int) ((time - last) / 1000000);

      // update ups count
      if (time - lastUPS >= 1000000000)
      {
        lastUPS = System.nanoTime();

        Engine.UPS = ups;

        ups = 0;
      }

      this.scene.updateObjects(dt);
      this.scene.update(dt);

      // reset update-cycle based input parameters (like mouse drag amount)
      Input.reset();

      last = time;

      // increment update counter
      ups++;

      try { Thread.sleep(1); }
      catch (InterruptedException e) { e.printStackTrace(); }
    }
  }

  public SceneThread(Scene scene)
  {
    this.scene = scene;
  }
}
