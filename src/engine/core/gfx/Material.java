package engine.core.gfx;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11C.glGetInteger;
import static org.lwjgl.opengl.GL20C.GL_MAX_TEXTURE_IMAGE_UNITS;

public class Material
{
  private static final int MAX_TEXTURES = glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);

  private Vector3f ambient;
  private Vector3f diffuse;
  private Vector3f specular;
  private float shininess;

  private Texture[] textures;

  /**
   * Adds a texture map to the material.
   */
  public void addMap(int index, Texture texture)
  {
    if (index >= Material.MAX_TEXTURES)
    {
      System.err.println("cannot bind texture to index " + index + " - no more than " + MAX_TEXTURES + " texture samplers allowed");
    }

    this.textures[index] = texture;
  }

  /**
   * Sets the required uniforms in the shader for material calculation.
   * Access them as follows:<br>
   * uniform struct<br>
   * {<br>
   *   vec3 ambient;<br>
   *   vec3 diffuse;<br>
   *   vec3 specular;<br>
   *   float shininess;<br>
   * } material;<br>
   * @param shader Shader to set the uniforms of
   */
  public void bind(Shader shader)
  {
    shader.setUniform("material.ambient", this.ambient);
    shader.setUniform("material.diffuse", this.diffuse);
    shader.setUniform("material.specular", this.specular);
    shader.setUniform("material.shininess", this.shininess);

    for (int i = 0; i < textures.length; i++)
    {
      if (textures[i] != null)
      {
        textures[i].bind(i);
        shader.setUniform("texture" + i, i);
      }
    }
  }

  public void unbind()
  {
    for (int i = 0; i < textures.length; i++)
    {
      if (textures[i] != null)
      {
        textures[i].unbind(i);
      }
    }
  }

  public Material(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess)
  {
    this.ambient = ambient;
    this.diffuse = diffuse;
    this.specular = specular;
    this.shininess = shininess;

    this.textures = new Texture[MAX_TEXTURES];
  }
}
