package se.florry.engine.ui.model;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.material.Material;
import se.florry.engine.model.Color;
import se.florry.engine.model.QuadModel;
import se.florry.engine.model.Size;

/*
 * An extension of the QuadModel used for rendering quads to the UI.
 */
public final class UIQuad extends QuadModel
{

	private final Material material;

	public UIQuad(final Size size, final Vector2f position, final String texture)
	{
		super(size);

		this.material = new Material(texture);

		this.position.x = position.x;
		this.position.y = position.y;
	}

	public UIQuad(final Size size, final Vector2f position, final Color color)
	{
		super(size);
		this.material = null;

		this.position.x = position.x;
		this.position.y = position.y;

		this.color.r = color.r;
		this.color.g = color.g;
		this.color.b = color.b;
		this.color.a = color.a;
	}

	@Override
	public void render()
	{
		if (this.material != null)
		{
			this.material.setMaterial();
		}

		super.render();

		if (this.material != null)
		{
			this.material.disable();
		}
	}
}
