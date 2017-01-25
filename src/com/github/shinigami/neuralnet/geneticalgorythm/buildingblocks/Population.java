package com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;

public class Population<T extends GeneticUnit<T>> {
	
	public int populationSize;
	public int parentPopuSize;
	public int index;
	public int generation;
	
	public float mutation;
	public float swap;
	public int elite;
	
	public float bestscore;
	private boolean setBest;
	private boolean setWorst;
	public float sumscore;
	public float avgscore;
	public float worstscore;
	public int count;
	
	ArrayList<T> population;
	ArrayList<T> parents;
	public Population(int populationSize,int parentPopuSize){
		this.populationSize = populationSize;
		this.parentPopuSize = parentPopuSize;
		generation=0;
		index=0;
		mutation = 0.03f;
		swap = 0.09f;
		elite=MathUtils.ceil(populationSize*0.01f);
		
		bestscore = 0f;
		avgscore = 0f;
		worstscore = 0f;
		
		population = new ArrayList<T>(populationSize);
		//temp  = new ArrayList<T>(populationSize);
		parents = new ArrayList<T>(parentPopuSize);	
		count=0;
	
	}
	
	public T get(int i){
		return population.get(i);		
	}
	public void add(T t){
		population.add(t);
		while(parents.size()<parentPopuSize)
			parents.add(t.getNew());
		//if(population.size()>temp.size())
		//	temp.add(t);
	}
	public T getNext(){
		if(get(index).tested){
		float score = get(index).score;			
		if( !setBest || score>bestscore){
			//System.out.println("best: "+score);
			bestscore=score;
			setBest=true;
		}
		if( !setWorst || score<worstscore){
			//System.out.println("worst: "+score);
			worstscore=score;		
			setWorst=true;
		}
		sumscore+=score;
		avgscore=sumscore/(++count);
		}
		
		index++;
		if(index>populationSize){
			nextGen();
			index=0;
		}
		
		return get(index);
	}
	
	public void sort(){
		population.sort(new Comparator<T>(){
			@Override
			public int compare(T a1, T a2) {
				return a1.compareTo(a2);
			}
			
		});
	}
	
	public void createPopulation(){
		//recycling parent pop;
		
		for(int i =0;i<parentPopuSize;i++){
			if(sumscore==0f){
				parents.get(i).replace(get(MathUtils.random(populationSize)));		
			}
			float choice = MathUtils.random(sumscore);
			for(int j=0;j<populationSize;j++){
				
				if(get(j).score<choice){
					choice-=get(j).score;
					continue;
				}
				else
				{
					parents.get(i).replace(get(j));
					//System.out.println("chosen "+j+" : "+choice+" : "+get(j).score);
					break;
				}		
			}
		}
		//System.out.println(parents);
		for(int i=0;i<elite;i++)
		{
			System.out.println("elite"+i);
			//int j = MathUtils.random(parentPopuSize-1);
			//get(i).replace(parents.get(j));
		}
		
		for(int i=elite;i<populationSize;i+=2){
			int i1=0;
			int i2=0;
			while(i1==i2){
				i1=MathUtils.random(parentPopuSize-1);
				i2=MathUtils.random(parentPopuSize-1);
			}
			//System.out.println(i+"parents: "+i1+" : "+i2);
			
			
			get(i).replace(parents.get(i1));
			get(i).score=0f;
			get(i).tested=false;
			if(i+1>populationSize)
				break;
			
			get(i+1).replace(parents.get(i2));
			get(i+1).score=0f;
			get(i+1).tested=false;
			
			get(i).swap(swap,get(i+1));
			get(i+1).mutate(mutation);
			get(i).mutate(mutation);
			

		}
		
	}
	
	public void nextGen() {
		sort();
		createPopulation();
		sumscore=0f;
		count=0;
		setWorst=false;
		setBest=false;
		generation++;
	}
	
	

}
