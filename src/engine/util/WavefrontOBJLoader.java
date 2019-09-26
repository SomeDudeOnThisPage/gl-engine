package engine.util;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import engine.core.gfx.VertexArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WavefrontOBJLoader
{
  public static VertexArray load(String path)
  {
    VertexArray vao = new VertexArray();
    File file = new File("resources/models/" + path + ".obj");

    try
    {
      Obj obj = ObjUtils.convertToRenderable(ObjReader.read(new BufferedReader(new FileReader(file))));
      vao.addAttribute(0, 3, ObjData.getVerticesArray(obj));
      vao.addAttribute(1, 2, ObjData.getTexCoordsArray(obj, 2, true));
      vao.addAttribute(2, 3, ObjData.getNormalsArray(obj));
      vao.addIndices(ObjData.getFaceVertexIndicesArray(obj));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return vao;
  }
}
