package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Detector {

	public int id;
	float detected;

	public Detector(int id) {
		detected=0f;
		this.id=id;
	}
	public abstract Float detect(TestRoom testRoom);
	public void draw(ShapeRenderer shr,Batch b,BitmapFont f, float x, float y, float width, float height,int detectorCount){
		float up = height/detectorCount;	
		f.setColor(1,1,1,1);
		f.draw(b, "D("+id+"): "+detected, x, up/2f+y+up*id+24f);
	};
	

}
