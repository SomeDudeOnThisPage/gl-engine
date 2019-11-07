package engine.terrain;

import org.joml.Vector2i;
import org.joml.Vector3f;

public class ExpLOD extends LODAlgorithm
{
  private int max;
  private int min;

  @Override
  public int getLOD(Vector3f camera, TerrainChunk chunk)
  {
    // distance of chunk to camera
    Vector2i v2_chunkPosition = chunk.getPositionWorldCentered();
    Vector3f chunkPosition = new Vector3f(-v2_chunkPosition.x, 0.0f, -v2_chunkPosition.y);

    float distance = (float) (camera.distance(chunkPosition) / Math.PI * 25.0f);

    for (int i = this.max; i > this.min; i /= 2)
    {
      if (distance > TerrainChunk.CHUNK_SIZE * (i - 1))
      {
        return i;
      }
    }

    return 1;
  }

  public ExpLOD(int min, int max)
  {
    this.max = max;
    this.min = min;
  }
}
