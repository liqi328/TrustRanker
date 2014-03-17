package prince;

import graph.AdjacencyGraph;
import diseasefamily.DiseaseSimilarity;
import diseasefamily.Disorder;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;

public class PrinceStartProbabilityStrategy{
	private AdjacencyGraph g;
	private DiseaseSimilarity diseaseSimilarity;
	private GeneDiseaseAssociation associations;
	
	public PrinceStartProbabilityStrategy(AdjacencyGraph g, DiseaseSimilarity diseaseSimilarity,
			GeneDiseaseAssociation associations){
		this.g = g;
		this.diseaseSimilarity = diseaseSimilarity;
		this.associations = associations;
	}
	
	
	public double[] getStartProbability(String diseaseOmimId) {
		double[] startProbability = new double[g.getNodeNum()];
		
		for(int i = 0; i < g.getNodeNum(); ++i){
			startProbability[i] = logisticFunction(maxDiseaseSimilarity(g.getNodeName(i), diseaseOmimId));
		}
		
		return startProbability;
	}
	
	/**
	 * for a query disease q and a protein v associated with a disease p
	 * 给定一个与疾病p相关的基因,和query疾病q, 计算p和q的相似性 
	 * @param geneNameInPPI		gene的名称,与PPI中gene的表示一致. 若PPI中的基因用HPRD id表示，则此处的geneName也是HPRD id
	 * @param diseaseOmimId		query疾病的OMIM id
	 * @return
	 */
	private double maxDiseaseSimilarity(String geneNameInPPI, String diseaseOmimId){
		Gene gene = associations.geneMap.get(geneNameInPPI);
		if(gene == null){
			return 0.0;
		}
		
		double max = 0.0;
		double tmp = 0.0;
		for(Disorder disorder : gene.disorderMap.values()){
			tmp = diseaseSimilarity.getTwoDiseaseSimilarity(diseaseOmimId, disorder.getOmimId());
			if(Double.doubleToLongBits(tmp) > Double.doubleToLongBits(max)){
				max = tmp;
			}
		}
		
		return max;
	}
	
	private static double logisticFunction(double x){
		if(Double.doubleToLongBits(x) >= Double.doubleToLongBits(0.0) &&
				Double.doubleToLongBits(x) <= Double.doubleToLongBits(0.3)){
			return 0.0001;
		}
		
		if(Double.doubleToLongBits(x) >= Double.doubleToLongBits(0.6) &&
				Double.doubleToLongBits(x) <= Double.doubleToLongBits(1.0)){
			return 0.9999;
		}
		
		double d = Math.log(9999);
		int c = -15;
		
		return 1.0 / (1.0 + Math.log(c * x + d));
	}
	
	
	public static void main(String[] args){
		System.out.println(logisticFunction(0.2));
		System.out.println(logisticFunction(0.3));
		System.out.println(logisticFunction(0.4));
		System.out.println(logisticFunction(0.5));
		System.out.println(logisticFunction(0.6));
		System.out.println(logisticFunction(0.8));
		System.out.println(logisticFunction(1.0));
		System.out.println(logisticFunction(1.6));
		System.out.println(logisticFunction(200));
	}

}
