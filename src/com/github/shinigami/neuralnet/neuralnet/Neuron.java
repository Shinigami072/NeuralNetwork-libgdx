package com.github.shinigami.neuralnet.neuralnet;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.github.shinigami.neuralnet.MathHelper;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;

public class Neuron {
	
	public ArrayList<Float> weight;
	public NeuronType type;
	public int inputs;
	public float output;
	public float threshold;
	public int maininput =0;

	public Neuron(int inputs){	
		type=NeuronType.DEFAULT;
		this.weight=new ArrayList<Float>(inputs+1);
		ArrayList<Float> weight=new ArrayList<Float>(inputs+1);
		for(int i=0;i<inputs+1;i++)
			weight.add(-1+2*MathUtils.random());
		setGeneticUnit(weight,inputs);		
	}
	
	public Neuron(ArrayList<Float> weight,int inputs){
		type=NeuronType.DEFAULT;
		this.weight=new ArrayList<Float>(inputs+1);
		setGeneticUnit(weight,inputs);
	}
	
	public float activated(ArrayList<Float> input){
		float sum =0;
		
		
		switch(type){
		default:
			for(int i =0;i<inputs;i++)
				sum+=weight.get(i)*input.get(i);
			sum-=threshold;
		break;
		
		case BOOLEAN:
			for(int i =0;i<inputs;i++)
				sum+=weight.get(i)*input.get(i);
		break;
		
		case INPUT:
			sum=input.get(maininput);
			break;
		}
		
		output=interpret(sum);
		return output;
	}
	
	protected float interpret(float in){
		float o = 0f;
		switch(type){
		case INPUT:
			o=in;
		break;
		case BOOLEAN:
			o= in>threshold?1f:0f;
		break;
		default:
			o=MathHelper.sigimond(in, 0.1f);
		}
		return o; 
	}
	
	public void setGeneticUnit(ArrayList<Float> weight,int inputs){
		if(inputs>(weight.size()-1))
			System.out.println("too much input");
		//System.out.println("N:" + inputs+ " " +weight.size() +"/" + (inputs+1));
		this.inputs=inputs;
		this.weight.clear();
		this.weight.addAll(weight);
		this.threshold=weight.get(inputs);
		type= NeuronType.DEFAULT;
	}
	public void draw(ShapeRenderer shr,float x,float y,float radious,float min){
		shr.begin(ShapeType.Filled);
		switch(type){
		case INPUT:
			shr.setColor(0.7f, 0.6f, 0.3f, 0.9f);
		break;
		case OUTPUT:
			shr.setColor(0.3f, 0.6f, 0.7f, 0.9f);
		break;
		default:
			shr.setColor(0.4f, 0.6f, 0.4f, 0.9f);
		}
		shr.circle(x, y, 1.1f*(radious));
		shr.end();	
		
		
		shr.begin(ShapeType.Filled);
		shr.setColor( Math.abs(output),  Math.abs(output), Math.abs(output), 0.9f);
		shr.circle(x, y, Math.abs(output)*(radious-min)+min);
		shr.end();	
		
		shr.begin(ShapeType.Line);
		shr.setColor(0f, 0f, 0.6f, 1f);
	
		if(threshold>0f)
		shr.circle(x, y, threshold/NeuralNetGeneticUnit.max_float*(radious));
		else
		shr.circle(x, y, threshold/NeuralNetGeneticUnit.min_float*(radious));
		shr.end();


	}
	
	@Override
	public String toString() {
		return "[N("+inputs+")("+maininput+")"+output+"]";
	}

}
