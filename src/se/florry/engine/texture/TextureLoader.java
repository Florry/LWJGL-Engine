package se.florry.engine.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;

import se.florry.engine.constants.Constants;

public final class TextureLoader
{

	/*
	 * Reads a texture from file and loads it in the Open GL context, returning
	 * the id of that texture within the context.
	 */
	public static int readTexture(String filename)
	{
		filename = Constants.Textures.TEXTURE_LOCATION + filename + "." + Constants.Textures.TEXTURE_FILEFORMAT;
		final IntBuffer tmp = BufferUtils.createIntBuffer(1);

		try
		{
			GL11.glGenTextures(tmp);
		} catch (final Exception e)
		{
			System.err.println("Init engine before loading textures!");
		}
		tmp.rewind();

		try
		{
			final InputStream in = new FileInputStream(filename);
			final PNGDecoder decoder = new PNGDecoder(in);

			final ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.RGBA);
			buf.flip();

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);

		} catch (final java.io.FileNotFoundException ex)
		{
			System.out.println("Error " + filename + " not found");
		} catch (final java.io.IOException e)
		{
			System.out.println("Error decoding " + filename);
		}

		tmp.rewind();

		return tmp.get(0);
	}

	/*
	 * Reads a texture and converts it to a BufferedImage. Typically used when
	 * processing images from the JAVA code.
	 */
	public static BufferedImage readTextureToBufferedImage(String filename)
	{
		filename = Constants.Textures.TEXTURE_LOCATION + filename + "." + Constants.Textures.TEXTURE_FILEFORMAT;
		try
		{
			final File file = new File(filename);
			final BufferedImage img = ImageIO.read(file);

			return img;

		} catch (final Exception e)
		{
			System.out.println("Error reading " + filename);
		}

		return null;
	}

}
