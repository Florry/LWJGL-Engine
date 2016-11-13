package se.florry.engine.trigger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import se.florry.engine.handler.TriggerHandler;
import se.florry.engine.model.TriggerModel;
import se.florry.engine.timer.Timeout;
import se.florry.engine.utils.EngineUtils;

/*
 * Class for managing triggers. A trigger executes a handler with code continuously at a steady framerate.
 */
public final class Trigger
{

	private final List<TriggerModel> triggers;

	public Trigger()
	{
		this.triggers = Collections.synchronizedList(new ArrayList<>());
	}

	/*
	 * @param name the name of the trigger, used for removing the trigger at a
	 * later stage.
	 * 
	 * @param framerate how many times the trigger should execute the code per
	 * second.
	 * 
	 * @param handler the handler with the code to be executed by the trigger.
	 */
	public Trigger add(final String name, final int framerate, final TriggerHandler handler)
	{
		final TriggerModel trigger = new TriggerModel(framerate, name, handler);
		this.triggers.add(trigger);

		return this;
	}

	public Trigger add(final String name, final int framerate, final TriggerHandler handler, final boolean shouldOnlyTriggerOnce)
	{
		final TriggerModel trigger = new TriggerModel(framerate, name, handler, shouldOnlyTriggerOnce);
		this.triggers.add(trigger);

		return this;
	}

	/*
	 * Removes a trigger by the name it was given when created.
	 */
	public Trigger remove(final String name)
	{
		try
		{
			for (int i = 0; i < this.triggers.size(); i++)
			{
				if (i < this.triggers.size() && name.equals(this.triggers.get(i)
						.getName()))
				{
					final TriggerModel trigger = this.triggers.get(i);

					if (trigger != null && trigger.getTimerObject() != null)
					{
						trigger.getTimerObject()
								.cancel();
					}

					if (this.triggers.size() > i && this.triggers.get(i) != null)
					{
						this.triggers.remove(i);
					}
					break;
				}
			}
		} catch (final Exception e)
		{
			System.out.println("Tried to remove trigger but couldn't");
		}

		return this;
	}

	private void removeAll()
	{
		for (int i = 0; i < this.triggers.size(); i++)
		{
			final TimerTask timer = this.triggers.get(i)
					.getTimerObject();

			if (timer != null)
			{
				timer.cancel();
			}
		}

		this.triggers.clear();
	}

	public boolean contains(final String name)
	{
		final List<Object> currentTriggers = Arrays.asList(this.triggers.toArray());
		for (final Object triggerObj : currentTriggers)
		{
			final TriggerModel trigger = (TriggerModel) triggerObj;

			if (trigger.getName()
					.equals(name))
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * Handles all triggers. Loops through each trigger model and determines if
	 * they should be triggered at the current time or not.
	 */
	public void handle(final int currentFrame)
	{
		try
		{
			final int currentTime = (int) (EngineUtils.getTime() - EngineUtils.getStartTime());

			for (final TriggerModel trigger : this.triggers)
			{
				if (trigger != null)
				{
					if (currentTime >= trigger.getNextTimeToTrigger())
					{
						trigger.setNextTimeToTrigger(currentTime + 1000 / trigger.getFramerate());
						trigger.setTimerObject(Timeout.set(trigger.getHandler(), 1000 / trigger.getFramerate()));

						if (trigger.shouldOnlyTriggerOnce())
						{
							this.remove(trigger.getName());
						}
					}
				}
			}
		} catch (final Exception e)
		{
		}
	}

	public void clean()
	{
		Timeout.getTimer()
				.cancel();
		this.removeAll();
	}

}
