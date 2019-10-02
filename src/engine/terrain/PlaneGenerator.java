package engine.terrain;

import engine.core.gfx.VertexArray;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public class PlaneGenerator {

  private static float getHeight(BufferedImage heightmap, float x, float y, float amplitude)
  {
    if (x >= heightmap.getHeight() || y >= heightmap.getHeight())
    {
      return 0.0f;
    }

    float height = (heightmap.getRGB((int) x, (int) y)) / (float) (255 * 255 * 255);
    height += 1.0f / 2.0f;
    height /= 1.0f / 2.0f;
    height *= 1.0f / 2.0f;
    return height * amplitude;
  }

  private static void generateVertices(boolean invert, int i, int j, float x, float y, int step, float amplitude, BufferedImage heightmap, Vector3f[] vertices)
  {
    if (invert)
    {
      vertices[0] = new Vector3f((float) i, getHeight(heightmap, x + i, y + j, amplitude), (float) j);
      vertices[1] = new Vector3f((float) i + step, getHeight(heightmap, x + i + step, y + j, amplitude), (float) j);
      vertices[2] = new Vector3f((float) i + step, getHeight(heightmap, x + i + step, y + j + step, amplitude), (float) j + step);
      vertices[3] = new Vector3f((float) i, getHeight(heightmap, x + i, y + j, amplitude), (float) j);
      vertices[4] = new Vector3f((float) i + step, getHeight(heightmap, x + i + step, y + j + step, amplitude), (float) j + step);
      vertices[5] = new Vector3f((float) i, getHeight(heightmap, x + i, y + j + step, amplitude), (float) j + step);
    }
    else
    {
      vertices[0] = new Vector3f((float) i, getHeight(heightmap, x + i, y + j, amplitude), (float) j);
      vertices[1] = new Vector3f((float) i + step, getHeight(heightmap, x + i + step, y + j, amplitude), (float) j);
      vertices[2] = new Vector3f((float) i, getHeight(heightmap, x + i, y + j + step, amplitude), (float) j + step);
      vertices[3] = new Vector3f((float) i + step, getHeight(heightmap, x + i + step, y + j, amplitude), (float) j);
      vertices[4] = new Vector3f((float) i + step, getHeight(heightmap, x + i + step, y + j + step, amplitude), (float) j + step);
      vertices[5] = new Vector3f((float) i, getHeight(heightmap, x + i, y + j + step, amplitude), (float) j + step);
    }
  }

  // this algorithm is fuck lmao
  public static VertexArray generateWithHeightmap(int size, int step, float x, float y, BufferedImage heightmap, float amplitude, boolean[] lod)
  {
    VertexArray vao = new VertexArray();

    float[] vertices = new float[size * size * 6 * 3];
    float[] normals = new float[size * size * 6 * 3];

    int v = 0;
    int n = 0;
    Vector3f[] vx = new Vector3f[6];

    Vector3f[] vxp = new Vector3f[6];

    int rows = 0;
    int columns = 0;

    for (int i = 0; i < size; i += step)
    {
      for (int j = 0; j < size; j += step)
      {
        vxp = vx;
        generateVertices(columns % 2 == 0, i, j, x, y, step, amplitude, heightmap, vx);

        if (lod[0] && rows == 0 && columns % 2 == 0)
        {
          // average vertex height
        }

        // move generated stuff to mesh
        for (int l = 0; l < vx.length; l+=3)
        {
          // insert triangle vertices
          for (int u = 0; u < 3; u++)
          {
            vertices[v++] = vx[l + u].x;
            vertices[v++] = vx[l + u].y;
            vertices[v++] = vx[l + u].z;
          }

          // insert triangle normals
          Vector3f vec1 = vx[l + 1].sub(vx[l]);
          Vector3f vec2 = vx[l + 2].sub(vx[l]);
          Vector3f normal = vec1.cross(vec2);
          for (int k = 0; k < 9; k+=3)
          {
            normals[n++] = -normal.x;
            normals[n++] = -normal.y;
            normals[n++] = -normal.z;
          }
        }
        columns++;
      }
      rows++;
    }

    vao.addAttribute(0, 3, vertices);
    vao.addAttribute(1, 3, normals);
    return vao;
  }

  public static VertexArray generate(int size, int step)
  {
    VertexArray vao = new VertexArray();

    float[] vertices = new float[size * size * 6 * 3];
    float[] normals = new float[size * size * 6 * 3];

    int v = 0;
    int n = 0;
    Vector3f v1, v2, v3, v4, v5, v6;

    for (int i = 0; i < size; i += step)
    {
      for (int j = 0; j < size; j += step)
      {
        if (j % 2 == 0)
        {
          v1 = new Vector3f((float) i, 0.0f, (float) j);
          v2 = new Vector3f((float) i + step, 0.0f, (float) j);
          v3 = new Vector3f((float) i + step, 0.0f, (float) j + step);
          v4 = new Vector3f((float) i, 0.0f, (float) j);
          v5 = new Vector3f((float) i + step, 0.0f, (float) j + step);
          v6 = new Vector3f((float) i, 0.0f, (float) j + step);
        }
        else
        {
          v1 = new Vector3f((float) i, 0.0f, (float) j);
          v2 = new Vector3f((float) i + step, 0.0f, (float) j + step);
          v3 = new Vector3f((float) i, 0.0f, (float) j + step);
          v4 = new Vector3f((float) i, 0.0f, (float) j);
          v5 = new Vector3f((float) i + step, 0.0f, (float) j);
          v6 = new Vector3f((float) i + step, 0.0f, (float) j + step);
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

  public static VertexArray generateGood()
  {
    VertexArray vao = new VertexArray();

    return vao;
  }

  public static VertexArray generate(int size)
  {
    return PlaneGenerator.generate(size, 1);
  }
}
