package com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks;

import com.badlogic.gdx.math.MathUtils;

public abstract class GeneticUnit<T extends GeneticUnit<T>> implements Comparable<GeneticUnit<T>>{
	public float score;
	public boolean tested;

	public void mutate(float probability){
		for(int i=0;i<getLength()-1;i++)
			if(MathUtils.random()<probability)
				mutate(i);
	}
	public abstract void mutate(int gene);
	
	public void swap(float probability,T s){
		if(MathUtils.random()<probability)
			swap(MathUtils.random(getLength()),s);
	}
	public abstract void swap(int gene, T s);
	
	public abstract T replace(T source);
	
	public abstract int getLength();
	
	public abstract boolean isCorrect();
	public abstract void correct();

	public abstract T getNew();
	
	@Override
	public int compareTo(GeneticUnit<T> d) {
		float diff = score-d.score;		
		int roundeddiff=MathUtils.round(diff);
		for(float i=10f;(roundeddiff==0 && diff!=0f);i*=10f)
			roundeddiff=MathUtils.round(diff*i);		
		return tested?-roundeddiff:-1;
	}
}
