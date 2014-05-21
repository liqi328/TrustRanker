package diseasesimilarity;


public class CombinedDiseaseDiseaseSimilarity implements DiseaseDiseaseSimilarity{
	private DiseaseDiseaseSimilarity firstDiseaseSimilarity;
	private DiseaseDiseaseSimilarity secondDiseaseSimilarity;
	private double alpha;
	
	public CombinedDiseaseDiseaseSimilarity(DiseaseDiseaseSimilarity firstDiseaseSimilarity,
			DiseaseDiseaseSimilarity secondDiseaseSimilarity, double alpha){
		this.firstDiseaseSimilarity = firstDiseaseSimilarity;
		this.secondDiseaseSimilarity = secondDiseaseSimilarity;
		this.alpha = alpha;
	}
	
	/*
	 * similarity = (1 - alpha) * firstDiseaseSimilarity + alpha * secondDiseaseSimilarity
	 * */
	@Override
	public double getTwoDiseaseSimilarity(String omimId1, String omimId2) {
		double similarity = (1 - alpha) * firstDiseaseSimilarity.getTwoDiseaseSimilarity(omimId1, omimId2);
		similarity += alpha * secondDiseaseSimilarity.getTwoDiseaseSimilarity(omimId1, omimId2);
		
		return  similarity;
	}

	@Override
	public void addDiseaseDiseaseSimilarity(
			DiseaseDiseaseSimilarity anotherDiseaseSimilarity, double alpha) {
		
	}

}
