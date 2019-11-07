package engine;

import engine.core.Input;
import engine.core.scene.prefabs3D.Model3D;
import engine.core.entity.component.FPSCameraMovementComponent;
import engine.core.scene.Scene;
import engine.core.gfx.Material;
import engine.core.gfx.Shader;
import engine.core.gfx.Texture;
import engine.gui.Label;
import engine.gui.text.BitmapFont;
import engine.terrain.qt.QTTerrain;
import engine.util.WavefrontOBJLoader;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class GameScene extends Scene
{
  private Label label1, label2, label3, label4;
  private QTTerrain terrain;

  private boolean wireframe = false;
  private boolean updateQT = true;

  private long lastSwitchWireframe = System.currentTimeMillis();
  private long lastSwitchUpdateQT = System.currentTimeMillis();

  @Override
  public void onEnter()
  {
    System.out.println("Hello World!");
  }

  @Override
  public void onExit()
  {
    System.out.println("Goodbye World!");
  }

  @Override
  public void update(int dt)
  {
    if (Input.keyDown(GLFW_KEY_Q) && this.lastSwitchWireframe <= System.currentTimeMillis())
    {
      this.wireframe = !this.wireframe;
      this.lastSwitchWireframe = System.currentTimeMillis() + 1000;
    }

    if (Input.keyDown(GLFW_KEY_E) && this.lastSwitchUpdateQT <= System.currentTimeMillis())
    {
      this.updateQT = !this.updateQT;
      this.lastSwitchUpdateQT = System.currentTimeMillis() + 1000;
    }
  }

  @Override
  public void render()
  {
    if (this.wireframe)
    {
      glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
      label2.setText("Rendering mode: GL_LINE");
    }
    else
    {
      glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
      label2.setText("Rendering mode: GL_FILL");
    }

    label3.setText("Updating QuadTree: " + this.updateQT);

    if (this.updateQT)
    {
      this.terrain.updateQuadTree(this.camera);
    }

    this.label4.setText("Threads: " + Engine.THREADS);

    super.render();
    this.label1.render();
    this.label2.render();
    this.label3.render();
    this.label4.render();
  }

  public GameScene()
  {
    Shader shader = new Shader("diffuse");

    // todo: static default material library
    Material duck = new Material(
      new Vector3f(0.1f, 0.1f, 0.1f),
      new Vector3f(1.0f, 1.0f, 1.0f),
      new Vector3f(1.0f, 1.0f, 1.0f),
      0.4f
    );

    Texture texture = new Texture("duck");
    duck.addMap(0, texture);

    Model3D model = new Model3D(WavefrontOBJLoader.load("duc"), shader, duck);
    model.scale(0.035f); // todo: use scale component not deprecated scale set in class

    this.add(model);

    // use static lights set in shader for debugging purposes
    // this.add(new GlobalLightSourceDirectional(new Vector3f(1000.0f, 1000.0f, 1000.0f)));
    // this.add(new PointLightDirectional(new Vector3f(), new Vector3f(-1.0f, -1.0f, -0.5f)));
    // todo: deferred rendering

    this.terrain = new QTTerrain("t0");
    this.add(this.terrain);

    BitmapFont font = new BitmapFont("arial", 32);
    this.label1 = new Label("This is still quite CPU inefficient, as I calculate the distance to every quad of the quadtree every frame.", font, 25, 10);
    this.label2 = new Label("doot", font, 25, 30);
    this.label3 = new Label("dööt", font, 25, 50);
    this.label4 = new Label("düüt", font, 25, 70);

    ((FPSCameraMovementComponent) this.camera.getComponent("fps_camera_movement")).translate(0.0f, 0.0f, -2.5f);
  }
}