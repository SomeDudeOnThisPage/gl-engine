package engine.core.scene.prefabs3D;

import engine.core.gfx.Material;
import engine.core.gfx.Shader;
import engine.core.gfx.VertexArray;
import engine.core.entity.Entity;
import org.joml.Matrix4f;

public class Model3D extends Entity
{
  private VertexArray mesh;
  private Shader shader;
  private Material material;

  public void render(Camera3D camera)
  {
    camera.bind(this.shader);
    this.shader.setUniform("model", new Matrix4f().identity().translate(this.position).rotateXYZ(this.rotation).scale(this.scale));

    this.material.bind(this.shader);

    this.shader.bind();
    this.mesh.render();
    this.shader.unbind();
  }

  public Model3D(VertexArray vao, Shader shader, Material material)
  {
    this.mesh = vao;
    this.shader = shader;
    this.material = material;
  }
}
