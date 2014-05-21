package diseasesimilarity;

public interface DiseaseDiseaseSimilarity {
	public double getTwoDiseaseSimilarity(String omimId1, String omimId2);
	
	/*
	 * similarity = (1 - alpha) * thisDiseaseSimilarity + alpha * anotherDiseaseSimilarity
	 * */
	public void addDiseaseDiseaseSimilarity(DiseaseDiseaseSimilarity anotherDiseaseSimilarity, double alpha);
}
