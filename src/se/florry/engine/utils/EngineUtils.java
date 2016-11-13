package se.florry.engine.utils;

import java.util.Arrays;
import java.util.Calendar;

/*
 * Some useful utils.
 */
public final class EngineUtils
{

	private static long startTime = getTime();
	private static Object[] lastLogged = null;

	public static long getTime()
	{
		return System.currentTimeMillis();
	}

	public static long getStartTime()
	{
		return startTime;
	}

	public static void log(final Object... logs)
	{
		if (logs.length == 1)
		{
			System.out.println(logs[0]);
		} else
		{
			final StringBuilder outputLog = new StringBuilder();
			for (final Object obj : logs)
			{
				outputLog.append(" ");
				outputLog.append(obj);
			}

			System.out.println(outputLog);
		}
	}

	public static void logWithTimestamp(final Object... logs)
	{
		logTimestamp();
		log(logs);
	}

	public static void logOnce(final Object... logs)
	{
		if (!Arrays.equals(lastLogged, logs))
		{
			lastLogged = logs;
			log(logs);
		}
	}

	public static void logOnceWithTimestamp(final Object... logs)
	{
		if (!Arrays.equals(lastLogged, logs))
		{
			lastLogged = logs;
			logTimestamp();
			log(logs);
		}
	}

	private static void logTimestamp()
	{
		log(Calendar.getInstance()
				.get(Calendar.HOUR_OF_DAY),
				":",
				Calendar.getInstance()
						.get(Calendar.MINUTE),
				":",
				Calendar.getInstance()
						.get(Calendar.SECOND),
				":",
				Calendar.getInstance()
						.get(Calendar.MILLISECOND));
	}

}