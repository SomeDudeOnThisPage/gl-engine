package engine.terrain;

import org.joml.Vector2i;

public class TerrainChunkGenerator
{
  public static int[] generateIndices(int lod, int size, int map, int[] neighbors)
  {
    int[] indices = new int[(size + 1) * (size + 1) * 6];
    int n = 0;
    for (int i = 0; i < size - lod; i+=lod)
    {
      for (int j = 0; j < size - lod; j+=lod)
      {
        int tl = i * size + j;                               // global top-right index
        int tr = tl + lod;                                   // global top-left index
        int bl = (i + lod) * size + j;                       // global bottom-left index
        int br = bl + lod;                                   // global bottom-right index

        //int bl2 = (i + lod * 2) * size + j;                  // global index bottom-left times 2
        //int br2 = bl2 + lod;                                 // global index bottom-right times 2

        // fix northern cracks
        //if (i == 0 && neighbors[Terrain.DIRECTION_NORTH] > lod) // seal northern cracks
        //{
          /*if (j == 0 && neighbors[Terrain.DIRECTION_WEST] > lod) // north-western corner
          {
            System.out.println("North-West");
            j+=lod;
          }
          else if (j == size - lod * 2 - 1 && neighbors[Terrain.DIRECTION_EAST] > lod) // north-eastern corner
          {
            System.out.println("North-East");
            j+=lod;
          }
          else
          {
            indices[n++] = tl;
            indices[n++] = bl;
            indices[n++] = br;

            indices[n++] = tl;
            indices[n++] = br;
            indices[n++] = tr + lod;

            indices[n++] = br;
            indices[n++] = br + lod;
            indices[n++] = tr + lod;

            j+=lod;
          }
        }
        else if (i == size - lod - 1 && neighbors[Terrain.DIRECTION_SOUTH] > lod) // seal southern cracks
        {
          if (j == 0 && neighbors[Terrain.DIRECTION_WEST] > lod) // south-western corner
          {
            System.out.println("South-West");
            j+=lod;
          }
          else if (j == size - lod * 2 - 1 && neighbors[Terrain.DIRECTION_EAST] > lod) // south-eastern corner
          {
            System.out.println("South-East");
            j+=lod;
          }
          else
          {
          indices[n++] = tl;
          indices[n++] = tr;
          indices[n++] = bl;

          indices[n++] = bl;
          indices[n++] = tr;
          indices[n++] = br + lod;

          indices[n++] = tr;
          indices[n++] = br + lod;
          indices[n++] = tr + lod;

            j+=lod;
          }
        }
        else if (j == 0 && neighbors[Terrain.DIRECTION_WEST] > lod) // seal western cracks
        {
          if (i == 0 && neighbors[Terrain.DIRECTION_NORTH] > lod) {} // don't seal crack if we have a corner
          else if (i == size - lod * 2 - 1 && neighbors[Terrain.DIRECTION_SOUTH] > lod) {} // don't seal crack if we have a corner
          else
          {
            if (i % (lod * 2) == 0)
            {
              indices[n++] = tl;
              indices[n++] = br;
              indices[n++] = tr;

              indices[n++] = tl;
              indices[n++] = bl2;
              indices[n++] = br;

              indices[n++] = bl2;
              indices[n++] = br2;
              indices[n++] = br;
            }
          }
        }
        else if (j == size - lod - 1 && neighbors[Terrain.DIRECTION_EAST] > lod) // seal western cracks
        {
          if (i == 0 && neighbors[Terrain.DIRECTION_NORTH] > lod) {} // don't seal crack if we have a corner
          else if (i == size - lod * 2 - 1 && neighbors[Terrain.DIRECTION_SOUTH] > lod) {} // don't seal crack if we have a corner
          else
            {
            if (i % (lod * 2) == 0)
            {
              indices[n++] = tr;
              indices[n++] = tl;
              indices[n++] = bl;

              indices[n++] = tr;
              indices[n++] = bl;
              indices[n++] = br2;

              indices[n++] = bl2;
              indices[n++] = br2;
              indices[n++] = bl;
            }
          }
        }
        else
        {*/
          // add sub-collection of indices to a buffer
          indices[n++] = tl;
          indices[n++] = tr;
          indices[n++] = bl;
          indices[n++] = tr;
          indices[n++] = br;
          indices[n++] = bl;
        //}
      }
    }

    return indices;
  }

  public static float[] generateVertices(Heightfield heightfield, Vector2i position, Vector2i size)
  {
    float[] vertices = new float[size.x * size.y * 3];
    int v = 0;

    for (int x = position.x; x < position.x + size.x; x++)
    {
      for (int y = position.y; y < position.y + size.y; y++)
      {
        vertices[v++] = x;
        vertices[v++] = heightfield.getHeight(x, y) * 0.0f;// * (255.0f / 2.0f);
        vertices[v++] = y;
      }
    }

    return vertices;
  }

  public static float[] generateNormals()
  {
    // todo
    return null;
  }
}
