package engine.core;

import engine.core.gfx.Material;
import engine.core.gfx.Shader;
import engine.core.gfx.VertexArray;
import org.joml.Matrix4f;

public class Model3D extends GameObject
{
  private VertexArray mesh;
  private Shader shader;
  private Material material;

  public void render(Camera camera)
  {
    // set uniforms
    this.shader.setUniform("projection", camera.getProjection());
    this.shader.setUniform("view", camera.getView());
    this.shader.setUniform("model", new Matrix4f().identity().translate(this.position).rotateXYZ(this.rotation).scale(this.scale));

    this.shader.setUniform("view_position", camera.getPosition());

    this.material.bind(this.shader);

    this.shader.bind();
    this.mesh.render();
    this.shader.unbind();
  }

  public void update() {}

  public Model3D(VertexArray vao, Shader shader, Material material)
  {
    this.mesh = vao;
    this.shader = shader;
    this.material = material;
  }
}
