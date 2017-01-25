package com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;

public class NeuralNetGeneticUnit extends GeneticUnit<NeuralNetGeneticUnit> {

	public ArrayList<Integer> layer;
	public ArrayList<Float> weight;
	public int minNeuron;
	public int maxNeuron;
	public int maxLayers;
	public int minLayers;
	public static float min_float=-50f;
	public static float max_float=50f;

	
	public NeuralNetGeneticUnit(int minLayers, int maxLayers,int minNeuron,int maxNeuron,int inputs,int outputs){
		
		ArrayList<Integer> layer = new ArrayList<Integer>();
		int count = MathUtils.random(minLayers,maxLayers);
		layer.add(inputs);
		for(;count-1>layer.size();){
			layer.add(MathUtils.random(minNeuron,maxNeuron));
		}
		layer.add(outputs);
		
		this.maxNeuron=maxNeuron;
		this.minNeuron=minNeuron;
		this.minLayers=minLayers;
		this.maxLayers=maxLayers;
		this.layer=layer;
		this.weight= new ArrayList<Float>();
		correct();

	}
	public NeuralNetGeneticUnit(ArrayList<Integer> layer,ArrayList<Float> weigth,int minNeuron,int maxNeuron,int minLayers,int maxLayers){
		this.maxNeuron=maxNeuron;
		this.minNeuron=minNeuron;
		this.layer = new ArrayList<Integer>();
		this.layer.addAll(layer);
	
		this.weight= new ArrayList<Float>();
		this.weight.addAll(weigth);
		correct();
	}
	public int getLayers(){
		return layer.size();
	}
	public int getLayer(int i){
		return layer.get(i);
	}
	public void setLayer(int i, int val){
		layer.set(i, val);
		correct();
	}
	public void removeLayer(){
		if(layer.size()<=minLayers)
			return;
		layer.remove(layer.size()-2);
		correct();
	}
	public void addLayer()
	{
		if(layer.size()>=maxLayers)
			return;
		addLayer(MathUtils.random(minNeuron, maxNeuron));
	}
	public void addLayer(int val){
		//TODO: reevaluate
		if(layer.size()>=maxLayers)
			return;
		//int origwcount = getWeightCount(layer.size()-1);
		layer.add(layer.size()-1,val);
		correct();
		/*int count = getWeightCount(layer.size()-1)+(getWeightCount(layer.size()-2)-origwcount);
		for(;count>0;count--)
			addWeight();*/
	}
	public int getWeights(){
		return weight.size();
	}
	public int getWeightCount(int layer){
		if(layer==0)
		return 0;
		
		return (getLayer(layer-1)+1)*getLayer(layer);
		
	}
	public int getWeightCount(){
		int i=0;
		for(int j=0;j<layer.size();j++)
			i+=getWeightCount(j);
		return i;	
	}
	
	public float getWeight(int i){
		int in = i-layer.size()+1;
		if(in<0)
			System.out.println(layer.size()+" : " + i);
		return weight.get(in);
	}
	
	public void setWeight(int i,float f){
	  weight.set(i-(layer.size()-1), f);
	}
	
	public void addWeight(float f){
	  weight.add(f);
	}
	
	public void addWeight(){
		if(MathUtils.random()<0.66f)
			addWeight( MathUtils.random(min_float,max_float));
		else
			addWeight(0f);
	}
	
	public void addWeight(int count){
		for(;count>=0;count--)
			addWeight();
	}

	
	
	
	@Override
	public void mutate(int gene) {
		if(gene!=0 && gene!= layer.size()-1)
		if(gene<layer.size()){
			float prob = MathUtils.random();
			if(prob<0.33f){
				int old = getLayer(gene);
			setLayer(gene, MathUtils.random(minNeuron, maxNeuron));
			System.out.println("Mutating Layer ("+gene+") "+old+" --> "+getLayer(gene)+ " [ "+minNeuron+" , "+maxNeuron+" ]");
			}
			else if(prob<0.66f){
			System.out.println("removingLayer("+gene+")");
			removeLayer();
			}
			else{
			System.out.println("addingLayer("+gene+")");
			addLayer();
			}
		}
		else if(gene-(layer.size())<weight.size())
		{	
			float old = getWeight(gene);
			if(MathUtils.random()<0.66f)
				if(MathUtils.randomBoolean())
				setWeight(gene, MathUtils.random(min_float,0));
				else
				setWeight(gene, max_float-MathUtils.random(0,max_float));

			else
				setWeight(gene, 0f);
			
			System.out.println("Mutating randomWeight ("+gene+") "+old+" --> "+ getWeight(gene) + " [ "+min_float+" , "+max_float+" ]");

		}
		else {
			addWeight(gene-weight.size());
			System.out.println("Mutating addWeight ("+gene+") null --> "+ getWeight(gene)+ " [ "+min_float+" , "+max_float+" ]");
		}

	}

