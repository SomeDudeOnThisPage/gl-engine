package engine.core.entity.component;

import engine.core.entity.Entity;

/**
 * An EntityComponentThread acts like a normal EntityComponent, with the difference that its' logic is run on a separate thread.
 * This might be especially useful for high-performance activities like generating terrain or animations.
 * Note that there is no synchronization whatsoever for these threads by default, meaning that, when the ECS queries data from a
 * component, the current data set is returned. Thus, it might be useful to set the getter-methods to synchronized.
 * I have to look more into multithreading though. :/
 */
public abstract class EntityComponentThread extends EntityComponent implements Runnable
{
  private Entity entity;

  @Override
  public void run()
  {
    //this.update(this.entity, dt);
  }

  public EntityComponentThread(Entity entity)
  {
    this.entity = entity;
  }
}
