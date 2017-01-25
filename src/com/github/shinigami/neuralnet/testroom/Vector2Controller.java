package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.math.Vector2;

public class Vector2Controller extends Controller {

	Vector2 vec;
	public Vector2Controller(int id,Vector2 v) {
		super(id);
		vec=v;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void controll(TestRoom testRoom) {
		vec.set(
				(2f*(testRoom.getOutput(id)-0.5f)),
				(2f*(testRoom.getOutput(id+1)-0.5f))
				);
	}

}
