package se.florry.engine.model;

import java.nio.IntBuffer;
import java.util.UUID;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

/*
 * Class for rendering a quad.
 */
public class QuadModel
{

	private final String id;
	public final Size size;
	public final Vector2f position;
	public final Vector2f renderPositionOffset;
	public final Vector2f textureCoordinates;
	public final Vector2f textureSize;
	public final Color color;

	private boolean shouldRender;
	private boolean hasTransparency;

	public QuadModel(final Size size, final int x, final int y, final float u, final float v, final float ux, final float vx)
	{
		this.id = UUID.randomUUID()
				.toString();
		this.size = size;
		this.position = new Vector2f(x, y);
		this.renderPositionOffset = new Vector2f();
		this.textureCoordinates = new Vector2f(u, v);
		this.textureSize = new Vector2f(ux, vx);
		this.color = new Color(255, 255, 255, 255);
		this.shouldRender = true;
	}

	public QuadModel(final Size size, final int x, final int y, final float u, final float v)
	{
		this(size, x, y, u, v, 1, 1);
	}

	public QuadModel(final Size size, final int x, final int y)
	{
		this(size, x, y, 0, 0, 1, 1);
	}

	public QuadModel(final Size size)
	{
		this(size, 0, 0, 0, 0, 1, 1);
	}

	public QuadModel(final int size)
	{
		this(new Size(size, size), 0, 0, 0, 0, 1, 1);
	}

	public void render()
	{
		try
		{
			if (this.shouldRender)
			{
				Color adjustedColor = new Color(this.color.r / 255, this.color.g / 255, this.color.b / 255);

				if (this.hasTransparency() || this.color.a < 1)
				{
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				}

				final IntBuffer fb = BufferUtils.createIntBuffer(4);
				GL11.glGetIntegerv(GL11.GL_CURRENT_COLOR, fb);

				GL11.glColor4f(adjustedColor.r, adjustedColor.g, adjustedColor.b, adjustedColor.a);

				GL11.glBegin(GL11.GL_QUADS);

				GL11.glTexCoord2f(0 + this.textureCoordinates.x, 0 + this.textureCoordinates.y);
				GL11.glVertex2f(this.position.x + this.renderPositionOffset.x, this.position.y + this.renderPositionOffset.y);

				GL11.glTexCoord2f(1 * this.textureSize.x + this.textureCoordinates.x, 0 + this.textureCoordinates.y);
				GL11.glVertex2f(this.position.x + this.renderPositionOffset.x + this.size.width, this.position.y + this.renderPositionOffset.y);

				GL11.glTexCoord2f(1 * this.textureSize.x + this.textureCoordinates.x, 1 * this.textureSize.y + this.textureCoordinates.y);
				GL11.glVertex2f(this.position.x + this.renderPositionOffset.x + this.size.width,
						this.position.y + this.renderPositionOffset.y + this.size.height);

				GL11.glTexCoord2f(0 + this.textureCoordinates.x, 1 * this.textureSize.y + this.textureCoordinates.y);
				GL11.glVertex2f(this.position.x + this.renderPositionOffset.x, this.position.y + this.renderPositionOffset.y + this.size.height);

				GL11.glEnd();

				if (this.hasTransparency() || adjustedColor.a < 255)
				{
					GL11.glDisable(GL11.GL_BLEND);
				}

				GL11.glColor4i(fb.get(0), fb.get(1), fb.get(2), fb.get(3));
			}
		} catch (final Exception e)
		{
		}
	}

	public boolean hasTransparency()
	{
		return this.hasTransparency;
	}

	public void setTransparency(final boolean transparency)
	{
		this.hasTransparency = transparency;
	}

	public String getId()
	{
		return this.id;
	}

	public void hide()
	{
		this.shouldRender = false;
	}

	public void show()
	{
		this.shouldRender = true;
	}

	public QuadModel setSize(final int width, final int height)
	{
		this.size.width = width;
		this.size.height = height;
		return this;
	}
}