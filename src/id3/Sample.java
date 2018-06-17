package id3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Sample {
	ArrayList<String> values;
	int size;
	
	public Sample(String row) {
		values = new ArrayList<String>();
		Arrays.stream(row.split(" ")).forEach(values::add);;
		size = values.size();
	}
	
	public String getValueForAttribute(int att) {
		return values.get(att);
	}
	
	public boolean satisfiesCondition(int att, String value) {
		return values.get(att).equals(value);
	}
	
	public String getDecision() {
		return values.get(size-1);
	}
	
	public ArrayList<String> getValues(){
		return values;
	}
	
	public String toString() {
		return values.stream().collect(Collectors.joining(" "));
	}
	
	public static void main(String [] args) {
		Sample sample = new Sample("one two three");
		System.out.println(sample + sample.getDecision());
	}
}