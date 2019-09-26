package engine;

import engine.core.Model3D;
import engine.core.Scene;
import engine.core.gfx.Material;
import engine.core.gfx.Shader;
import engine.core.gfx.Texture;
import engine.terrain.Terrain;
import engine.terrain.WaterPlane;
import engine.util.WavefrontOBJLoader;
import org.joml.Vector3f;

public class GameScene extends Scene
{
  private Model3D model1;
  private Terrain terrain;

  public void initialize() {}

  @Override
  public void update()
  {
    //this.model1.rotate(0.01f, 0.0f, 0.0f);
  }

  public GameScene()
  {
    Shader shader = new Shader("diffuse");
    Material gold = new Material(
      new Vector3f(0.24725f, 0.1995f, 0.0745f),
      new Vector3f(0.75164f, 0.60648f, 0.22648f),
      new Vector3f(0.628281f, 0.555802f, 0.366065f),
      0.4f
    );

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

    this.camera.translate(0.0f, 0.0f, -5.0f);

    this.terrain = new Terrain("splitter");
    this.add(this.terrain);

    WaterPlane water = new WaterPlane(16 * 4);
    this.add(water);
  }
}
