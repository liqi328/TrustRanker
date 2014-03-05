
public class Main {
	public static void main(String[] args){
		String ppiFilename = "E:/2013疾病研究/实验数据/TrustRanker/input/HPRD_ppi.txt";
		String seedFilename = "E:/2013疾病研究/实验数据/TrustRanker/input/neurodegenerative_seeds.txt";
		
		TrustRankRunner runner = new TrustRankRunner(new InversePageRankSeedSelectionStrategy(), 4);
		
		double[] rankScores = runner.run(ppiFilename, seedFilename);
		
		for(int i = 0; i < rankScores.length; ++i){
        	System.out.println(rankScores[i]);
        }
	}
}
