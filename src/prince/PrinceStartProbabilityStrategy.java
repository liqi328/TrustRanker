package prince;

import graph.AdjacencyGraph;
import diseasefamily.Disorder;
import diseasefamily.Gene;
import diseasefamily.GeneDiseaseAssociation;
import diseasesimilarity.DiseaseDiseasePhenotypeSimilarity;
import diseasesimilarity.DiseaseDiseaseSimilarity;

public class PrinceStartProbabilityStrategy{
	private AdjacencyGraph g;
	private DiseaseDiseaseSimilarity diseaseSimilarity;
	private GeneDiseaseAssociation associations;
	
	public PrinceStartProbabilityStrategy(AdjacencyGraph g, DiseaseDiseaseSimilarity diseaseSimilarity,
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
	 * ����һ���뼲��p��صĻ���,��query����q, ����p��q�������� 
	 * @param geneNameInPPI		gene������,��PPI��gene�ı�ʾһ��. ��PPI�еĻ�����HPRD id��ʾ����˴���geneNameҲ��HPRD id
	 * @param diseaseOmimId		query������OMIM id
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
