package engine.terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class TerrainChunk extends Node
{
  private int x, y;
  private int lod;
  private int size;
  private int mapSize;
  public Vector3f color;
  public int[] indices;

  private TerrainChunk[] neighbors;

  public int[] getIndices()
  {
    return this.indices;
  }

  /**
   * Finds a neighbor of greater or equal size in a given direction.
   * @param direction 0 = north, 1 = east, 2 = south, 3 = west
   * @return neighbor
   */
  public TerrainChunk findGreaterNeighbor(int direction)
  {
    // root
    if (this.getParent() == null) { return null; }

    // check parents' children
    TerrainChunk parent = (TerrainChunk) this.getParent();
    ArrayList<Node> parentChildren = (ArrayList<Node>) parent.getChildren();

    if (direction == 0) // north
    {
      // check if we're in the same quad
      if (parentChildren.get(2) == this) { return (TerrainChunk) parentChildren.get(0); }
      if (parentChildren.get(3) == this) { return (TerrainChunk) parentChildren.get(1); }

      // if not, go up one parent
      TerrainChunk chunk = parent.findGreaterNeighbor(direction);
      if (chunk == null || !chunk.subdivided()) { return chunk; }

      if (parentChildren.get(0) == this) { return (TerrainChunk) chunk.getChildren().get(2); }
      if (parentChildren.get(1) == this) { return (TerrainChunk) chunk.getChildren().get(3); }
    }
    else if (direction == 1) // east
    {
      // check if we're in the same quad
      if (parentChildren.get(0) == this) { return (TerrainChunk) parentChildren.get(1); }
      if (parentChildren.get(2) == this) { return (TerrainChunk) parentChildren.get(3); }

      // if not, go up one parent
      TerrainChunk chunk = parent.findGreaterNeighbor(direction);
      if (chunk == null || !chunk.subdivided()) { return chunk; }

      if (parentChildren.get(1) == this) { return (TerrainChunk) chunk.getChildren().get(0); }
      if (parentChildren.get(3) == this) { return (TerrainChunk) chunk.getChildren().get(2); }
    }
    else if (direction == 2) // south
    {
      // check if we're in the same quad
      if (parentChildren.get(0) == this) { return (TerrainChunk) parentChildren.get(2); }
      if (parentChildren.get(1) == this) { return (TerrainChunk) parentChildren.get(3); }

      // if not, go up one parent
      TerrainChunk chunk = parent.findGreaterNeighbor(direction);
      if (chunk == null || !chunk.subdivided()) { return chunk; }

      if (parentChildren.get(2) == this) { return (TerrainChunk) chunk.getChildren().get(0); }
      if (parentChildren.get(3) == this) { return (TerrainChunk) chunk.getChildren().get(1); }
    }
    else if (direction == 3) // west
    {
      // check if we're in the same quad
      if (parentChildren.get(1) == this) { return (TerrainChunk) parentChildren.get(0); }
      if (parentChildren.get(3) == this) { return (TerrainChunk) parentChildren.get(2); }

      // if not, go up one parent
      TerrainChunk chunk = parent.findGreaterNeighbor(direction);
      if (chunk == null || !chunk.subdivided()) { return chunk; }

      if (parentChildren.get(0) == this) { return (TerrainChunk) chunk.getChildren().get(1); }
      if (parentChildren.get(2) == this) { return (TerrainChunk) chunk.getChildren().get(3); }
    }

    return null;
  }

  public void generateIndices()
  {
    if (!this.subdivided())
    {
      this.indices = TerrainGenerator.generateIndices(this.mapSize, this.x, this.y, this.size, this.lod, this);
    }
    else
    {
      for (int i = 0; i < 4; i++)
      {
        ((TerrainChunk) this.getChildren().get(i)).generateIndices();
      }
    }
  }

  public void subdivide()
  {
    int half = (this.size) / 2;
    if (this.lod > 1 && this.getChildren().size() == 0)
    {
      // create four children chunks
      TerrainChunk nw = new TerrainChunk(this, this.mapSize, this.x, this.y, half, this.lod / 2);
      TerrainChunk ne = new TerrainChunk(this, this.mapSize, this.x + half, this.y, half, this.lod / 2);
      TerrainChunk sw = new TerrainChunk(this, this.mapSize, this.x, this.y + half, half, this.lod / 2);
      TerrainChunk se = new TerrainChunk(this, this.mapSize, this.x + half, this.y + half, half, this.lod / 2);

      this.add(nw);
      this.add(ne);
      this.add(sw);
      this.add(se);

      for (int i = 0; i < 4; i++)
      {
        TerrainChunk chunk = (TerrainChunk) this.getChildren().get(i);
        chunk.generateIndices();
        for (int j = 0; j < 4; j++)
        {
          TerrainChunk neighbor = chunk.findGreaterNeighbor(j);
          if (neighbor != null)
          {
            neighbor.generateIndices();
          }
        }
      }
    }
  }

  public void join()
  {
    this.getChildren().clear();
    for (int j = 0; j < 4; j++)
    {
      TerrainChunk neighbor = this.findGreaterNeighbor(j);
      if (neighbor != null)
      {
        neighbor.generateIndices();
      }
    }
  }

  public int getLOD()
  {
    return this.lod;
  }

  public int getX() { return this.x; }
  public int getY() { return this.y; }

  public Vector2f getCenter()
  {
    return new Vector2f(this.x + this.size / 2.0f, this.y + this.size / 2.0f);
  }

  public TerrainChunk(TerrainChunk parent, int mapSize, int x, int y, int size, int lod)
  {
    this.parent = parent;
    this.size = size;
    this.mapSize = mapSize;
    this.lod = lod;
    this.x = x;
    this.y = y;
    this.color = new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random());
  }
}
