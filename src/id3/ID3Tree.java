package id3;

public class ID3Tree {
	ID3Node root;
	
	public ID3Tree(SampleSet sampleSet) {
		this.root = new ID3Node(sampleSet);
	}
	
	public int getNumberOfLeaves() {
		return root.getNumberOfLeaves();
	}
	
	public void printTree() {
		System.out.println(root);
	}
}
