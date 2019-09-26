package engine.terrain;

import engine.core.Camera;
import engine.core.GameObject;
import engine.core.gfx.Shader;
import engine.core.gfx.VertexArray;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11C.*;

public class WaterPlane extends GameObject
{
  private VertexArray vao;
  private Shader shader;
  private Vector3f position;

  @Override
  public void render(Camera camera)
  {
    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    glLineWidth(5.0f);

    this.shader.setUniform("projection", camera.getProjection());
    this.shader.setUniform("view", camera.getView());

    this.shader.setUniform("model", new Matrix4f().identity().translate(this.position));

    this.shader.bind();
    vao.render();
    this.shader.unbind();

    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
  }

  @Override
  public void update()
  {

  }

  public WaterPlane(int size)
  {
    this.vao = PlaneGenerator.generate(size);
    this.shader = new Shader("water");
    this.position = new Vector3f(0.0f, 0.0f, 0.0f);
  }
}
