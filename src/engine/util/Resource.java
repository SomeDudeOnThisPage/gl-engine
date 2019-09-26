package engine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Resource
{
  public static ByteBuffer ioLoadResource(String resource) throws IOException
  {
    ByteBuffer buffer;
    File file = new File(resource);

    if (file.isFile())
    {
      FileInputStream input = new FileInputStream(file);
      FileChannel channel = input.getChannel();

      buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

      channel.close();
      input.close();

      return buffer;
    }

    throw new IOException("no such file");
  }
}
