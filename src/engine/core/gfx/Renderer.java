package engine.core.gfx;

import engine.core.scene.GameObject;

import java.util.List;

public abstract class Renderer extends Thread
{
  public abstract void render(List<GameObject> entities);
}
