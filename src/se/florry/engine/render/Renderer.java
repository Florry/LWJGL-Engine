package se.florry.engine.render;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import se.florry.engine.constants.Constants;
import se.florry.engine.model.Color;

/*
 * The engine renderer. Sets up all initial Open GL and GLFW related stuff. Takes care of clearing and rendering to the display.
 */
public final class Renderer
{

	private final Color backgroundColor;

	private int fbo;
	private int rbo;

	public Renderer()
	{
		this.backgroundColor = new Color(Constants.Display.BACKGROUND_COLOR.x,
				Constants.Display.BACKGROUND_COLOR.y,
				Constants.Display.BACKGROUND_COLOR.z,
				Constants.Display.BACKGROUND_COLOR.w);
	}

	public void init()
	{
		GL.createCapabilities();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GL11.glOrtho(0, Constants.Display.WIDTH, Constants.Display.HEIGHT, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, Constants.Display.WIDTH, Constants.Display.HEIGHT);

		glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);

		try
		{
			this.fbo = GL30.glGenFramebuffers();
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.fbo);

			this.rbo = GL30.glGenRenderbuffers();
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.rbo);
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_RGBA8, Constants.Display.WIDTH, Constants.Display.HEIGHT);
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, this.rbo);

			assert GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE;

		} catch (final Exception e)
		{
		}
	}

	public void prepare()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		try
		{
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, this.fbo);
			GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		} catch (final Exception e)
		{
		}
	}

	public void render(final long window)
	{
		try
		{
			GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.fbo);
			GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
			GL20.glDrawBuffers(GL11.GL_BACK_LEFT);

			GL30.glBlitFramebuffer(0,
					0,
					Constants.Display.WIDTH,
					Constants.Display.HEIGHT,
					0,
					0,
					Constants.Display.WIDTH,
					Constants.Display.HEIGHT,
					GL11.GL_COLOR_BUFFER_BIT,
					GL11.GL_NEAREST);

			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		} catch (final Exception e)
		{
		}

		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public void setBackgroundColor(Color color)
	{
		this.backgroundColor.r = color.r / 255;
		this.backgroundColor.g = color.g / 255;
		this.backgroundColor.b = color.b / 255;
		this.backgroundColor.a = color.a / 255;
	}

}
