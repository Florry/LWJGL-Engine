package se.florry.engine.sound;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.newdawn.slick.util.ResourceLoader;

import se.florry.engine.constants.Constants;

public final class Sound
{

	private final Map<String, Clip> soundEffects;
	private boolean isDisabled;

	public Sound()
	{
		this.soundEffects = new HashMap<>();
	}

	/*
	 * Loads a sound file and stores that data as a named sound effect.
	 */
	public void loadSound(final String filename, final String soundEffectName)
	{
		final String completeFilename = Constants.Sound.SOUND_LOCATION + filename + "." + Constants.Sound.SOUND_FILEFORMAT;

		try
		{
			final AudioInputStream audioIn = AudioSystem.getAudioInputStream(ResourceLoader.getResourceAsStream(completeFilename));
			final Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			this.soundEffects.put(soundEffectName, clip);
		} catch (final Exception e)
		{
		}
	}

	/*
	 * Plays a stored sound effect.
	 */
	public void playSound(final String soundEffectName)
	{
		if (!this.isDisabled)
		{
			if (this.soundEffects.containsKey(soundEffectName))
			{
				try
				{
					final Clip clip = this.soundEffects.get(soundEffectName);

					clip.stop();
					clip.setFramePosition(0);
					clip.start();
				} catch (final Exception e)
				{
				}
			}
		}
	}

	public void disableAllSounds()
	{
		this.isDisabled = true;
	}

}