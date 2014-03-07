package rank;
import graph.AdjacencyGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RankUtil {
	
	public static Rank[] sort(double[] rankScores, AdjacencyGraph g){
		List<Rank> rankList = new ArrayList<Rank>();
		for(int i = 0; i < rankScores.length; ++i){
			rankList.add(new Rank(i, rankScores[i]));
		}
		
		Rank[] rankArr = rankList.toArray(new Rank[]{});
		
		Arrays.sort(rankArr);
		
		return rankArr;
	}
	
	public static String array2String(double[] rankScores, AdjacencyGraph g){
		Rank[] rankArr = sort(rankScores, g);
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < rankArr.length; ++i){
			sb.append(g.getNodeName(rankArr[i].index)).append("\t");
			sb.append(rankArr[i].value).append("\n");
		}
		
		return sb.toString();
	}

}
