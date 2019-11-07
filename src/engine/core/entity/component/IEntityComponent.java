package engine.core.entity.component;

import engine.core.entity.Entity;

public interface IEntityComponent
{
  void update(Entity entity, int dt);
  String name();
}
