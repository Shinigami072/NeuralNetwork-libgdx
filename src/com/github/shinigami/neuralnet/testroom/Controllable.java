package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Controllable {
	
	public Rectangle boundingbox;
	public Vector2 position;
	protected Rectangle movespace;

	
	public abstract void simulate(TestRoom t,float delta);
	public abstract void draw(TestRoom t,float x,float y,float delta);

}
