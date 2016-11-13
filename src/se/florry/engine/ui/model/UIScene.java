package se.florry.engine.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.input.Input;

/*
 * A set of UIComponents being rendered together creating a scene. 
 * This is used when displaying the actual UI to the screen. 
 * E.g. an ui scene could be the GUI of a game with UIComponents being a health meter, stamina meter and score.
 */
public abstract class UIScene
{

	private final List<UIComponent> uiComponents;
	public final Vector2f position;
	private final String id;

	private boolean initialised;

	public UIScene()
	{
		this.uiComponents = new ArrayList<>();
		this.position = new Vector2f();
		this.id = UUID.randomUUID()
				.toString();
	}

	public abstract void prepare();

	public void render()
	{
		if (!this.initialised)
		{
			this.initialised = true;
			this.prepare();
		}

		for (final UIComponent uiComponent : this.uiComponents)
		{
			if (uiComponent.shouldRender())
			{
				uiComponent.position.x += this.position.x;
				uiComponent.position.y += this.position.y;

				uiComponent.render();

				uiComponent.position.x -= this.position.x;
				uiComponent.position.y -= this.position.y;
			}
		}
	}

	public void addUIComponent(final UIComponent uiComponent)
	{
		this.uiComponents.add(uiComponent);
	}

	public void destroy()
	{
		this.removeInputs(this.uiComponents);
		this.uiComponents.clear();
		this.initialised = false;
	}

	private void removeInputs(final List<UIComponent> components)
	{
		Input input = null;

		for (final UIComponent component : components)
		{
			input = component.input;
			if (component.uiComponents.size() > 0)
			{
				this.removeInputs(component.uiComponents);
			}

			if (input != null)
			{
				input.remove(component.getId());
			}
		}
		try
		{
			input.remove(this.getId());
		} catch (final Exception e)
		{
		}
	}

	public String getId()
	{
		return this.id;
	}

	public void clear()
	{
		this.uiComponents.clear();
	}
}