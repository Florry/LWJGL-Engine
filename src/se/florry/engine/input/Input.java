package se.florry.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import se.florry.engine.handler.InputHandler;
import se.florry.engine.model.InputModel;

/*
 * Class for handling all inputs for the engine.
 */
public final class Input
{

	private final Map<Integer, List<InputModel>> inputs;
	private final List<InputModel> activeInputs;
	private GLFWKeyCallback keyCallback;
	private float deltaTime;
	private boolean paused;

	public Input()
	{
		this.inputs = new HashMap<>();
		this.activeInputs = new ArrayList<>();
	}

	/*
	 * Makes an InputModel object of the inputted key and handler. Puts that in
	 * an array to be triggered later.
	 * 
	 * @param isApplicationKey should ignore the paused state > typically a
	 * application key.
	 * 
	 * @param contextReference id or other identifier to be able to link several
	 * inputs together. Used in GUIs where one screen has several inputs with
	 * the same contextReference. Removing that context when changing GUI screen
	 * will unbind all inputs related to the screen.
	 * 
	 */
	private Input add(final int inputKey, final boolean keyUp, final InputHandler inputHandler, final boolean isApplicationKey, final String contextReference)
	{
		final InputModel input = new InputModel(inputKey, keyUp, inputHandler, isApplicationKey, contextReference);

		if (!this.inputs.containsKey(inputKey))
		{
			this.inputs.put(inputKey, new ArrayList<>());
		}

		final List<InputModel> inputModels = this.inputs.get(inputKey);

		inputModels.add(input);

		return this;
	}

	/*
	 * Shortcut methods for the add method.
	 */
	public Input press(final int inputKey, final InputHandler inputHandler)
	{
		this.add(inputKey, false, inputHandler, false, null);
		return this;
	}

	public Input press(final int inputKey, final InputHandler inputHandler, final String contextReference)
	{
		this.add(inputKey, false, inputHandler, false, contextReference);
		return this;
	}

	public Input release(final int inputKey, final InputHandler inputHandler)
	{
		this.add(inputKey, true, inputHandler, false, null);
		return this;
	}

	public Input release(final int inputKey, final InputHandler inputHandler, final String contextReference)
	{
		this.add(inputKey, true, inputHandler, false, contextReference);
		return this;
	}

	public Input appPress(final int inputKey, final InputHandler inputHandler)
	{
		this.add(inputKey, false, inputHandler, true, null);
		return this;
	}

	public Input appPress(final int inputKey, final InputHandler inputHandler, final String contextReference)
	{
		this.add(inputKey, false, inputHandler, true, contextReference);
		return this;
	}

	public Input appRelease(final int inputKey, final InputHandler inputHandler)
	{
		this.add(inputKey, true, inputHandler, true, null);
		return this;
	}

	public Input appRelease(final int inputKey, final InputHandler inputHandler, final String contextReference)
	{
		this.add(inputKey, true, inputHandler, true, contextReference);
		return this;
	}

	/*
	 * Remove all inputs with a certain reference.
	 */
	public Input remove(final String reference)
	{
		for (final List<InputModel> inputModels : this.inputs.values())
		{
			final List<InputModel> inputsToRemove = new ArrayList<>();

			for (final InputModel inputModel : inputModels)
			{
				if (reference.equals(inputModel.getReference()))
				{
					inputsToRemove.add(inputModel);
				}
			}

			for (final InputModel inputModel : inputsToRemove)
			{
				inputModels.remove(inputModel);
				this.activeInputs.remove(inputModel);
			}

		}
		return this;
	}

	/*
	 * Inits the inputs with GLFW and sets up a callback to whenever an input is
	 * triggered. That input is then placed as active and triggered the next
	 * time the engine processes inputs. This makes it possible for multiple
	 * inputs to be triggered at the same time, different or same keys.
	 */
	public void init(final long window)
	{
		this.appPress(GLFW_KEY_ESCAPE, pressed ->
		{
			GLFW.glfwSetWindowShouldClose(window, GLFW.GLFW_TRUE);
		});

		GLFW.glfwSetKeyCallback(window, this.keyCallback = new GLFWKeyCallback()
		{
			@Override
			public void invoke(final long window, final int key, final int scancode, final int action, final int mods)
			{
				if (Input.this.inputs.containsKey(key))
				{
					final List<InputModel> inputsForKey = Input.this.inputs.get(key);
					for (final InputModel input : inputsForKey)
					{
						if (!Input.this.paused || input.isApplicationKey())
						{
							final boolean eventIsKeyUp = action == GLFW_RELEASE;

							if (!Input.this.activeInputs.contains(input))
							{
								if (!input.isKeyUp())
								{
									Input.this.activeInputs.add(input);
								}
							}

							if (eventIsKeyUp)
							{
								if (input.isKeyUp())
								{
									input.getInputHandler()
											.pressed(Input.this.deltaTime);
								} else
								{
									Input.this.activeInputs.remove(input);
								}
							}
						}
					}
				}
			}
		});
	}

	/*
	 * Triggers all inputs marked as active.
	 */
	public void triggerActiveInputs(final float deltaTime)
	{
		final List<InputModel> inputs = new ArrayList<>(this.activeInputs);
		this.deltaTime = deltaTime;

		for (final InputModel input : inputs)
		{
			input.getInputHandler()
					.pressed(this.deltaTime);
		}
	}

	public GLFWKeyCallback getKeyCallback()
	{
		return this.keyCallback;
	}

	/*
	 * Pauses all inputs not marked with isApplicationKey.
	 */
	public void pause()
	{
		this.paused = true;
		this.activeInputs.clear();
	}

	/*
	 * Resumes all inputs not marked with isApplicationKey.
	 */
	public void resume()
	{
		this.paused = false;
	}

}