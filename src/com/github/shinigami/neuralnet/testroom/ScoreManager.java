package com.github.shinigami.neuralnet.testroom;

import java.util.ArrayList;

public class ScoreManager {

	public ArrayList<Collideable> scoreable;
	public ArrayList<Score> scores;
	
	
	public ScoreManager(TestRoom testRoom, int scoreamt,float score) {
		scoreable = new ArrayList<Collideable>();
		scores = new ArrayList<Score>(scoreamt);
		for(int i=0;i<scoreamt;i++)
		scores.add(new Score(testRoom,score,this));
	}
	
	public void draw(){
		for(Score s:scores)
			s.draw();
	}
	public void addScorable(Collideable sb) {
		if(!scoreable.contains(sb))
			scoreable.add(sb);
	}

}
