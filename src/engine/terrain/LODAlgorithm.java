package engine.terrain;

import org.joml.Vector3f;

public abstract class LODAlgorithm
{
  public abstract int getLOD(Vector3f camera, TerrainChunk chunk);
}
