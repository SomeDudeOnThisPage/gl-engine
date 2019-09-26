package engine.terrain;

import engine.core.gfx.VertexArray;
import org.joml.Vector3f;

public class PlaneGenerator
{
  public static VertexArray generate(int size)
  {
    VertexArray vao = new VertexArray();

    float[] vertices = new float[size * size * 6 * 3];
    float[] normals = new float[size * size * 6 * 3];

    int v = 0;
    int n = 0;
    Vector3f v1, v2, v3, v4, v5, v6;

    for (int i = 0; i < size; i++)
    {
      for (int j = 0; j < size; j++)
      {
        if (j % 2 == 0)
        {
          v1 = new Vector3f((float) i, 0.0f, (float) j);
          v2 = new Vector3f((float) i + 1, 0.0f, (float) j);
          v3 = new Vector3f((float) i + 1, 0.0f, (float) j + 1);
          v4 = new Vector3f((float) i, 0.0f, (float) j);
          v5 = new Vector3f((float) i + 1, 0.0f, (float) j + 1);
          v6 = new Vector3f((float) i, 0.0f, (float) j + 1);
        }
        else
        {
          v1 = new Vector3f((float) i, 0.0f, (float) j);
          v2 = new Vector3f((float) i + 1, 0.0f, (float) j + 1);
          v3 = new Vector3f((float) i, 0.0f, (float) j + 1);
          v4 = new Vector3f((float) i, 0.0f, (float) j);
          v5 = new Vector3f((float) i + 1, 0.0f, (float) j);
          v6 = new Vector3f((float) i + 1, 0.0f, (float) j + 1);
        }

        // first triangle vertices
        vertices[v++] = v1.x;
        vertices[v++] = v1.y;
        vertices[v++] = v1.z;

        vertices[v++] = v2.x;
        vertices[v++] = v2.y;
        vertices[v++] = v2.z;

        vertices[v++] = v3.x;
        vertices[v++] = v3.y;
        vertices[v++] = v3.z;

        // first triangle normals
        Vector3f vec1 = v2.sub(v1);
        Vector3f vec2 = v3.sub(v1);
        Vector3f normal = vec1.cross(vec2);

        normals[n++] = -normal.x;
        normals[n++] = -normal.y;
        normals[n++] = -normal.z;

        normals[n++] = -normal.x;
        normals[n++] = -normal.y;
        normals[n++] = -normal.z;

        normals[n++] = -normal.x;
        normals[n++] = -normal.y;
        normals[n++] = -normal.z;

        // second triangle vertices
        vertices[v++] = v4.x;
        vertices[v++] = v4.y;
        vertices[v++] = v4.z;

        vertices[v++] = v5.x;
        vertices[v++] = v5.y;
        vertices[v++] = v5.z;

        vertices[v++] = v6.x;
        vertices[v++] = v6.y;
        vertices[v++] = v6.z;

        // second triangle normals
        Vector3f vec3 = v5.sub(v4);
        Vector3f vec4 = v6.sub(v4);
        Vector3f normal2 = vec3.cross(vec4);

        normals[n++] = -normal2.x;
        normals[n++] = -normal2.y;
        normals[n++] = -normal2.z;

        normals[n++] = -normal2.x;
        normals[n++] = -normal2.y;
        normals[n++] = -normal2.z;

        normals[n++] = -normal2.x;
        normals[n++] = -normal2.y;
        normals[n++] = -normal2.z;
      }
    }

    vao.addAttribute(0, 3, vertices);
    vao.addAttribute(1, 3, normals);
    return vao;
  }
}
