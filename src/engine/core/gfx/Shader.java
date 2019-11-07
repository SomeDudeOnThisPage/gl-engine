package engine.core.gfx;

import engine.core.scene.Camera;
import engine.core.scene.prefabs3D.Camera3D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32C.GL_GEOMETRY_SHADER;

public class Shader
{
  private static ArrayList<Shader> loaded = new ArrayList<>();
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

  public void setUniform(String name, Vector4f data)
  {
    bind();

    FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
    data.get(buffer);

    int location = glGetUniformLocation(program, name);
    glUniform4fv(location, buffer);

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

    if (new File("resources/shaders/" + name + ".vs").exists())
    {
      int vshader = Shader.load(GL_VERTEX_SHADER, name + ".vs");
      glAttachShader(program, vshader);
    }
    else
    {
      System.err.println("error loading shader " + name + " (no vertex shader (\'.vs\') file found)");
    }

    if (new File("resources/shaders/" + name + ".gs").exists())
    {
      int gshader = Shader.load(GL_GEOMETRY_SHADER, name + ".gs");
      glAttachShader(program, gshader);
    }

    if (new File("resources/shaders/" + name + ".fs").exists())
    {
      int fshader = Shader.load(GL_FRAGMENT_SHADER, name + ".fs");
      glAttachShader(program, fshader);
    }
    else
    {
      System.err.println("error loading shader " + name + " (no fragment shader (\'.fs\') file found)");
    }

    glLinkProgram(program);

    loaded.add(this);
  }
}
