package engine.core.gfx;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
  private static ArrayList<Shader> loaded = new ArrayList<>();
  private static int bound = 0;

  private Matrix4f pr_matrix;
  private int program;

  private static int load(int type, String name)
  {
    int shader = glCreateShader(type);

    try
    {
      String program = new String(Files.readAllBytes(Paths.get("resources/shaders/" + name)));

      glShaderSource(shader, program);
      glCompileShader(shader);

      if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
      {
        System.err.println("Failed to compile \'" + name +"\':");
        System.err.println(glGetShaderInfoLog(shader));
      }

      return shader;
    }
    catch(IOException e)
    {
      System.err.println("Could not load shader '" + name);
      e.printStackTrace();
      return -1;
    }
  }

  public static ArrayList<Shader> getLoaded()
  {
    return loaded;
  }

  public void setUniform(String name, Matrix4f data)
  {
    bind();

    FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
    data.get(buffer);

    int location = glGetUniformLocation(program, name);
    glUniformMatrix4fv(location, false, buffer);

    unbind();
  }

  public void setUniform(String name, Vector3f data)
  {
    bind();

    FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
    data.get(buffer);

    int location = glGetUniformLocation(program, name);
    glUniform3fv(location, buffer);

    unbind();
  }

  public void setUniform(String name, Vector2f data)
  {
    bind();

    FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
    data.get(buffer);

    int location = glGetUniformLocation(program, name);
    glUniform2fv(location, buffer);

    unbind();
  }

  public void setUniform(String name, int data)
  {
    bind();

    int location = glGetUniformLocation(program, name);
    glUniform1i(location, data);

    unbind();
  }

  public void setUniform(String name, float data)
  {
    bind();

    int location = glGetUniformLocation(program, name);
    glUniform1f(location, data);

    unbind();
  }

  public void bind()
  {
    glUseProgram(program);
  }

  public void unbind()
  {
    glUseProgram(0);
  }

  public Shader(String name)
  {
    program = glCreateProgram();

    int vshader = Shader.load(GL_VERTEX_SHADER, name + ".vs");
    int fshader = Shader.load(GL_FRAGMENT_SHADER, name + ".fs");

    glAttachShader(program, vshader);
    glAttachShader(program, fshader);
    glLinkProgram(program);

    loaded.add(this);
  }
}
