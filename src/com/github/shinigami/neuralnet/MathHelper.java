package com.github.shinigami.neuralnet;

public class MathHelper {
	public static float sigimond(float input,float activation)
	{
		float f=(float)(1f/(1f+Math.exp(-input*activation)));
		return  f;
	}

}
