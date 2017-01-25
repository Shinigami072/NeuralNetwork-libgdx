package com.github.shinigami.neuralnet.testroom;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.Population;
import com.github.shinigami.neuralnet.neuralnet.NeuronNet;

public abstract class TestRoom {
	
	public float score;
	public float elapsed;
	public float testtime;
	public boolean finished;
	public float width;
	public float height;
	public int outputs;
	public int inputs;
	boolean showNet =true;;

	
	public ArrayList<Float> input;
	public ArrayList<Float> output;
	public ArrayList<Detector> detectors;
	public ArrayList<Controller> controllers;
	public ArrayList<Controllable> controllables;
	public ArrayList<Collideable> collideables;

	
	public BitmapFont font;
	public ShapeRenderer shr;
	public Population<NeuralNetGeneticUnit> population; 
	public NeuronNet neuronnet;
	public Batch batch;
	
	TestRoom(Population<NeuralNetGeneticUnit> p,float testtime,int inputs,int outputs){
		this.population = p;

		input = new ArrayList<Float>(inputs);
		for(int i=0;i<inputs;i++)
			input.add(0f);
		output = new ArrayList<Float>(outputs);
		for(int i=0;i<outputs;i++)
			output.add(0f);
		
		this.testtime=testtime;
		this.inputs=inputs;
		this.outputs=outputs;
		finished=false;
		font = new BitmapFont();
		shr = new ShapeRenderer();
		detectors = new ArrayList<Detector>();
		controllers = new ArrayList<Controller>();
		controllables = new ArrayList<Controllable>();
		collideables = new ArrayList<Collideable>();
		setup();
		neuronnet = new NeuronNet(p.get(0));
	}
	protected void fillPopulation(int minLayer,int maxLayer,int minNeuron,int maxNeuron){
		int i = population.populationSize;
		for(;i>=0;i--)
		population.add(new NeuralNetGeneticUnit(minLayer,maxLayer,minNeuron,maxNeuron,inputs,outputs));
	}
	abstract void setup();
	abstract void grade();
	abstract void simulate(float delta);
	protected void addDecector(Detector d) {
		if(!detectors.contains(d))
		detectors.add(d);
	}
	protected void addControllable(Controllable d) {
		if(!controllables.contains(d))
			controllables.add(d);
		if(d instanceof Collideable)
		addCollideable((Collideable)d);
	}
	protected void addCollideable(Collideable d) {
		if(!collideables.contains(d))
			collideables.add(d);
	}
	protected void addController(Controller d) {
		if(!controllers.contains(d))
			controllers.add(d);
	}
	public void setInput(int i,float in){
		if(i<input.size()){
			input.set(i, in);
			return;
		}
		while(i<input.size()-1){
			if(i!=input.size()-1)
			input.add(0f);
			else
			input.add(in);
		}
	}
	
	public float getOutput(int i){
		if(i<output.size())
			return output.get(i);
		
		return 0;
	}
	
	public void update(float delta){
		if(finished){
			grade();
			population.get(population.index).score=score;
			population.get(population.index).tested=true;
			reset();
			neuronnet.setGeneticUnit(population.getNext());
		}
		else{
			for(Detector d:detectors)
				setInput(d.id,d.detect(this));
			neuronnet.activated(input);
			output = neuronnet.outputs;
			for(Controller c:controllers)
				c.controll(this);
			simulate(delta);	
			
			for(Controllable c:controllables)
				c.simulate(this,delta);
			
			for(int i=0;i<collideables.size();i++)
				for(int j=i+1;j<collideables.size();j++){
				if(collideables.get(i).getBoundingBox().overlaps(collideables.get(j).getBoundingBox()))
				{
					collideables.get(i).onCollision(collideables.get(j));
					//collideables.get(j).onCollision(collideables.get(i));

				}}
			elapsed+=delta;
			finished=elapsed>testtime;
		}
		
	}

	public void reset() {
		elapsed=0f;
		finished=false;
		score=0f;
		for(int i=0;i<input.size();i++)
			setInput(i,0f);
	}
	
	public void draw(Batch batch,float delta,float x,float y){
		this.batch=batch;
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shr.begin(ShapeType.Line);
		shr.setColor(1, 1,1, 1);
		shr.rect(x, y, width, height);
		shr.end();
		
		for(Controllable c:controllables)
			c.draw(this,x,y,delta);
		
		if(showNet)
		{
			batch.begin();
			float w = width;
			float h = height*0.7f; 
			for(Detector d:detectors)
				d.draw(shr,batch,font, x, y, w*0.3f, h,inputs);
			batch.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			batch.begin();
			neuronnet.draw(shr, x+w*0.3f, y, w*0.7f, h, 10f, 1f);
			batch.end();
			batch.begin();
			font.setColor(1f,1f,1f,0.7f);
			float up = h/neuronnet.outputsize;
			for(int i=0;i<neuronnet.outputsize;i++){
				font.draw(batch,i+" : "+MathUtils.round(output.get(i)*100f)/100f,x+w-10f -(w*0.7f/(2f*neuronnet.layerCount)),y-10f+up/2f+up*i);
			}
			up = h/neuronnet.inputsize;
			for(int i=0;i<neuronnet.inputsize;i++){
				font.draw(batch,i+" : "+MathUtils.round(input.get(i)*100f)/100f,x+w*0.3f-10f +(w*0.7f/(2f*neuronnet.layerCount)),y-10f+up/2f+up*i);
			}
			batch.end();
			
			
		}
		batch.begin();
		font.setColor(1f,1f,1f,0.7f);
		font.draw(batch,"Generation:"+population.generation,x,y+height);
		font.draw(batch,"NeuralNet:"+population.index,x,y+height-14f);
		font.draw(batch,"AvrageScore:"+MathUtils.round(population.avgscore*100f)/100f,x,y+height-28f);
		font.draw(batch,"BestScore:"+MathUtils.round(population.bestscore*100f)/100f,x,y+height-42f);
		font.draw(batch,"worstScore:"+MathUtils.round(population.worstscore*100f)/100f,x,y+height-56f);
		font.draw(batch,"score:"+MathUtils.round(score*100f)/100f ,x,y+height-70f);
		font.draw(batch,"time:"+MathUtils.round(elapsed*10f)/10f+"/"+testtime,x,y+height-84f);

		batch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		 
	}
}
