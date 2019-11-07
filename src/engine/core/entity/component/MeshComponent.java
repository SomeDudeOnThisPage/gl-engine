package engine.core.entity.component;

import engine.core.entity.Entity;
import engine.core.gfx.VertexArray;

public class MeshComponent extends EntityComponent
{
  private VertexArray vao;

  @Override
  public void update(Entity entity, int dt) {}

  @Override
  public String name()
  {
    return "mesh";
  }

  public VertexArray get()
  {
     return this.vao;
  }

  public MeshComponent(VertexArray vao)
  {
    this.vao = vao;
  }
}
