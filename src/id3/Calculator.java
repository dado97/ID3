package id3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Calculator {
	SampleSet sampleSet;
	double entropy;
	int decision;
	
	public Calculator(SampleSet sampleSet) {
		this.sampleSet = sampleSet;
		this.decision = sampleSet.getNumberOfAttributes()-1;
		this.entropy = this.calculateEntropy();
	}
	
	public double getEntropy() {
		return this.entropy;
	}
	
	public double log2(double number) {
		return Math.log(number)/Math.log(2);
	}
	
	public double entropyOfInstance(int numberOfInstances,int all) {
		if(numberOfInstances==0) return 0;
		return -1 * ((double)numberOfInstances/all) * log2((double)numberOfInstances/all);
	}
	
	public double calculateEntropy() {
		HashSet<String> values = sampleSet.getValuesForAttribute(decision);
		
		Iterator<String> iterator = values.iterator();
		ArrayList<Integer> instances = new ArrayList<Integer>();
		
		do {
			instances.add(sampleSet.getNumberOfSamplesWithValue(decision, iterator.next()));
		} while(iterator.hasNext());
		
		double toReturn = 0;
		
		for(Integer i : instances) {
			toReturn+=entropyOfInstance(i,sampleSet.getNumberOfSamples());
		}
		
		return toReturn;
	}
	
	public double calculateInformationGain(int attribute) {		
		HashSet<String> values = sampleSet.getValuesForAttribute(attribute);
		
		Iterator<String> iterator = values.iterator();
		double toReturn = this.entropy;
		
		while(iterator.hasNext()) {
			String toWorkWith = iterator.next();
			SampleSet reduced = sampleSet.reduceSet(attribute, toWorkWith);
			Calculator reducedCalculator = new Calculator(reduced); 
			
			toReturn -= reducedCalculator.calculateEntropy() * ((double)reduced.numberOfSamples)/sampleSet.numberOfSamples;
		}
		return toReturn;
	}
}
