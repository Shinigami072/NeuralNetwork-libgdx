package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DetectorOutput extends Detector {
	public int output;
	public DetectorOutput(int id,int output){
		super(id);
		this.output=output;
		
	}
	
	@Override
	public Float detect(TestRoom testRoom) {
		detected = testRoom.output.get(output);
		return detected;
	}
	
	@Override
	public void draw(ShapeRenderer shr, Batch b, BitmapFont f, float x, float y, float width, float height,
			int detectorCount) {
		float up = height/detectorCount;	
		f.setColor(1,1,1,1);
		f.draw(b, "D("+id+":"+output+"): "+detected, x, up/2f+y+up*id+24f);
	}

}
