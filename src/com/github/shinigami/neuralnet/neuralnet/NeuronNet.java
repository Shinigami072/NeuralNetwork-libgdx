package com.github.shinigami.neuralnet.neuralnet;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;

public class NeuronNet {
	
	public ArrayList<NeuronLayer> layers;
	public int layerCount;
	//public ArrayList<Float> inputs;
	public int inputsize;
	public int outputsize;
	private ArrayList<Float> temp;
	public ArrayList<Float> outputs;
	
	private int minNeurons = 2;
	private int maxNeurons = 2;
	private int minLayers= 3;
	private int maxLayers= 5;
	
	public NeuronNet(NeuralNetGeneticUnit n){
		outputs = new ArrayList<Float>(n.getLayer(n.getLayers()-1));
		layers = new ArrayList<NeuronLayer>(n.getLayers());
		setGeneticUnit(n);
	}
	
	public ArrayList<Float> activated(ArrayList<Float> input){
		if(input ==null)
			System.out.println("no input");
		temp=input;
		for(NeuronLayer l:layers){
			temp = l.activated(temp);
			if(temp ==null)
				System.out.println("no output");
		}
		outputs =layers.get(layerCount-1).outputs;
		return outputs;	
	}
	
	public NeuralNetGeneticUnit getGeneticUnit(){
		ArrayList<Integer> layer = new ArrayList<Integer>();
		ArrayList<Float> weight= new ArrayList<Float>();

		for(NeuronLayer l:layers)
		{	layer.add(l.neuronCount);
			for(Neuron n:l.neurons)
				weight.addAll(n.weight);
		}
		
		NeuralNetGeneticUnit n = new NeuralNetGeneticUnit(layer, weight, minNeurons, maxNeurons,minLayers,maxLayers);
		
		return n;
	}
	public void setGeneticUnit(NeuralNetGeneticUnit n) {
		this.minNeurons=n.minNeuron;
		this.maxNeurons=n.maxNeuron;
		this.minLayers=n.minLayers;
		this.maxLayers=n.maxLayers;
		this.layerCount=n.getLayers();
		this.inputsize=n.getLayer(0);
		this.outputsize=n.getLayer(n.getLayers()-1);
		for(int i=0;i<n.getLayers();i++){
			if(i==0)
//				if(i>layers.size()-1){
//				layers.add(i,new InputNeuronLayer(n.getLayer(i)));
//				}
//				else{
//				layers.get(i).neuronCount=n.getLayer(i);
//				((InputNeuronLayer) layers.get(i)).setInputs(n.getLayer(i));
//				}
				if(i>layers.size()-1)
				layers.add(i,new InputNeuronLayer(n.getLayer(i)));
				else
					((InputNeuronLayer) layers.get(i)).setInputs(n.getLayer(i));
			else
				if(i>layers.size()-1)
				layers.add(i,new NeuronLayer(n.getLayer(i),n.getLayer(i-1)));

			
//			if(i==n.getLayers()-1)
//			for(Neuron ne:layers.get(i).neurons)
//				ne.type=NeuronType.OUTPUT;
		}

		int index=0;
		for(int i=1;i<n.getLayers();i++){
			
//			layers.get(i).neuronCount=n.getLayer(i);
//			layers.get(i).inputCount=n.getLayer(i-1);
			temp=new ArrayList<Float>(); 
			temp.addAll(n.weight.subList(index, index+n.getWeightCount(i)));
			layers.get(i).setGeneticUnit(n.getLayer(i-1), n.getLayer(i), temp);
			
			index+=n.getWeightCount(i);
			
		}
		//System.out.println(this);
	}
	
	public void draw(ShapeRenderer shr,float x,float y,float width,float height,float radius,float min){
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		float gap=width/layerCount;
		int i=0;
		shr.begin(ShapeType.Line);
		shr.setColor(1, 1,1, 1);
		shr.rect(x, y, width, height);
		shr.end();
		for(int in=0;in<layerCount;in++){
			if(layers.get(in) == null)
				System.out.println("no layer?!?!!");
			layers.get(in).draw(shr, x+gap*(i++)+gap/2f, y, gap, height,radius,min);			
		}
			
	}
	
	@Override
	public String toString() {
		String s = "[N: ";
		for(NeuronLayer l:layers)
		s+=l.toString()+" ";
		s+="]";
		return s;
	}

}
