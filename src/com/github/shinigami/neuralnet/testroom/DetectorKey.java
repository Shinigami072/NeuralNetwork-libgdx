package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DetectorKey extends Detector {
	int key;
	public DetectorKey(int id,int key) {
		super(id);
		this.key=key;
	}
	
	@Override
	public Float detect(TestRoom testRoom) {
		detected = Gdx.input.isKeyPressed(key)?1f:0f;
		return detected;
	}

	@Override
	public void draw(ShapeRenderer shr,Batch b,BitmapFont f, float x, float y, float width, float height,int detectorCount) {
		float up = height/detectorCount;	
		f.setColor(1f,1f,1f,1f);
		b.setColor(1f,1f,1f,1f);
		f.draw(b, "D("+id+":"+Keys.toString(key)+"): "+detected, x, up/2f+y+up*id+24f);
	}

}
