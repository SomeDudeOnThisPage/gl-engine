package engine.terrain;

import engine.core.Camera;
import engine.core.scene.GameObject;
import engine.core.scene.Scene;
import engine.core.gfx.FrameBuffer;
import engine.core.gfx.Shader;
import engine.core.gfx.Texture;
import engine.core.gfx.VertexArray;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class WaterPlane extends GameObject
{
  private VertexArray vao;
  private Shader shader;
  private Vector3f position;
  private FrameBuffer refraction;
  private FrameBuffer reflection;

  public Texture renderRefraction(Scene scene)
  {
    ArrayList<GameObject> objects = scene.getGameObjects();
    Camera camera = scene.getCamera();
    camera.clip(true, new Vector4f(0.0f, -1.0f, 0.0f, 0.05f));

    this.refraction.bind();

    for (GameObject object:objects)
    {
      if (object.getClass() != WaterPlane.class)
      {
        object.render(camera);
      }
    }

    this.refraction.unbind();
    camera.unclip();

    return this.refraction.getTexture();
  }

  public Texture renderReflection(Scene scene)
  {
    ArrayList<GameObject> objects = scene.getGameObjects();
    Camera camera = scene.getCamera();
    camera.clip(true, new Vector4f(0.0f, 1.0f, 0.0f, -0.05f));

    float distance = 2 * (camera.getPosition().y + 0.0f);
    camera.getPosition().y -= distance;
    camera.invert();

    this.reflection.bind();

    for (GameObject object:objects)
    {
      if (object.getClass() != WaterPlane.class)
      {
        object.render(camera);
      }
    }

    this.reflection.unbind();
    camera.unclip();

    camera.invert();
    camera.getPosition().y += distance;

    return this.reflection.getTexture();
  }

  @Override
  public void render(Camera camera)
  {
    this.shader.setCamera(camera);
    this.shader.setUniform("model", new Matrix4f().identity().translate(this.position));

    this.reflection.getTexture().bind(0);
    this.refraction.getTexture().bind(1);
    this.refraction.getDepthTexture().bind(2);

    this.shader.setUniform("reflection", 0);
    this.shader.setUniform("refraction", 1);
    this.shader.setUniform("depth_map", 2);

    this.shader.bind();
    vao.render();
    this.shader.unbind();

    this.reflection.getTexture().unbind(0);
    this.reflection.getTexture().unbind(1);
  }

  @Override
  public void update()
  {

  }

  public FrameBuffer getReflection()
  {
    return this.reflection;
  }

  public FrameBuffer getRefraction()
  {
    return this.refraction;
  }

  public WaterPlane(int size)
  {
    this.vao = PlaneGenerator.generate(size, 16);
    this.refraction = new FrameBuffer(1280, 720);
    this.reflection = new FrameBuffer(1280, 720);

    this.shader = new Shader("water");
    this.position = new Vector3f(0.0f, 0.0f, 0.0f);
  }
}
