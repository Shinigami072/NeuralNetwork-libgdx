package com.github.shinigami.neuralnet.testroom;

public abstract class Controller {
	
		public int id;

		public Controller(int id) {
			this.id=id;
		}
		
		public abstract void controll(TestRoom testRoom);

}
