package se.florry.engine.model;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import se.florry.engine.display.Display;
import se.florry.engine.handler.LoopHandler;
import se.florry.engine.input.Input;
import se.florry.engine.render.Renderer;
import se.florry.engine.sound.Sound;
import se.florry.engine.trigger.Trigger;
import se.florry.engine.utils.FrameUtils;

/*
 * The actual engine, taking care of processing all other components and displaying the rendered image to the display.
 */
public final class Engine
{

	private LoopHandler loopHandler;
	private final Display display;
	private final Renderer renderer;
	private final Input input;
	private final Trigger trigger;
	private final Sound sound;
	private float deltaTime;
	private int framerate;
	private int currentFrame;

	public Engine()
	{
		this.display = new Display("Engine");
		this.display.init();
		this.renderer = new Renderer();
		this.renderer.init();
		this.input = new Input();
		this.input.init(this.display.getWindow());
		this.trigger = new Trigger();
		this.sound = new Sound();
	}

	/*
	 * The engine loop.
	 * 
	 * @param loop The main game loop handler.
	 * 
	 * Processes triggers, inputs, renderer, main game loop and displays
	 * everything in the GLFW display.
	 */
	public void run(final LoopHandler loop)
	{
		this.loopHandler = loop;

		try
		{
			while (glfwWindowShouldClose(this.display.getWindow()) == GLFW_FALSE)
			{
				if (this.currentFrame >= 60)
				{
					this.currentFrame = 0;
				} else
				{
					this.currentFrame++;
				}
				this.getDeltaTimeForFrame();
				this.handleTriggers();
				this.input.triggerActiveInputs(this.deltaTime);
				this.renderer.prepare();
				this.loopHandler.loop(this.deltaTime);
				this.renderer.render(this.display.getWindow());

				this.getFramerateForFrame();
			}

			glfwDestroyWindow(this.display.getWindow());
			this.input.getKeyCallback()
					.release();
		} finally
		{
			this.trigger.clean();

			glfwTerminate();
			this.display.getErrorCallback()
					.release();
		}
	}

	/*
	 * Handles triggers.
	 */
	private void handleTriggers()
	{
		this.trigger.handle(this.currentFrame);
	}

	/*
	 * Returns the delta time for the current frame.
	 */
	private void getDeltaTimeForFrame()
	{
		this.deltaTime = FrameUtils.getDeltaTime();
	}

	/*
	 * Returns the frame rate for the current frame.
	 */
	private void getFramerateForFrame()
	{
		this.framerate = FrameUtils.getFramerate();
	}

	/*
	 * Sets the title of the display window.
	 */
	public void setTitle(final String title)
	{
		this.display.setTitle(title);
	}

	/*
	 * Sets the background color of the renderer. The parts of the display not
	 * being rendered on.
	 */
	public void setBackgroundColor(final Color color)
	{
		this.renderer.setBackgroundColor(color);
	}

	/*
	 * Returns the display to which the engine/game is rendered.
	 */
	public Display getDisplay()
	{
		return this.display;
	}

	/*
	 * Returns the input.
	 */
	public Input input()
	{
		return this.input;
	}

	/*
	 * Returns the trigger.
	 */
	public Trigger trigger()
	{
		return this.trigger;
	}

	/*
	 * Returns delta time for the current frame.
	 */
	public float getDeltaTime()
	{
		return this.deltaTime;
	}

	/*
	 * Returns frame rate for the current frame.
	 */
	public int getFramerate()
	{
		return this.framerate;
	}

	/*
	 * Returns the frame count of the current frame.
	 */
	public int getCurrentFrame()
	{
		return this.currentFrame;
	}

	/*
	 * Adds a sound effect to the engine.
	 * 
	 * @filename the file name of the actual sound file.
	 * 
	 * @param soundEffectName the name/identifier to be used when playing the
	 * sound.
	 */
	public void addSoundEffect(final String filename, final String soundEffectName)
	{
		this.sound.loadSound(filename, soundEffectName);
	}

	/*
	 * Plays the specified sound.
	 * 
	 * @param sound the name/identifier specified in the addSoundEffect method.
	 */
	public void playSound(final String sound)
	{
		this.sound.playSound(sound);
	}

	/*
	 * Mutes all sounds.
	 */
	public void disableAllSounds()
	{
		this.sound.disableAllSounds();
	}

}
