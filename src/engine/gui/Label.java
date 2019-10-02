package engine.gui;

import engine.Engine;
import engine.core.gfx.InstancedRenderingVertexArray;
import engine.core.gfx.Shader;
import engine.gui.text.BitmapFont;
import engine.gui.text.StringMeshBuilder;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LESS;

public class Label extends GUIObject
{
  private static final Shader textShader = new Shader("text");
  private static final Matrix4f projection = new Matrix4f().ortho2D(-800, 800, -450, 450);

  private String text;
  private BitmapFont font;

  private InstancedRenderingVertexArray vao;

  @Override
  public void render()
  {
    glDisable(GL_DEPTH_TEST);

    // Setup text shader uniforms
    textShader.setUniform("pr_matrix", projection);
    textShader.setUniform("scale", font.getScale());
    textShader.setUniform("window_w", Engine.window.getWidth() / 2.0f);
    textShader.setUniform("window_h", Engine.window.getHeight() / 2.0f);

    textShader.setUniform("atlas_size", 16);

    textShader.setUniform("position_x", position.x);
    textShader.setUniform("position_y", position.y);

    textShader.bind();
    font.bind();
    vao.render();
    font.unbind();
    textShader.unbind();

    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
  }

  public void setText(String text)
  {
    this.text = text;
    StringMeshBuilder.generate(this.text, this.font, this.vao);
  }

  public Label(String text, BitmapFont font, int x, int y)
  {
    super(x, y);
    this.text = text;
    this.font = font;

    vao = new InstancedRenderingVertexArray();
    // Indices
    vao.addIndices(new int[] {0, 1, 2, 2, 3, 0});
    // Vertex Positions
    vao.addAttribute(0, 2, new float[] {-0.2f, -0.2f, 0.2f, -0.2f, 0.2f, 0.2f, -0.2f, 0.2f});
    // Texture Positions
    vao.addAttribute(1, 2, new float[] {0.0f,  1.0f, 1.0f,  1.0f, 1.0f,  0.0f, 0.0f,  0.0f});
    // Instance World Positions
    vao.addInstancedAttribute(2, Short.MAX_VALUE, 3, 1);
    // Instance Texture Positions
    vao.addInstancedAttribute(3, Short.MAX_VALUE, 2, 1);

    setText(text);
  }
}
