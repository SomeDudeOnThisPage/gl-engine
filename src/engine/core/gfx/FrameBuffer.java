package engine.core.gfx;

import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.opengl.GL32C.glFramebufferTexture;

public class FrameBuffer
{
  private int id;
  private int width;
  private int height;

  private Texture texture;
  private Texture depthTexture;
  private int depthBuffer;

  public static void terminate()
  {
    // todo: delete cached framebuffers
  }

  public Texture getTexture() { return this.texture; }
  public Texture getDepthTexture() { return this.depthTexture; }

  public void bind()
  {
    glBindFramebuffer(GL_FRAMEBUFFER, this.id);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glViewport(0, 0, this.width, this.height);
  }

  public void unbind()
  {
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
    this.texture.unbind();
    glViewport(0, 0, 1600, 900);
  }

  public void addTexture()
  {
    System.out.println(this.id);
    this.texture = new Texture(this.width, this.height, GL_RGBA);
    this.texture.bind();

    this.bind();
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getID(), 0);
    this.unbind();

    this.texture.unbind();
  }

  public void addDepthTexture()
  {
    this.bind();

    this.depthTexture = new Texture(this.width, this.height, GL_DEPTH_COMPONENT);
    this.depthTexture.bind();

    glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, this.depthTexture.getID(), 0);
    this.unbind();

    this.depthTexture.unbind();
  }

  public void addDepthBuffer()
  {
    this.depthBuffer = glGenRenderbuffers();
    glBindRenderbuffer(GL_RENDERBUFFER, this.depthBuffer);
    glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, this.width, this.height);

    this.bind();
    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, this.depthBuffer);
    this.unbind();
  }

  public FrameBuffer(int width, int height)
  {
    this.id = glGenFramebuffers();
    this.width = width;
    this.height = height;

    this.addTexture();
    this.addDepthBuffer();
    this.addDepthTexture();
  }
}
