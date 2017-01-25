package com.github.shinigami.neuralnet.neuralnet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.NeuralNetGeneticUnit;
import com.github.shinigami.neuralnet.geneticalgorythm.buildingblocks.Population;

public class NeuralNetIO {
	
	
	public static void writeFloatArray(DataOutputStream o,ArrayList<Float> array) throws IOException{		
		o.writeInt(array.size());
		for(float f:array)
			o.writeFloat(f);
	}
	
	public static ArrayList<Float> readFloatArray(DataInputStream o) throws IOException{
		int size = o.readInt();
		ArrayList<Float> a =new ArrayList<Float>(size);
		for(;size>=0;size--)
			a.add(o.readFloat());
		return a;	
	}
	
	public static void writeIntArray(DataOutputStream o,ArrayList<Integer> array) throws IOException{
		o.writeInt(array.size());
		for(int f:array)
			o.writeInt(f);
	}
	
	public static ArrayList<Integer> readIntArray(DataInputStream o) throws IOException{
		int size = o.readInt();
		ArrayList<Integer> a =new ArrayList<Integer>(size);
		for(;size>=0;size--)
			a.add(o.readInt());
		return a;	
	}
	
	public static void writeNeuralNetGeneticUnit(DataOutputStream o,NeuralNetGeneticUnit gu) throws IOException{		
		o.writeInt( gu.maxNeuron );
		o.writeInt( gu.minNeuron );
		
		o.writeInt( gu.maxLayers );
		o.writeInt( gu.minLayers );
		
		o.writeBoolean(gu.tested);
		o.writeFloat(gu.tested?gu.score:0f);

		writeIntArray(o,gu.layer);
		
		writeFloatArray(o,gu.weight);
		o.close();
	}
	
	public static void writeNeuralNetGeneticUnit(FileHandle o,NeuralNetGeneticUnit gu) throws IOException{
		writeNeuralNetGeneticUnit(new DataOutputStream(o.write(false)),gu);
	}
	
	public static void writeNeuronNet(FileHandle o,NeuronNet gu) throws IOException{
		writeNeuralNetGeneticUnit(new DataOutputStream(o.write(false)),gu.getGeneticUnit());
	}
	
	public static void writePopulation(FileHandle o,Population<NeuralNetGeneticUnit> gu) throws IOException{
	
		DataOutputStream out = new DataOutputStream( o.child(".meta").write(false));
		out.writeInt(gu.populationSize);
		out.writeInt(gu.parentPopuSize);
		
		out.writeInt(gu.elite);
		out.writeFloat(gu.mutation);
		out.writeFloat(gu.swap);
		
		out.writeInt(gu.index);	
		out.writeInt(gu.count);
		out.writeFloat(gu.sumscore);
		out.writeFloat(gu.bestscore);
		out.writeFloat(gu.worstscore);
		
		out.close();
		
		for(int i=0;i<gu.populationSize;i++)
			writeNeuralNetGeneticUnit( new DataOutputStream( o.child(i+".ann").write(false)), gu.get(i));
				
	}
	public static Population<NeuralNetGeneticUnit> readPopulation(FileHandle o) throws IOException{
		
		DataInputStream in = new DataInputStream( o.child(".meta").read());
		int populationSize = in.readInt();
		int parentPopuSize = in.readInt();
		
		int elite = in.readInt();
		float mutation =  in.readFloat();
		float swap = in.readFloat();
		
		int index = in.readInt();	
		int count = in.readInt();
		float sumscore = in.readFloat();
		float bestscore = in.readFloat();
		float worstscore = in.readFloat();
		
		in.close();
		
		Population<NeuralNetGeneticUnit> p = new Population<NeuralNetGeneticUnit>( populationSize, parentPopuSize);
		p.elite = elite;
		
		p.mutation = mutation;
		p.swap = swap;
		
		p.index = index;
		
		p.sumscore = sumscore;
		p.bestscore = bestscore;
		p.worstscore = worstscore;
		
		p.count = count;
		for(int i=0;i<populationSize;i++)
			p.add( readNeuralNetGeneticUnit( new DataInputStream( o.child(i+".ann").read())) );
				
		
		
		return p;
	}
	
	public static NeuralNetGeneticUnit readNeuralNetGeneticUnit(DataInputStream o) throws IOException{
		int maxNeuron = o.readInt();
		int minNeuron = o.readInt();
		
		int maxLayers= o.readInt();
		int minLayers = o.readInt();
		
		boolean tested = o.readBoolean();
		float   score  = o.readFloat();
		
		ArrayList<Integer> layers = readIntArray(o);
		ArrayList<Float>   weigths= readFloatArray(o);
		
		NeuralNetGeneticUnit c = new NeuralNetGeneticUnit(layers, weigths, minNeuron, maxNeuron,minLayers,maxLayers);
		c.tested=tested;
		c.score=score;
		
		return c;
	}
	
	public static NeuralNetGeneticUnit readNeuralNetGeneticUnit(FileHandle o) throws IOException{
		return readNeuralNetGeneticUnit(new DataInputStream(o.read()));
	}
	
	public static NeuronNet readNeuronNet(FileHandle o) throws IOException{
		return new NeuronNet(readNeuralNetGeneticUnit(new DataInputStream(o.read())));
	}
	
//	public static void writeToFile(NeuronNet net, FileHandle file ) throws IOException{
//		DataOutputStream dataout = new DataOutputStream(file.write(false));
//		dataout.writeInt(net.layers.size());//writeNumberOfLayers
//		for(NeuronLayer l:net.layers){
//		dataout.writeInt(l.neurons.size());//writenumberofNeuronsPerLayer
//		}
//		ArrayList<Float> DNA = net.getDNA();
//		for(float f:DNA)
//			dataout.writeFloat(f);//writeallfloats
//			
//		dataout.close();
//
//		
//	}
//	
//	public static NeuronNet readFile(FileHandle file) throws IOException{
//		DataInputStream datain = new DataInputStream(file.read());
//		int layerNumber = datain.readInt();
//		int[] NeuronLayerCount = new int[layerNumber];
//		
//		for(int i=0;i<layerNumber;i++)
//			NeuronLayerCount[i]=datain.readInt();
//		NeuronNet n =new NeuronNet(NeuronLayerCount[0], layerNumber, NeuronLayerCount[layerNumber-1]);
//		for(int i=1;i<layerNumber;i++)
//			n.layers.add(new NeuronLayer(NeuronLayerCount[i],NeuronLayerCount[i-1]));
//	
//		
//		ArrayList<Float> DNA = new ArrayList<Float>();
//
//		
//		
//		for(int i=1;i<layerNumber;i++)
//			for(int j=0;j<NeuronLayerCount[i];j++)			
//				for(int w=0;w<NeuronLayerCount[i-1]+1;w++)
//					DNA.add(datain.readFloat());
//		n.setDNA(DNA);
//			
////		for(int i=1;i<layerNumber;i++){
////			NeuronLayer layer = new NeuronLayer(NeuronLayerCount[i], NeuronLayerCount[i-1]);
////			for(int j=0;j<NeuronLayerCount[i];j++){
////				ArrayList<Float> weights = new ArrayList<Float>(NeuronLayerCount[i-1]);
////				
////				for(int w=0;w<NeuronLayerCount[i-1]+1;w++)
////					weights.add(datain.readFloat());
////				
////				layer.setInputs(j, weights);	
////			}
////			
////			n.layers.add(layer);
////		}
////		for(Neuron neu:n.layers.get(layerNumber-1).neurons)
////			neu.type=NeuronType.OUTPUT;
//		
//		return n;	
//	}

}
