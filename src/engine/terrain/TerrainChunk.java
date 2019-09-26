package engine.terrain;

import engine.core.gfx.VertexArray;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

public class TerrainChunk
{
  private enum MODE { INTERLEAVED, STATIC }

  private static final int SIZE = 16;
  private static final int MAX_COLOR = 255 * 255 * 255;
  private static final float amplitude = 5.0f;

  private VertexArray vao;
  private BufferedImage heightmap;
  private Vector2f position;

  private float getHeight(int x, int z)
  {
    if (x >= heightmap.getHeight() || z >= heightmap.getHeight())
    {
      return 0.0f;
    }

    float height = this.heightmap.getRGB(x, z) / (float) MAX_COLOR;
    height += 1.0f/2.0f;
    height /= 1.0f/2.0f;
    height *= 1.0f/2.0f;
    return height * amplitude;
  }

  /**
   * Creates a mesh for the chunk based on a level of detail or something.
   */
  public VertexArray generate()
  {
    float[] vertices = new float[TerrainChunk.SIZE * TerrainChunk.SIZE * 6 * 3];
    float[] normals = new float[TerrainChunk.SIZE * TerrainChunk.SIZE * 6 * 3];

    int v = 0;
    int n = 0;
    Vector3f v1, v2, v3, v4, v5, v6;

    for (int i = 0; i < TerrainChunk.SIZE; i++)
    {
      for (int j = 0; j < TerrainChunk.SIZE; j++)
      {
        if (j % 2 == 0)
        {
          v1 = new Vector3f((float) i, this.getHeight(i, j), (float) j);
          v2 = new Vector3f((float) i + 1, this.getHeight(i + 1, j), (float) j);
          v3 = new Vector3f((float) i + 1, this.getHeight(i + 1, j + 1), (float) j + 1);
          v4 = new Vector3f((float) i, this.getHeight(i, j), (float) j);
          v5 = new Vector3f((float) i + 1, this.getHeight(i + 1, j + 1), (float) j + 1);
          v6 = new Vector3f((float) i, this.getHeight(i, j + 1), (float) j + 1);
        }
        else
        {
          v1 = new Vector3f((float) i, this.getHeight(i, j), (float) j);
          v2 = new Vector3f((float) i + 1, this.getHeight(i + 1, j + 1), (float) j + 1);
          v3 = new Vector3f((float) i, this.getHeight(i, j + 1), (float) j + 1);
          v4 = new Vector3f((float) i, this.getHeight(i, j), (float) j);
          v5 = new Vector3f((float) i + 1, this.getHeight(i + 1, j), (float) j);
          v6 = new Vector3f((float) i + 1, this.getHeight(i + 1, j + 1), (float) j + 1);
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

    this.vao.addAttribute(0, 3, vertices);
    this.vao.addAttribute(1, 3, normals);
    return this.vao;
  }

  public VertexArray getVAO()
  {
    return this.vao;
  }

  public Vector2f getPosition()
  {
    return this.position;
  }

  public TerrainChunk(BufferedImage heightmap, int x, int y)
  {
    this.vao = new VertexArray();
    this.position = new Vector2f(x * TerrainChunk.SIZE, y * TerrainChunk.SIZE);
    this.heightmap = heightmap;
  }
}