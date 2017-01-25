package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Vector2Detector extends Detector {

	public Vector2 vec;
	public Vector2Detector(int id, Vector2 v) {
		super(id);
		this.vec=v;
	}

	@Override
	public Float detect(TestRoom testRoom) {
		float x = vec.x/vec.len();
		float y = vec.y/vec.len();
		if(Float.isNaN(x))
			x=0f;
		
		if(Float.isNaN(y))
			y=0f;
		testRoom.setInput(id, x);
		testRoom.setInput(id+1, y);
		return x;
	}
	
	public void draw(ShapeRenderer shr,Batch b,BitmapFont f, float x, float y, float width, float height,int detectorCount){
		float up = height/detectorCount;	
		f.setColor(1,1,1,1);
		f.draw(b, "D("+id+","+(id+1)+"): ["+vec.x/vec.len()+","+vec.y/vec.len()+"]", x, up/2f+y+up*id+24f);
	};

}
