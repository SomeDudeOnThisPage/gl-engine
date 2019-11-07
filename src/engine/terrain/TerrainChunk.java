package engine.terrain;

import engine.core.gfx.VertexArray;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class TerrainChunk
{
  public static final int CHUNK_SIZE = 32 + 1;

  private int lod;
  private Vector2i positionWorld;
  private Vector2i position;

  private Vector3f color;

  private Terrain terrain;

  private VertexArray vao;

  public VertexArray getVAO()
  {
    return this.vao;
  }
  public Vector3f getColor() { return this.color; }

  public Vector2i getPosition()
  {
    return this.position;
  }

  public Vector2i getPositionWorldCentered()
  {
    return new Vector2i(this.positionWorld.x + TerrainChunk.CHUNK_SIZE / 2, this.positionWorld.y + TerrainChunk.CHUNK_SIZE / 2);
  }

  public void setLOD(int lod, int[] neighbours)
  {
    this.lod = lod;
    this.vao.setIndices(TerrainChunkGenerator.generateIndices(this.lod, TerrainChunk.CHUNK_SIZE, this.terrain.getSize().x * TerrainChunk.CHUNK_SIZE, neighbours));
  }

  public int getLOD()
  {
    return this.lod;
  }

  public TerrainChunk(int x, int y, Heightfield heightfield, Terrain terrain)
  {
    this.position = new Vector2i(x, y);
    this.positionWorld = new Vector2i(x * (CHUNK_SIZE - 1), y * (CHUNK_SIZE - 1));
    this.lod = 1;
    this.terrain = terrain;

    this.vao = new VertexArray();
    this.vao.addAttribute(0, 3, TerrainChunkGenerator.generateVertices(heightfield, this.positionWorld, new Vector2i(TerrainChunk.CHUNK_SIZE)));
    this.color = new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random());
  }
}
