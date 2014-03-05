
public class Main {
	public static void main(String[] args){
		String ppiFilename = "E:/2013�����о�/ʵ������/TrustRanker/input/HPRD_ppi.txt";
		String seedFilename = "E:/2013�����о�/ʵ������/TrustRanker/input/neurodegenerative_seeds.txt";
		
		TrustRankRunner runner = new TrustRankRunner(new InversePageRankSeedSelectionStrategy(), 4);
		
		double[] rankScores = runner.run(ppiFilename, seedFilename);
		
		for(int i = 0; i < rankScores.length; ++i){
        	System.out.println(rankScores[i]);
        }
	}
}
