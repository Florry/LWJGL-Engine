package se.florry.engine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.constants.Constants;
import se.florry.engine.material.Material;

/*
 * Class for rendering text.
 */
public final class TextModel
{

	private List<TextQuadModel> sentenceQuads;
	private String sentence;
	private final int size;
	public final Vector2f position;
	private final Color color;
	private final Material material;
	private final boolean useShadows;

	private final int letterSpacing;

	private final String id;

	public TextModel(final String sentence, final int size, final Vector2f position, final Color color, final boolean shadows, final int letterSpacing)
	{
		this.sentence = sentence.toUpperCase();
		this.size = size;
		this.position = position;
		this.color = color;
		this.sentenceQuads = new ArrayList<>();
		this.material = new Material("letters");
		this.useShadows = shadows;
		this.letterSpacing = letterSpacing;

		this.id = UUID.randomUUID()
				.toString();

		this.convertToQuadList();
	}

	public TextModel(final String sentence, final int size, final Vector2f position, final Color color, final boolean shadows)
	{
		this(sentence, size, position, color, shadows, Constants.UI.Text.LETTER_SPACING);
	}

	public TextModel(final String sentence, final int size, final Vector2f position, final Color color)
	{
		this(sentence, size, position, color, true);
	}

	public TextModel(final String sentence, final int size, final Vector2f position)
	{
		this(sentence, size, position, new Color(255, 255, 255, 255), true);
	}

	public TextModel(final String sentence, final int size, final Vector2f position, final boolean useShadows)
	{
		this(sentence, size, position, new Color(255, 255, 255, 255), useShadows);
	}

	public void setSentence(final String sentence)
	{
		this.sentence = sentence.toUpperCase();
		this.sentenceQuads = new ArrayList<>();
		this.convertToQuadList();
	}

	/*
	 * Converts the inputted sentence string to a list of quads.
	 */
	private void convertToQuadList()
	{
		for (int i = 0; i < this.sentence.length(); i++)
		{
			final float offset = this.getTextureOffsetForChar(this.sentence.charAt(i));

			if (this.useShadows)
			{
				this.addQuadToList(this.letterSpacing * i + i * this.size + 3, 0 + 3, offset, new Color(0, 0, 0, 255));
			}
			this.addQuadToList(this.letterSpacing * i + i * this.size, 0, offset, this.color);
		}
	}

	/*
	 * Adds a quad to the list of quads. Sets the texture coordinates according
	 * to which character it is.
	 */
	private void addQuadToList(final float x, final float y, final float textureOffset, final Color color)
	{
		final TextQuadModel letterQuad = new TextQuadModel(new Size(this.size, this.size), color);

		letterQuad.position.x = x;
		letterQuad.position.y = y;

		letterQuad.textureSize.x = 0.025f;
		letterQuad.textureSize.y = 1;

		letterQuad.textureCoordinates.x = 1f / Constants.UI.Text.LETTERS.length * textureOffset;
		letterQuad.setTransparency(true);

		this.sentenceQuads.add(letterQuad);
	}

	/*
	 * Returns the index of a character in the char array constant.
	 */
	private float getTextureOffsetForChar(final char charAt)
	{
		int i = 0;

		for (i = 0; i < Constants.UI.Text.LETTERS.length; i++)
		{
			if (Constants.UI.Text.LETTERS[i] == charAt)
			{
				break;
			}
		}

		return i;
	}

	public String getSentence()
	{
		return this.sentence;
	}

	/*
	 * Goes through the whole list of quads and renders one at a time.
	 */
	public void render()
	{
		this.material.setMaterial();

		try
		{
			for (final TextQuadModel letter : this.sentenceQuads)
			{
				letter.position.x += this.position.x;
				letter.position.y += this.position.y;

				letter.render();

				letter.position.x -= this.position.x;
				letter.position.y -= this.position.y;
			}
		} catch (final Exception e)
		{
			System.err.println(e.getMessage());
		}

		this.material.disable();
	}

	public int length()
	{
		return this.sentenceQuads.size();
	}

	public int getSize()
	{
		return this.size;
	}

	public String getId()
	{
		return this.id;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		} else if (obj instanceof TextModel)
		{
			return ((TextModel) obj).getId()
					.equals(this.id);
		}
		return false;
	}

}