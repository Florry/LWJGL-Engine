package se.florry.engine.material;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import se.florry.engine.texture.TextureLoader;

/*
 * Class for rendering a texture to one or more quads.
 */
public final class Material
{

	private final static Map<Integer, String> loadedTextures = new HashMap<>();
	private int texture;

	/*
	 * Looks at the static loadedTexture map to see if texture has already been
	 * loaded, otherwise it is loaded and saved in the map.
	 */
	public Material(final String textureName)
	{
		try
		{
			boolean textureIsLoaded = false;

			for (final Integer textureId : loadedTextures.keySet())
			{
				final String loadedTexture = loadedTextures.get(textureId);

				if (loadedTexture.equals(textureName))
				{
					textureIsLoaded = true;
					this.texture = textureId;

					break;
				}
			}

			if (!textureIsLoaded)
			{
				this.loadTexture(textureName);
			}
		} catch (final Exception e)
		{
		}
	}

	private void loadTexture(final String textureName)
	{
		this.texture = TextureLoader.readTexture(textureName);
		loadedTextures.put(this.texture, textureName);
	}

	public int getTexture()
	{
		return this.texture;
	}

	/*
	 * Used in the render class of any of the quads before drawing the actual
	 * mesh data of that quad. This sets the material to be rendered on all
	 * quads to this material.
	 */
	public void setMaterial()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.getTexture());
	}

	/*
	 * Removes the material from being the material to be rendered on all quads.
	 * This is typically done after rendering the mesh data of a quad in its
	 * render method.
	 */
	public void disable()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		} else if (obj instanceof Material)
		{
			final Material otherMat = (Material) obj;
			return otherMat.texture == this.texture;
		}

		return false;
	}

}
