package com.github.shinigami.neuralnet.neuralnet;

import java.util.ArrayList;


public class InputNeuronLayer extends NeuronLayer {

	public InputNeuronLayer(int inputs) {
		super(inputs, inputs);
		setInputs(inputs);
	}
	
	public void setInputs(int inputs){
		for(int i=0;i<inputs;i++){
			if(i>=neurons.size())
				neurons.add(i,new Neuron(inputs));
			
			neurons.get(i).type=NeuronType.INPUT;
			neurons.get(i).maininput=i;
		}
		this.inputCount=inputs;
		this.neuronCount=inputs;
	}
	@Override
	public void setGeneticUnit(int inputCount ,int neuronCount,ArrayList<Float> weight){
		setInputs(inputCount);
		
	}

}
