package se.florry.engine.utils;

/*
 * Delta time and frame rate utils.
 */
public final class FrameUtils
{

	private static long lastFrame = EngineUtils.getTime();
	private static long lastFPS = 60;
	private static int fps = 60;
	private static int lastFramerate = 60;

	public static float getDeltaTime()
	{
		final long time = EngineUtils.getTime();
		final float delta = (float) (time - lastFrame) / 1000L;
		lastFrame = time;

		return delta;
	}

	public static int getFramerate()
	{
		if (EngineUtils.getTime() - lastFPS > 1000)
		{
			lastFramerate = fps;
			fps = 0;
			lastFPS = EngineUtils.getTime();
		}
		fps++;

		return lastFramerate;
	}

}