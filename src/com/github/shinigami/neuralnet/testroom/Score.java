package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Score implements Collideable {

	float score;
	TestRoom testroom;
	Vector2 v;
	Vector2 position;
	Rectangle boundingbox;
	ScoreManager smng;
	public Score(TestRoom testRoom, float score,ScoreManager scoremanager) {
		this.score=score;
		this.testroom=testRoom;
		v = new Vector2(0f,0f);
		
		position = new Vector2(MathUtils.random(testroom.width),MathUtils.random(testroom.height));
		boundingbox = new Rectangle(0f,0f,10f,10f);
		boundingbox.setCenter(position);
		smng  = scoremanager;
	}

	@Override
	public Rectangle getBoundingBox() {
		return boundingbox;
	}

	@Override
	public void onCollision(Collideable a) {
		if(smng.scoreable.contains(a)){
			testroom.score+=score;
			position.set(MathUtils.random(testroom.width),MathUtils.random(testroom.height));
			boundingbox.setCenter(position);
		}
	}

	@Override
	public Vector2 getVelocity() {
		return v;
	}

	public void draw() {
		testroom.shr.begin(ShapeType.Filled);
		testroom.shr.setColor(1f, 1f, 0f, 1f);
		testroom.shr.rect(boundingbox.x, boundingbox.y, boundingbox.width,boundingbox.height);
		testroom.shr.end();
	}

}
