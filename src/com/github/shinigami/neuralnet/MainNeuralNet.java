package com.github.shinigami.neuralnet;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.GeneticUnit;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;
import com.github.shinigami.neuralnet.multithreading.TestRoomThread;
import com.github.shinigami.neuralnet.neuralnet.NeuronNet;
import com.github.shinigami.neuralnet.testroom.TestRepository;
import com.github.shinigami.neuralnet.testroom.TestRoom;

public class MainNeuralNet extends ApplicationAdapter {
	SpriteBatch batch;
	
	ShapeRenderer shr;
	Texture img;
	NeuronNet best;
	ArrayList<Float> input;
	TestRoom testroom;

	@Override
	public void create () {

		batch = new SpriteBatch();
		shr = new ShapeRenderer();
		img = new Texture(Gdx.files.internal("badlogic.jpg"));
		TestRoomThread t =new TestRoomThread(new NeuralNetGeneticUnit(3, 3, 4, 4, 4, 4));
		t.start();
		//testroom = TestRepository.collector;
	}
	float delta =0f;
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		delta = Gdx.graphics.getDeltaTime();
		//testroom.update(delta);
		//testroom.draw(batch, delta, 0f, 0f);
		
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
