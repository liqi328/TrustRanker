import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class InversePageRankSeedSelectionStrategy extends
		SeedSelectionStrategy {

	@Override
	public double[] selectSeeds(String goodSeedFilename, AdjacencyGraph g, int L) {
		double[] rankScores = PageRankRunner.run(GraphUtil.getInverseTransitionMatrix(g));
		
		Set<String> goodSeedSet = readSeedFile(goodSeedFilename);
		
		List<Rank> rankList = TopNAlgorithm.run(rankScores, L);
		
		double[] seeds = new double[g.getNodeNum()];
		Arrays.fill(seeds, 0.0);
		
		int goodSeedNum = 0;
		for(Rank rank : rankList){
			if(goodSeedSet.contains(g.getNodeName(rank.index))){
				seeds[rank.index] = 1.0;
				++goodSeedNum;
				System.out.println("+" + g.getNodeName(rank.index));
			}else{
				System.out.println("-" + g.getNodeName(rank.index));
			}
		}
		
		for(int i = 0; i < seeds.length; ++i){
			seeds[i] /= goodSeedNum; 
		}
		
		return seeds;
	}

}
