package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Collideable {
	public Rectangle getBoundingBox();
	public void onCollision(Collideable a);
	public Vector2 getVelocity();

}
