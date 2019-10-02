package engine;

import engine.core.Input;
import engine.core.Model3D;
import engine.core.scene.Scene;
import engine.core.gfx.Material;
import engine.core.gfx.Shader;
import engine.core.gfx.Texture;
import engine.gui.Label;
import engine.gui.text.BitmapFont;
import engine.terrain.Terrain;
import engine.terrain.WaterPlane;
import engine.util.WavefrontOBJLoader;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class GameScene extends Scene
{
  private Model3D model1;
  private Terrain terrain;
  private WaterPlane water;

  private static int fc = 0;

  private Label label;

  public void initialize() {}

  @Override
  public void update()
  {
    if (!Input.keyDown(GLFW_KEY_Q))
    {
      if (fc >= 30)
      {
        terrain.updateQuadTree(this.camera);
        fc = 0;
      }
      fc++;
    }

    if (Input.keyDown(GLFW_KEY_E))
    {
      glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }
    else
    {
      glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
  }

  @Override
  public void render()
  {
    //water.renderReflection(this);
    //water.renderRefraction(this);
    super.render();
    label.render();
  }

  public GameScene()
  {
    Shader shader = new Shader("diffuse");
    /*Material gold = new Material(
      new Vector3f(0.24725f, 0.1995f, 0.0745f),
      new Vector3f(0.75164f, 0.60648f, 0.22648f),
      new Vector3f(0.628281f, 0.555802f, 0.366065f),
      0.4f
    );*/

    Material normal = new Material(
      new Vector3f(0.1f, 0.1f, 0.1f),
      new Vector3f(1.0f, 1.0f, 1.0f),
      new Vector3f(1.0f, 1.0f, 1.0f),
      0.4f
    );

    Texture texture = new Texture("duck");
    normal.addMap(0, texture);

    this.model1 = new Model3D(WavefrontOBJLoader.load("duc"), shader, normal);
    this.model1.scale(0.035f);

    this.add(this.model1);

    this.camera.translate(0.0f, 0.0f, -2.5f);

    this.terrain = new Terrain("t0");
    this.add(this.terrain);

    BitmapFont font = new BitmapFont("arial", 42);
    this.label = new Label("Hello World", font, 100, 100);

    //this.water = new WaterPlane(512);
    //this.add(this.water);
  }
}