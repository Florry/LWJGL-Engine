package se.florry.engine.timer;

import java.util.Timer;
import java.util.TimerTask;

import se.florry.engine.handler.TriggerHandler;

/*
 * Simple timeout functionality. Executes inputted handler code after x miliseconds.
 */
public final class Timeout
{

	private static final Timer timer = new Timer();

	public static TimerTask set(final TriggerHandler handler, final long delay)
	{
		final TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				if (!this.cancel())
				{
					handler.handle(true);
				}
			}
		};
		try
		{
			timer.schedule(task, delay);
		} catch (final Exception e)
		{

		}
		return task;
	}

	public static Timer getTimer()
	{
		return timer;
	}

}
