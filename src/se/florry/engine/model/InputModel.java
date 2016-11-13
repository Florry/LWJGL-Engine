package se.florry.engine.model;

import java.util.UUID;

import se.florry.engine.handler.InputHandler;

/*
 * Model for saving an input.
 */
public final class InputModel
{

	private final int key;
	private final boolean isKeyUp;
	private final InputHandler inputHandler;
	private final boolean isApplicationKey;
	private final Object contextRefernce;

	/*
	 * @param key the key int from GLFW.GLFW_KEY_*
	 * 
	 * @param isKeyUp if the input is triggered when key is released.
	 * 
	 * @param inputHandler the handler with the code to be executed when the key
	 * is pressed or released.
	 * 
	 * @param isApplicationKey if input should ignore the paused state.
	 * 
	 * @param contextReference id or other identifier to be able to link several
	 * inputs together. Used in GUIs where one screen has several inputs with
	 * the same contextReference. Removing that context when changing GUI screen
	 * will unbind all inputs related to the screen.
	 */
	public InputModel(final int key, final boolean isKeyUp, final InputHandler inputHandler, final boolean isApplicationKey, final Object contextRefernce)
	{
		this.key = key;
		this.isKeyUp = isKeyUp;
		this.inputHandler = inputHandler;
		this.isApplicationKey = isApplicationKey;

		if (contextRefernce == null)
		{
			this.contextRefernce = UUID.randomUUID()
					.toString();
		} else
		{
			this.contextRefernce = contextRefernce;
		}
	}

	public InputModel(final int key, final boolean isKeyUp, final InputHandler inputHandler, final boolean isApplicationKey)
	{
		this(key, isKeyUp, inputHandler, isApplicationKey, UUID.randomUUID()
				.toString());
	}

	public int getKey()
	{
		return this.key;
	}

	public boolean isKeyUp()
	{
		return this.isKeyUp;
	}

	public InputHandler getInputHandler()
	{
		return this.inputHandler;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		} else if (obj instanceof InputModel)
		{
			final InputModel otherInput = (InputModel) obj;

			if (otherInput != null && this.key == otherInput.getKey() && this.isKeyUp() == otherInput.isKeyUp() && this.getReference()
					.equals(otherInput.getReference()))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 1871;

		hash *= this.key;
		hash *= this.isKeyUp ? 37 : 23;
		hash *= this.inputHandler.hashCode();

		return hash;
	}

	public boolean isApplicationKey()
	{
		return this.isApplicationKey;
	}

	public boolean contextIsActive()
	{
		return this.contextRefernce != null;
	}

	public Object getReference()
	{
		return this.contextRefernce;
	}

}
