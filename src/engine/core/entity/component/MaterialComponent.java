package engine.core.entity.component;

import engine.core.entity.Entity;
import engine.core.gfx.Material;
import org.joml.Vector3f;

public class MaterialComponent extends EntityComponent
{
  private Material material;

  @Override
  public void update(Entity entity, int dt) {}

  @Override
  public String name()
  {
    return "material";
  }

  public Material get()
  {
    return this.material;
  }

  public MaterialComponent(Material material)
  {
    this.material = material;
  }

  public MaterialComponent(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess)
  {
    this(new Material(ambient, diffuse, specular, shininess));
  }
}
