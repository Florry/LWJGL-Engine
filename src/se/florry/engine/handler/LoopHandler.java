package se.florry.engine.handler;

/*
 * Functional handler with deltaTime of frame as parameter
 */

public interface LoopHandler
{

	void loop(float deltaTime);

}