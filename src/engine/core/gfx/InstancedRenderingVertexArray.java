package engine.core.gfx;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL33.*;

public class InstancedRenderingVertexArray extends VertexArray
{
  private int[] buffers;

  private int AMOUNT;

  public void setAmount(int amount)
  {
    AMOUNT = amount;
  }

  private int COUNT = 6;
  @Override
  public void render()
  {
    bind();

    if (this.indices != -1)
    {
      glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, AMOUNT);
    }
    else
    {
      glDrawArraysInstanced(GL_TRIANGLES, 0, COUNT, AMOUNT);
      glDrawArrays(GL_TRIANGLES, 0, COUNT);
    }

    unbind();
  }

  public void updateInstancedAttributeData(int attribute, float[] data)
  {
    if (attribute < MAX_ATTRIBUTES)
    {
      bind();
      glBindBuffer(GL_ARRAY_BUFFER, buffers[attribute]);

      FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
      buffer.put(data);
      buffer.flip();

      glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);

      unbind();
    }
  }

  public void addInstancedAttribute(int attribute, int length, int dataSize, int offset)
  {
    bind();

    int index = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, index);
    glBufferData(GL_ARRAY_BUFFER, length * /* float size in bytes */ 4, GL_STREAM_DRAW);

    glEnableVertexAttribArray(attribute);
    glVertexAttribPointer(attribute, dataSize, GL_FLOAT, false, dataSize * 4, 0L);
    glVertexAttribDivisor(attribute, 1);

    attributes[attribute] = true;
    buffers[attribute] = index;

    glDisableVertexAttribArray(attribute);
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    unbind();
  }

  public InstancedRenderingVertexArray()
  {
    super();
    buffers = new int[8];
  }
}
