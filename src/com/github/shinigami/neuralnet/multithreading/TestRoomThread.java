package com.github.shinigami.neuralnet.multithreading;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;
import com.github.shinigami.neuralnet.neuralnet.NeuronNet;

public class TestRoomThread extends Thread {
	
	boolean ready = false;
	NeuronNet neuronnet;
	ArrayList<Float> input;
	int inputs;
	public TestRoomThread(NeuralNetGeneticUnit n) {
		neuronnet = new NeuronNet(n);
		inputs = n.getLayer(0);
		input = new ArrayList<Float>(inputs);
		setupInput();
	}
	private void randomizeInput(){
		for(int i=0;i<inputs;i++)
			input.set(i, MathUtils.random(0f,1f));
	}
	private void setInput(ArrayList<Float> newinput){
		for(int i=0;i<inputs;i++)
			input.set(i, newinput.get(i));	
	}
	private void setupInput() {
		while(input.size()<inputs)
			input.add(MathUtils.random(0f,1f));
	}

	void setGeneticUnit(NeuralNetGeneticUnit n){
		neuronnet.setGeneticUnit(n);
		inputs = n.getLayer(0);
		setupInput();
	}
	
	boolean isReady(){
		return ready;		
	}
	
	@Override
	public void run() {
		super.run();
		while(!ready){
			neuronnet.activated(input);
			randomizeInput();
			System.out.println(neuronnet.outputs);
			//System.out.println(Gdx.graphics.getDeltaTime()*1000);
		}
	}

}
