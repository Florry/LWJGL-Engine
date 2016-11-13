package se.florry.engine.model;

/*
 * Quad model för letters when displaying text.
 */
public final class TextQuadModel extends QuadModel
{

	public TextQuadModel(final Size size, final Color color)
	{
		super(size);

		this.color.r = color.r;
		this.color.g = color.g;
		this.color.b = color.b;
		this.color.a = color.a;
	}

}