	@Override
	public void swap(int gene, NeuralNetGeneticUnit s) {	

//		if(getLayers()<s.getLayers() && gene<s.getLayers())
//		{
//			//less layers
//				swapLayers(gene,s.layer);
//		}
//		else if(s.getLayers()< getLayers() && gene<getLayers())
//		{
//			//more layers
//				swapLayers(gene,s.layer);
//		}		
//		else if(gene < getLayers())
//		{
//			//equal number of layers
//		}
		
		swapLayers(gene,s.layer);

		//replace weights
		swapWeights(gene, s.weight);
	}
	public void swapLayers(int gene,ArrayList<Integer> sourcelayer){
		
		if(gene >= getLayers()-1 || gene >= sourcelayer.size()-1 )
			return;
		
		boolean isGreater = sourcelayer.size()>getLayers();
		int diff = (getLayers()-sourcelayer.size());
		System.out.println("Swapping layers("+gene+"): " + diff+", "+getLayers());
		if(isGreater)
			for(int i=0;i<-diff;i++)
				addLayer();
		else
			for(int i=0;i<-diff;i++)
				sourcelayer.add(sourcelayer.size()-2,MathUtils.random(minLayers,maxLayers));
		
		for(int i=Math.max(gene, 1);i<getLayers()-1;i++){
			int layer = getLayer(i);
			setLayer(i, sourcelayer.get(i));
			sourcelayer.set(i, layer);
		}
		
		for(int i=0;i<Math.abs(diff);i++)
			if(isGreater)
				sourcelayer.remove(sourcelayer.size()-2);
			else
				removeLayer();
			
//TODO: MODIFU TOPOLOGY
//		
//		if(sourcelayer.size()>getLayers()){
//			int f;
//			for(int i=gene;i<getLayers();i++){
//				f=sourcelayer.get(i);
//				sourcelayer.set(i, getLayer(i));
//				setLayer(i,f);
//			}
//			while(sourcelayer.size()>getLayers()){
//				f=sourcelayer.get(sourcelayer.size()-2);
//				sourcelayer.remove(sourcelayer.size()-2);
//				addLayer(f);
//			}
//			return;			
//		}
//		if(sourcelayer.size()<getLayers()){
//			int f;
//			for(int i=gene;i<sourcelayer.size();i++){
//				f=sourcelayer.get(i);
//				sourcelayer.set(i, getLayer(i));
//				setLayer(i,f);
//			}
//			while(sourcelayer.size()<getLayers()){
//				f=getLayer(sourcelayer.size()-2);
//				removeLayer();
//				sourcelayer.add(sourcelayer.size()-2,f);
//			}
//			return;			
//		}
//		int f;
//		for(int i=gene;i<sourcelayer.size();i++){
//			f=sourcelayer.get(i);
//			sourcelayer.set(i, getLayer(i));
//			setLayer(i,f);
//		}
//		
		
	}
	public void swapWeights(int gene,ArrayList<Float> sourceweight){
		float f;
		System.out.println("Swapping weights("+gene+"): "+getWeightCount());
		if(gene-(layer.size()-1)<0)
			gene=layer.size();
		for(int i=gene;i<Math.max(sourceweight.size(),getWeights());i++){
			while(i>=sourceweight.size())
				sourceweight.add(MathUtils.random(min_float,max_float));
			if(i>=getWeights())
				addWeight(sourceweight.size()-getWeights());
			
			f=sourceweight.get(i);
			sourceweight.set(i, getWeight(i));
			setWeight(i,f);
		}
	}
	@Override
	public NeuralNetGeneticUnit replace(NeuralNetGeneticUnit source) {
		
		layer.clear();
		layer.addAll(source.layer);
		weight.clear();
		weight.addAll(source.weight);
		return this;
	}

	@Override
	public int getLength() {
		return getLayers()+getWeights();
	}


	@Override
	public	void correct() {
		if(getWeightCount()>=weight.size())
			addWeight(getWeightCount()-(weight.size()-1));
	}


	@Override
	public boolean isCorrect() {

		return getWeightCount()<=weight.size();
	}
	
	@Override
	public String toString() {
		String s="[GU: ";
		s+=(" LS:"+getLayers());
		s+=(" WS:"+getWeightCount());
		for(int i=1;i<getLayers();i++){
			s+=(" ["+i+" L: "+getLayer(i));
			s+=(" W: "+getWeightCount(i)+"/"+(getLayer(i-1)+1));
			s+="]";
		}
		s+=" ]";
		return s;
	}
	@Override
	public NeuralNetGeneticUnit getNew() {
		return new NeuralNetGeneticUnit(layer, weight, maxNeuron, maxNeuron,minLayers,maxLayers);
	}


}
