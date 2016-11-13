package se.florry.engine.model;

import java.util.TimerTask;

import se.florry.engine.handler.TriggerHandler;

/*
 * Class for holding information about a trigger.
 */
public final class TriggerModel
{

	private final int framerate;
	private final String name;
	private final TriggerHandler handler;
	private final boolean shouldOnlyTriggerOnce;

	public int nextTimeToTrigger;
	private TimerTask timerObject;

	public TriggerModel(final int framerate, final String name, final TriggerHandler handler, final boolean shouldOnlyTriggerOnce)
	{
		this.framerate = framerate > 0 ? framerate : 1;
		this.name = name;
		this.handler = handler;
		this.shouldOnlyTriggerOnce = shouldOnlyTriggerOnce;
	}

	public TriggerModel(final int framerate, final String name, final TriggerHandler handler)
	{
		this(framerate, name, handler, false);
	}

	public int getFramerate()
	{
		return this.framerate;
	}

	public String getName()
	{
		return this.name;
	}

	public TriggerHandler getHandler()
	{
		return this.handler;
	}

	public int getNextTimeToTrigger()
	{
		return this.nextTimeToTrigger;
	}

	public void setNextTimeToTrigger(final int nextTimeToTrigger)
	{
		this.nextTimeToTrigger = nextTimeToTrigger;
	}

	public TimerTask getTimerObject()
	{
		return this.timerObject;
	}

	public void setTimerObject(final TimerTask timerObject)
	{
		this.timerObject = timerObject;
	}

	public boolean shouldOnlyTriggerOnce()
	{
		return this.shouldOnlyTriggerOnce;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		} else if (obj instanceof TriggerModel)
		{
			final TriggerModel other = (TriggerModel) obj;

			return other.getName()
					.equals(this.getName());
		}

		return false;
	}

}