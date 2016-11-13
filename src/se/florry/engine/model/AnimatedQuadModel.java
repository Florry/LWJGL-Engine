package se.florry.engine.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import se.florry.engine.utils.EngineUtils;

/*
 * Class for rendering a quad with an animated texture.
 */
public class AnimatedQuadModel extends QuadModel
{

	public final Vector2f frameDimensions;
	private int totalFrames;

	private float animationRate;
	private int currentFrame;
	private int animationStart = 0;
	private int timePassed = 0;
	private boolean hasStarted;
	private boolean isPlaying;

	public int animationDirection = 1;

	public AnimatedQuadModel(final Size size, final int x, final int y, final int animationRate, final int totalFrames)
	{
		super(size, x, y);

		this.animationRate = 1000 / animationRate;
		this.totalFrames = totalFrames;
		this.frameDimensions = new Vector2f(1 / (float) this.totalFrames, 1);
	}

	/*
	 * Processes the frames of the animation before doing the actual rendering
	 * of the quad.
	 */
	@Override
	public void render()
	{
		this.processAnimation();
		this.renderAnimatedQuad();
	}

	/*
	 * Looks at which frame to be displayed on the quad based on the animation
	 * framerate, number of frames and the current time.
	 */
	private void processAnimation()
	{
		if (this.isPlaying)
		{
			if (!this.hasStarted)
			{
				this.animationStart = (int) (EngineUtils.getTime() - EngineUtils.getStartTime());
				this.hasStarted = true;
			}

			final int currentTime = (int) (EngineUtils.getTime() - EngineUtils.getStartTime());
			this.timePassed = currentTime - this.animationStart;
			this.currentFrame = (int) (this.timePassed / this.animationRate % this.totalFrames);

			if (this.currentFrame >= this.totalFrames)
			{
				this.hasStarted = false;
			}
		}
	}

	/*
	 * Renders the quad. Basically the same as a normal quad but with the
	 * offsets for displaying different frames of the animation.
	 */
	private void renderAnimatedQuad()
	{
		if (this.hasTransparency())
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}

		Color adjustedColor = new Color(this.color.r / 255, this.color.g / 255, this.color.b / 255);
		GL11.glColor4f(adjustedColor.r, adjustedColor.g, adjustedColor.b, adjustedColor.a);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(this.textureSize.x * (0 + this.getCurrentAnimationOffset()), 0 + this.textureCoordinates.y);
		GL11.glVertex2f(this.position.x + this.renderPositionOffset.x, this.position.y + this.renderPositionOffset.y);

		GL11.glTexCoord2f(this.textureSize.x * (this.getFrameDimensions().x + this.getCurrentAnimationOffset()), 0 + this.textureCoordinates.y);
		GL11.glVertex2f(this.position.x + this.renderPositionOffset.x + this.size.width, this.position.y + this.renderPositionOffset.y);

		GL11.glTexCoord2f(this.textureSize.x * (this.getFrameDimensions().x + this.getCurrentAnimationOffset()),
				1 * this.textureSize.y + this.textureCoordinates.y);
		GL11.glVertex2f(this.position.x + this.renderPositionOffset.x + this.size.width, this.position.y + this.renderPositionOffset.y + +this.size.height);

		GL11.glTexCoord2f(this.textureSize.x * (0 + this.getCurrentAnimationOffset()), 1 * this.textureSize.y + this.textureCoordinates.y);
		GL11.glVertex2f(this.position.x + this.renderPositionOffset.x, this.position.y + this.renderPositionOffset.y + +this.size.height);

		GL11.glEnd();

		if (this.hasTransparency())
		{
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	/*
	 * Returns the offset of the animation frame in UV space (A value between 0
	 * and 1).
	 */
	private float getCurrentAnimationOffset()
	{
		float offset;

		if (this.animationDirection == 1)
		{
			offset = this.currentFrame * this.getFrameDimensions().x;
		} else
		{
			offset = 1 - this.currentFrame * this.getFrameDimensions().x;
		}

		return offset;
	}

	/*
	 * Pauses the processing of the animation.
	 */
	public void pauseAnimation()
	{
		this.isPlaying = false;
	}

	/*
	 * Resumes the processing of the animation.
	 */
	public void resumeAnimation()
	{
		this.isPlaying = true;
	}

	/*
	 * Starts the animation at a specified frame. Is required to be called for
	 * the animation to begin animating at all.
	 */
	public void startAnimation(final int frame)
	{
		this.currentFrame = frame;
		this.isPlaying = true;
	}

	/*
	 * Starts the animation. Is required to be called for the animation to begin
	 * animating at all.
	 */
	public void startAnimation()
	{
		this.isPlaying = true;
	}

	/*
	 * Jumps to a specific frame in the animation.
	 */
	public void setCurrentFrame(final int frame)
	{
		this.currentFrame = frame;
	}

	/*
	 * Sets the rate which the animation plays at.
	 */
	public void setAnimationRate(final float animationRate)
	{
		this.animationRate = 1000 / animationRate;
	}

	/*
	 * Sets the total of frames the texture has.
	 */
	public void setTotalFrames(final int totalFrames)
	{
		this.totalFrames = totalFrames;
		this.getFrameDimensions().x = 1 / (float) this.totalFrames;
	}

	public Vector2f getFrameDimensions()
	{
		return frameDimensions;
	}

}
