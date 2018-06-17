package id3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ID3Node {
	String classification;
	int depth;
	int attribute;
	int numberOfAttributes;
	double entropy;
	
	SampleSet sampleSet;
	Calculator calculator;
	
	ArrayList<Integer> usedAttributes;
	ArrayList<ID3Node> children;
	
	public ID3Node(SampleSet sampleSet) {
		this.depth = 0;
		this.classification = null;
		
		this.sampleSet = sampleSet;
		this.calculator = new Calculator(sampleSet);
		
		this.setNumberOfAttributes();
		this.entropy = calculator.getEntropy();
		//Set attribute TODO
		//this.attribute = calculator.getAttributeWithMostInformationGain();
		this.usedAttributes = new ArrayList<Integer>();
		this.children = new ArrayList<ID3Node>();
		this.attribute = this.selectAttribute();
	}
	
	public ID3Node(ID3Node parent, int depthOfParent, int attribute, String classification) {
		this.depth = depthOfParent+1;
		this.classification = classification;
		
		this.sampleSet = parent.getSampleSet().reduceSet(attribute, classification);
		this.calculator = new Calculator(this.sampleSet);
		
		this.entropy = calculator.getEntropy();
		this.numberOfAttributes = parent.numberOfAttributes;
		
		this.usedAttributes = new ArrayList<Integer>(parent.usedAttributes);
		this.usedAttributes.add(attribute);
		
		this.attribute = this.selectAttribute();
		this.children = new ArrayList<ID3Node>();
	}
	
	public void setNumberOfAttributes() {
		this.numberOfAttributes = sampleSet.getNumberOfAttributes() - 1;
	}
	
	public int selectAttribute() {
		if(this.isLeaf()) return 0;
		
		double maxIG = 0;
		int toReturn = 0;
		
		for(int i = 0;i<this.numberOfAttributes;i++) {
			if(this.usedAttributes.contains(i)) continue;
			
			double informationGain = this.calculator.calculateInformationGain(i);
			if(informationGain >= maxIG) {
				maxIG=informationGain;
				toReturn = i;
			}
		}
		
		return toReturn;
	}
	
	public String getNameOfNode() {
		return sampleSet.getNameOfAttribute(attribute);
	}
	
	public SampleSet getSampleSet() {
		return this.sampleSet;
	}
	
	public double getEntropy() {
		return this.entropy;
	}
	
	public boolean isLeaf() {
		if( this.entropy == 0 || this.sampleSet.getNumberOfSamples() == 0) return true;
		if( this.usedAttributes.size() == this.numberOfAttributes) return true;
		return false;
	}
	
	public void generateChildren() {
		if(this.isLeaf()) return;
		
		HashSet<String> classifications = sampleSet.getValuesForAttribute(attribute);
		Iterator<String> iterator = classifications.iterator();
		
		while(iterator.hasNext()) {
			String toWorkWith = iterator.next();
			
			ID3Node child = new ID3Node(this, this.depth, this.attribute, toWorkWith);
			
			children.add(child);
			child.generateChildren();
		}
	}
	
	public String toString() {
		if(this.isLeaf()) {
			return "Depth of leaf is: " + this.depth + "\n" 
					+ "Classification: " + this.classification + "\n"
					+ "Used attributes: " + this.usedAttributes + "\n"
					+ "The decision for this leaf is: " + this.getDecision() + "\n\n";
		}
		StringBuilder toReturn = new StringBuilder();
		
		toReturn.append("Name of node is: " + this.getNameOfNode() + "\n");
		toReturn.append("Classification: " + this.classification + "\n");
		toReturn.append("Depth of node is: " + depth + "\n");
		toReturn.append("Entropy of node is: " + entropy + "\n\n");
		
		for(ID3Node child : children) {
			toReturn.append(child.toString());
		}
		
		return toReturn.toString();
	}
	
	public int getNumberOfLeaves() {
		if(this.isLeaf()) return 1;
		else return children.stream().mapToInt(node -> node.getNumberOfLeaves()).sum();
	}

	private String getDecision() {
		return this.sampleSet.getDecision();
	}
}
