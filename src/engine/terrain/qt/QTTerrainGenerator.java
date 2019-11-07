package engine.terrain.qt;

import engine.core.gfx.VertexArray;

import java.awt.image.BufferedImage;

public class QTTerrainGenerator
{
  private static final float COLOR_MAX = 255.0f * 255.0f * 255.0f;

  /**
   * Gets a height-value from a heightmap in the range [-1, 1].
   * @param heightmap heightmap to be used
   * @param x x-position on the heightmap
   * @param y y-position on the heightmap
   * @return height value
   */
  private static float getHeight(BufferedImage heightmap, float x, float y)
  {
    if (x >= heightmap.getHeight() || y >= heightmap.getHeight())
    {
      return 0.0f;
    }

    float height = (heightmap.getRGB((int) x, (int) y)) / COLOR_MAX ;
    height += 1.0f / 2.0f;
    height /= 1.0f / 2.0f;
    height *= 1.0f / 2.0f;
    return height;
  }

  /**
   * Generates an array of indices to render a sub-mesh of the total vertex-array-terrain with a given LOD.
   * @param mapSize total size of the map
   * @param ox offset x
   * @param oy offset z (yes it says 'y' but ignore that please :) )
   * @param size size of the sub-mesh
   * @param lod lod-level
   * @return integer indices array
   */
  public static int[] generateIndices(int mapSize, int ox, int oy, int size, int lod, QTTerrainChunk chunk)
  {
    int[] indices = new int[(size) * (size) * 6 / lod / lod + size * 3];

    QTTerrainChunk c_north = chunk.findGreaterNeighbor(0);
    QTTerrainChunk c_east = chunk.findGreaterNeighbor(1);
    QTTerrainChunk c_south = chunk.findGreaterNeighbor(2);
    QTTerrainChunk c_west = chunk.findGreaterNeighbor(3);

    int i = 0;
    for (int z = oy; z < size + oy; z+=lod)
    {
      for (int x = ox; x < size + ox; x+=lod)
      {
        // determine index location in total map
        int tl = z * mapSize + x;         // global top-right index
        int tr = tl + lod;                // global top-left index
        int bl = (z + lod) * mapSize + x; // global bottom-left index
        int br = bl + lod;                // global bottom-right index

        int bl2 = (z + lod * 2) * mapSize + x; // global index bottom-left times 2
        int br2 = bl2 + lod;                   // global index bottom-right times 2

        if (z == oy && c_north != null && c_north.getLOD() > lod && !c_north.subdivided()) // seal northern cracks
        {
          if (x == ox && c_west != null && c_west.getLOD() > lod && !c_west.subdivided()) // north-western corner
          {
            indices[i++] = br;
            indices[i++] = br + lod;
            indices[i++] = tr + lod;

            indices[i++] = tl;
            indices[i++] = br;
            indices[i++] = tr + lod;

            indices[i++] = tl;
            indices[i++] = bl2;
            indices[i++] = br;

            indices[i++] = bl2;
            indices[i++] = br2;
            indices[i++] = br;
          }
          else if (x == size + ox - lod - lod && c_east != null && c_east.getLOD() > lod && !c_east.subdivided()) // north-eastern corner
          {
            indices[i++] = bl;
            indices[i++] = br;
            indices[i++] = tl;

            indices[i++] = tl;
            indices[i++] = br;
            indices[i++] = tr + lod;

            indices[i++] = tr + lod;
            indices[i++] = br;
            indices[i++] = br2 + lod;

            indices[i++] = br;
            indices[i++] = br2;
            indices[i++] = br2 + lod;
          }
          else
          {
            indices[i++] = tl;
            indices[i++] = bl;
            indices[i++] = br;

            indices[i++] = tl;
            indices[i++] = br;
            indices[i++] = tr + lod;

            indices[i++] = br;
            indices[i++] = br + lod;
            indices[i++] = tr + lod;
          }
          x += lod;
        }
        else if (x == size + ox - lod && c_east != null && c_east.getLOD() > lod && !c_east.subdivided()) // seal eastern cracks
        {
          if (z % (lod * 2) == 0)
          {
            if (z == oy + size - lod * 2 && (c_south != null && c_south.getLOD() > lod && !c_south.subdivided()))
            {
              // construct mesh for south-eastern crack
              indices[i++] = tl;
              indices[i++] = bl;
              indices[i++] = tr;

              indices[i++] = tr;
              indices[i++] = bl;
              indices[i++] = br2;

              indices[i++] = bl2 - lod;
              indices[i++] = br2;
              indices[i++] = bl;

              indices[i++] = bl;
              indices[i++] = bl - lod;
              indices[i++] = bl2 - lod;
            }
            else
            {
              indices[i++] = tr;
              indices[i++] = tl;
              indices[i++] = bl;

              indices[i++] = tr;
              indices[i++] = bl;
              indices[i++] = br2;

              indices[i++] = bl2;
              indices[i++] = br2;
              indices[i++] = bl;
            }
          }
        }
        else if (z == size + oy - lod && c_south != null && c_south.getLOD() > lod && !c_south.subdivided()) // seal southern cracks
        {
          if (x == ox && c_west != null && c_west.getLOD() > lod && !c_west.subdivided()) // south western corner
          {
            //continue;
          }
          else if (x == size + ox - lod - lod  && c_east != null && c_east.getLOD() > lod && !c_east.subdivided()) // south eastern corner
          {
            //continue;
          }
          else
          {
            indices[i++] = tl;
            indices[i++] = bl;
            indices[i++] = tr;

            indices[i++] = tr;
            indices[i++] = bl;
            indices[i++] = br + lod;

            indices[i++] = tr;
            indices[i++] = br + lod;
            indices[i++] = tr + lod;
          }
          x += lod;
        }
        else if (x == ox && c_west != null && c_west.getLOD() > lod && !c_west.subdivided()) // seal western cracks
        {
          if (z % (lod * 2) == 0)
          {
            if (z == oy + size - lod - lod && (c_south != null && c_south.getLOD() > lod && !c_south.subdivided()))
            {
              // construct mesh for south-western crack
              indices[i++] = tl;
              indices[i++] = br;
              indices[i++] = tr;

              indices[i++] = tl;
              indices[i++] = bl2;
              indices[i++] = br;

              indices[i++] = br;
              indices[i++] = bl2;
              indices[i++] = br2 + lod;

              indices[i++] = br;
              indices[i++] = br2 + lod;
              indices[i++] = br + lod;
            }
            else
            {
              indices[i++] = tl;
              indices[i++] = br;
              indices[i++] = tr;

              indices[i++] = tl;
              indices[i++] = bl2;
              indices[i++] = br;

              indices[i++] = bl2;
              indices[i++] = br2;
              indices[i++] = br;
            }
          }
        }
        else
        {
          // add sub-collection of indices to a buffer
          indices[i++] = tl;
          indices[i++] = bl;
          indices[i++] = br;
          indices[i++] = tl;
          indices[i++] = br;
          indices[i++] = tr;
        }
      }
    }

    return indices;
  }

  public static VertexArray generate(BufferedImage heightmap, int size)
  {
    VertexArray vao = new VertexArray();

    float[] vertices = new float[size * size * 3];
    int v = 0;
    for (int z = 0; z < size; z++)
    {
      for (int x = 0; x < size; x++)
      {
        // store three floats per vertex
        vertices[v++] = x;                                 // vector x component
        vertices[v++] = getHeight(heightmap, x, z) * 75.0f; // vector y component
        vertices[v++] = z;                                 // vector z component
      }
    }

    vao.addAttribute(0, 3, vertices);
    return vao;
  }
}
