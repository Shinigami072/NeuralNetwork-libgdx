package com.github.shinigami.neuralnet.neuralnet;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;

public class NeuronLayer {
	
	public ArrayList<Neuron> neurons;
	public int neuronCount;
	public int inputCount;
	public ArrayList<Float> outputs;
//	private ArrayList<Float> inputs;
//	private ArrayList<Float> temp;

	public NeuronLayer(int neuron,int inputs){
		neuronCount=neuron;
		inputCount=inputs;
		neurons = new ArrayList<Neuron>(neuron);
		outputs = new ArrayList<Float>(neuron);
		//temp = new ArrayList<Float>(neuron);
		for(int i=0;i<neuron;i++){
			neurons.add(new Neuron(inputs));
			outputs.add(0f);		
		}				
	}
	
	public ArrayList<Float> activated(ArrayList<Float> input){
		for(int i=0;i<neuronCount;i++)
			outputs.set(i, (neurons.get(i).activated(input)));
//		temp.clear();
//		temp.addAll(outputs.subList(0, neuronCount));
		return outputs;
	}
	public void setGeneticUnit(int inputCount ,int neuronCount,ArrayList<Float> weight){
		//System.out.println("L:"+inputCount+"'"+neuronCount+":"+((inputCount+1)*neuronCount)+"/"+weight.size());
		this.neuronCount=neuronCount;
		this.inputCount=inputCount;
		ArrayList<Float> nwei = new ArrayList<Float>();

		for(int i=0;i<neuronCount;i++){
			nwei.clear();
			nwei.addAll(weight.subList(i*(inputCount+1), (i+1)*(inputCount+1)));
			if(neurons.size()<=i){
				neurons.add(i,new Neuron(nwei,inputCount));
				outputs.add(0f);
			}
			else
			neurons.get(i).setGeneticUnit(nwei, inputCount);
		
		}
//		neurons.get(i).setInputs(weight,inputCount);
	}
	
	public void draw(ShapeRenderer shr,float x,float y,float gap,float height,float radious, float min){
		float up=height/neuronCount;
		float inputup=height/inputCount;
		
		Neuron n;
		for(int in =0;in<neuronCount;in++){
			n = neurons.get(in);

			if(!(this instanceof InputNeuronLayer))
				for(int i=0;i<n.inputs;i++){
					float mag = Math.abs(outputs.get(in))*0.4f;
					shr.begin(ShapeType.Filled);

					if(n.weight.get(i)<0f){
						shr.setColor(1f, 0, mag, 0.9f*Math.abs(n.weight.get(i)/NeuralNetGeneticUnit.max_float)+0.05f);	
					}else if(n.weight.get(i)>0f){
						shr.setColor(0,1f, mag,0.95f*Math.abs(n.weight.get(i)/NeuralNetGeneticUnit.min_float)+0.05f);
					}
					else {
						shr.setColor(0f,0.2f, 1f,0.10f);
					}
					
					shr.rectLine(
							x-0.9f*radious, up/2f+y+up*in+(-0.5f+(float)i/(float)(n.inputs))*radious,
							x-gap+0.9f*radious, inputup/2f+y+inputup*i+(-0.5f+(float)in/(float)(neuronCount))*radious,2f);
					shr.end();
					
			}
			
			n.draw(shr, x, height/(2f*neuronCount)+y+up*(in), radious, min);
			
		}
			
	}
	
	@Override
	public String toString() {
		return "[L:"+neurons.toString()+"]";
	}

}
