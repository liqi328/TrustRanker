package rank;
import java.util.ArrayList;
import java.util.List;

/*
 * 从数组中选出前n大(小)的数
 * */

public class TopNAlgorithm {
	public static List<Rank> run(double[] values, int L){
		List<Rank> rankList = new ArrayList<Rank>();
		for(int i = 0; i < L; ++i){
			rankList.add(new Rank(0, 0.0));
		}
		
		int cur = 0;
		for(int i = 0; i < values.length; ++i){
			if(cur < L){
				rankList.get(cur).index = i;
				rankList.get(cur).value = values[i];
				++cur;
			}else{
				int index = getMinimumRankIndex(rankList);
				if(values[i] > rankList.get(index).value){
					rankList.get(index).index = i;
					rankList.get(index).value = values[i];
				}
			}
		}
		
		return rankList;
	}
	
	private static int getMinimumRankIndex(List<Rank> rankList){
		int index = 0;
		double minimum = rankList.get(0).value;
		
		for(int i = 1; i < rankList.size(); ++i){
			if(rankList.get(i).value < minimum){
				minimum = rankList.get(i).value;
				index = i;
			}
		}
		return index;
	}
}
