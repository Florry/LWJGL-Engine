package se.florry.engine.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.input.Input;
import se.florry.engine.model.QuadModel;
import se.florry.engine.model.TextModel;

/*
 * A set of UI quads, text models or UIComponents being rendered together. 
 * A ui component can have its own position essentially grouping together all entities within it.
 */
public abstract class UIComponent
{

	private final List<QuadModel> uiQuads;
	private final List<TextModel> uiTexts;
	public final List<UIComponent> uiComponents;

	protected final Input input;
	public final Vector2f position;
	protected boolean initialised;
	private final String id;
	private boolean shouldRender;

	public UIComponent(final Input input)
	{
		this.uiQuads = new ArrayList<>();
		this.uiTexts = new ArrayList<>();
		this.uiComponents = new ArrayList<>();
		this.input = input;

		this.position = new Vector2f();
		this.id = UUID.randomUUID()
				.toString();
		this.shouldRender = true;
	}

	public abstract void prepare();

	protected abstract void update();

	protected abstract void initialised();

	/*
	 * Goes through and renders all UIComponent, QuadModels and TextModels with
	 * its own position applied to them.
	 */
	public void render()
	{
		if (!this.initialised)
		{
			this.prepare();
		}

		for (final UIComponent uiComponent : this.uiComponents)
		{
			uiComponent.position.x += this.position.x;
			uiComponent.position.y += this.position.y;

			uiComponent.render();

			uiComponent.position.x -= this.position.x;
			uiComponent.position.y -= this.position.y;
		}

		for (final QuadModel quad : this.uiQuads)
		{
			quad.position.x += this.position.x;
			quad.position.y += this.position.y;

			quad.render();

			quad.position.x -= this.position.x;
			quad.position.y -= this.position.y;
		}

		for (final TextModel text : this.uiTexts)
		{
			text.position.x += this.position.x;
			text.position.y += this.position.y;

			text.render();

			text.position.x -= this.position.x;
			text.position.y -= this.position.y;
		}

		if (!this.initialised)
		{
			this.initialised = true;
			this.initialised();
		}

		this.update();
	}

	/*
	 * Used to build the UIComponent.
	 */
	protected void addElement(final QuadModel quad)
	{
		this.uiQuads.add(quad);
	}

	/*
	 * Used to build the UIComponent.
	 */
	protected void addElement(final TextModel text)
	{
		this.uiTexts.add(text);
	}

	/*
	 * Used to build the UIComponent.
	 */
	protected void addElement(final UIComponent uiComponent)
	{
		this.uiComponents.add(uiComponent);
	}

	protected void remove(final QuadModel quad)
	{
		this.uiQuads.remove(quad);
	}

	protected void remove(final TextModel text)
	{
		this.uiTexts.remove(text);
	}

	protected void remove(final UIComponent uiComponent)
	{
		this.uiComponents.remove(uiComponent);
	}

	public String getId()
	{
		return this.id;
	}

	public void hide()
	{
		this.shouldRender = false;
	}

	public void show()
	{
		this.shouldRender = true;
	}

	public boolean shouldRender()
	{
		return this.shouldRender;
	}

	public boolean visible()
	{
		return this.shouldRender;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		} else if (obj instanceof UIComponent)
		{
			final UIComponent uiComponent = (UIComponent) obj;

			if (this.getId()
					.equals(uiComponent.getId()))
			{
				return true;
			}
		}
		return false;
	}
}