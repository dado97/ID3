package id3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class SampleSet {
	HashMap<Integer, String> columns = new HashMap<Integer, String>();
	HashMap<Integer, HashSet<String> > valuesForAttributes = new HashMap<Integer, HashSet<String> >();
	
	ArrayList<String> names = new ArrayList<String>();
	List<Sample> samples = new ArrayList<Sample>();
	
	int numberOfAttributes;
	int numberOfSamples;
	
	public SampleSet(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		
		setNames(scanner.nextLine());
		
		while(scanner.hasNextLine()) {
			String row = scanner.nextLine();
			
			Sample sample = new Sample(row);
			samples.add(sample);
			
			String[] split = row.split(" ");
			for(int i = 0;i<split.length;i++) {
				valuesForAttributes.get(i).add(split[i]);
			}
		}
		numberOfSamples = samples.size();
		
		scanner.close();
	}
	
	public SampleSet(SampleSet sampleSet, int attribute, String value) {
		this.columns = sampleSet.columns;
		this.valuesForAttributes = sampleSet.valuesForAttributes;
		this.names = sampleSet.names;
		this.numberOfAttributes = sampleSet.numberOfAttributes;
		
		this.samples = new ArrayList<Sample>();
		
		this.samples = sampleSet.samples.stream().filter(s -> s.satisfiesCondition(attribute, value)).collect(Collectors.toList());
		this.numberOfSamples = samples.size();
	}
	
	public SampleSet reduceSet(int attribute, String value) {
		return new SampleSet(this, attribute, value);
	}
	
	public void setNames(String row) {
		String[] split=row.split(" ");
		
		for(int i = 0; i<split.length ;i++) {
			columns.put(i, split[i]);
			names.add(split[i]);
			valuesForAttributes.put(i, new HashSet<String>());
		}
		
		numberOfAttributes = names.size();
	}
	
	public HashSet<String> getValuesForAttribute(int number){
		return valuesForAttributes.get(number);
	}
	
	public int getNumberOfSamplesWithValue(int att, String val) {
		int toReturn = 0;
		
		for(Sample s : samples) {
			if(s.satisfiesCondition(att, val)) toReturn++;
		}
		
		return toReturn;
	}
	
	public String getNameOfAttribute(int attribute) {
		return names.get(attribute);
	}
	
	public int getNumberOfSamples() {
		return this.numberOfSamples;
	}
	
	public int getNumberOfAttributes() {
		return this.numberOfAttributes;
	}
	
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append(names.stream().collect(Collectors.joining(" ")));
		toReturn.append("\n");
		
		toReturn.append(samples.stream().map(Sample::toString).collect(Collectors.joining("\n")));
		
		return toReturn.toString();
	}
	
	public String getDecision() {
		if(samples.size() == 0) return "No decision for this set";
		if(samples.size() == 1) return samples.get(0).getDecision();
		else {
			Set<String> decisions = samples.stream().map(sample -> sample.getDecision()).collect(Collectors.toSet());
			if(decisions.size() == 1) return decisions.iterator().next();
			else return "There is a contradiction in the sample set";
		}
	}
	
	public static void main(String [] args) throws FileNotFoundException {
		File f = new File("C:\\Users\\Nina\\Desktop\\Dado\\test2.txt");
		
		SampleSet ss = new SampleSet(f);
		
		//Calculator calc = new Calculator(ss);
		//System.out.println(calc.calculateEntropy());
		
		//System.out.println(ss.reduceSet(3, "weak"));
		
		//System.out.println(calc.calculateEntropy());
		//System.out.println(calc.calculateInformationGain(0));
		//System.out.println(calc.getAttributeWithMostInformationGain());
		ID3Tree tree = new ID3Tree(ss);
		tree.root.generateChildren();
		//System.out.println(tree.root);
		tree.printTree();
		System.out.println(tree.getNumberOfLeaves());
	}
}
