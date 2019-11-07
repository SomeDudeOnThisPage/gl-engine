package engine.core.scene;

import engine.Engine;

public class SceneManager
{
  public Scene active;
  public Thread thread;

  public void setScene(Scene scene)
  {
    if (this.active != null && this.thread != null)
    {
      // exit active scene
      this.active.finish();
      this.active.onExit();

      // join old scenes' thread
      try { this.thread.join(); }
      catch (InterruptedException e) { e.printStackTrace(); }
    }

    // initialize new scene
    this.active = scene;
    this.active.onEnter();

    // start scene thread
    this.thread = new Thread(new SceneThread(this.active));
    this.thread.start();
    Engine.THREADS += 1;
  }

  public void render()
  {
    if (this.active != null)
    {
      this.active.render();
    }
  }

  public void terminate()
  {
    if (this.active != null && this.thread != null)
    {
      this.active.finish();
      this.active.onExit();
      try
      {
        this.thread.join();
        Engine.THREADS -= 1;
      }
      catch (InterruptedException e) { e.printStackTrace(); }
    }
  }
}
