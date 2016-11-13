package se.florry.engine.ui.model;

import se.florry.engine.material.Material;
import se.florry.engine.model.AnimatedQuadModel;
import se.florry.engine.model.Size;

/*
 * An extension of the AnimatedQuadModel used for rendering animated quads to the UI.
 */
public final class AnimatedUIQuad extends AnimatedQuadModel
{

	private final Material material;

	public AnimatedUIQuad(final Size size, final int x, final int y, final int animationRate, final int totalFrames, final String texture)
	{
		super(size, x, y, animationRate, totalFrames);
		this.material = new Material(texture);
	}

	@Override
	public void render()
	{
		this.material.setMaterial();
		super.render();
		this.material.disable();
	}

}