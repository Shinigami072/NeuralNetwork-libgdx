package com.github.shinigami.neuralnet.testroom;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.Population;

public class TestRepository {
	public static TestRoom geneticTester = new TestRoom(new Population<NeuralNetGeneticUnit>(20, 10), 0.50f, 3, 3){

		float anwser = 0.9f;
		float anwsersum = 0f;
		int anwsercount =0;
		
		@Override
		void setup() {
			width=550f;
			height=400f;
			fillPopulation(3, 4, 1, 4);
			population.elite=3;
			addDecector(new Detector(0){

				@Override
				public Float detect(TestRoom testRoom) {
					detected =1f;
					return detected;
				}});
			addDecector(new DetectorOutput(1,0));
			addDecector(new DetectorKey(2,Keys.A));
		
			
			
		}



		@Override
		void grade() {
			score = 1f-Math.abs(anwser-anwsersum/anwsercount);
			
		}

		@Override
		void simulate(float delta) {
			anwsersum+=output.get(0);
			anwsercount++;
		}
		@Override
		public void reset() {
			super.reset();
			anwsercount=0;
			anwsersum=0f;
		};
		
		
	};
	
	public static TestRoom collector = new TestRoom(new Population<NeuralNetGeneticUnit>(40, 10), 5f, 4, 2){

		Vector2 nextpoint;
		Vector2 next;
		Vector2 controller;
		ScoreManager sm;
		SimpleBox sb;
		float f =0f;
		float distsum;
		float defaulttime;
		@Override
		void setup() {
			width=550f;
			height=400f;
			defaulttime=testtime;
			//showNet=false;
			controller = new Vector2(0f,0f);
			next = new Vector2(0f,0f);
			nextpoint = new Vector2(MathUtils.random(0,width),MathUtils.random(0,height));
			distsum =0f;
			fillPopulation(3, 7, 3, 9);
			population.elite=3;
			
			sb = new SimpleBox(this, controller, 20f, 20f);
			
			sm = new ScoreManager(this,20,10f);
			
			sm.addScorable(sb);
			
			for(Score s:sm.scores)
				addCollideable(s);
			
			addDecector(new Vector2Detector(0,next));
			addDecector(new Vector2Detector(2,sb.velocity));
			addController(new Vector2Controller(0, controller));

			addControllable(sb);

		}

		
		@Override
		void grade() {
			score+=(score>0?score/2f:1f)*(1f - distsum/(f*(height*height+width*width)));
		}

		@Override
		void simulate(float delta) {
			
			float l = -1f;
			for(Score s:sm.scores){
				nextpoint.set(s.position).sub(sb.position);
				if(l>nextpoint.len2() || l<0)
				{
					l=nextpoint.len2();
					next.set(s.position).sub(sb.position);
				}
			}
			distsum+=next.len2();
			f+=1f;
			testtime=defaulttime+0.01f*score;
			
		}
		@Override
		public void reset() {
			super.reset();
			distsum =0f;
			sb.position.set(width/2f,height/2f);
			sb.velocity.set(0f,0f);
			System.out.println(sb.getVelocity());
			f =0f;
			testtime=defaulttime;

		};
		
		@Override
		public void draw(Batch batch, float delta, float x, float y) {
			
			
			sm.draw();
			
			
			
			super.draw(batch, delta, x, y);
			
			Vector2 n = new Vector2(sb.position);
			shr.begin(ShapeType.Line);
			shr.setColor(0.7f,1f,0f,1f);
			shr.line(sb.position, n.add(next));
			shr.end();
		};
		
	};
}
