package rank;
import java.util.Arrays;


public class Rank implements Comparable<Rank>{
	public int index = 0;
	public double value = 0.0;
	
	public Rank(int i, double v){
		index = i;
		value = v;
	}
	
	@Override
	public String toString(){
		return "" + index + "\t" + value;
	}

	@Override
	public int compareTo(Rank rightObj) {
		// ½µÐò
		return Double.valueOf(rightObj.value).compareTo(Double.valueOf(this.value));
	}
	
	public static void main(String[] args){
		Rank[] ranks = {
				new Rank(1, 0.7), new Rank(3, 0.3), new Rank(8, 0.5), new Rank(2, 1.0)
		};
		
		Arrays.sort(ranks);
		
		for(Rank rank: ranks){
			System.out.println(rank);
		}
	}
}