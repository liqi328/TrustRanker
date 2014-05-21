package diseasesimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class DiseaseDiseasePhenotypeSimilarity implements DiseaseDiseaseSimilarity{
	private class Row{
		public double[] row;
		Row(int length){
			row = new double[length];
		}
	}
	private Map<String, Integer> omimId2IndexMap;
	private Map<Integer, String> index2OmimIdMap;
	private Map<Integer, Row> rowsMap;
	
	public DiseaseDiseasePhenotypeSimilarity(){
		read();
	}
	
	public DiseaseDiseasePhenotypeSimilarity(String similarityFilepath){
		read(similarityFilepath);
	}
	
	public double getTwoDiseaseSimilarity(String omimId1, String omimId2){
		Integer i = omimId2IndexMap.get(omimId1);
		Integer j = omimId2IndexMap.get(omimId2);
		
		if(i == null || j == null){
			return 0.0;
		}
		
		return rowsMap.get(i).row[j];
	}
	
	/*
	 * similarity = (1 - alpha) * pheotypeSimilarity + alpha * toplogySimilarity
	 * */
	public void addDiseaseDiseaseSimilarity(DiseaseDiseaseSimilarity anotherDiseaseSimilarity, double alpha){
		double pSimilarity = 0.0;
		for(int i = 0; i < omimId2IndexMap.size(); ++i){
			for(int j = i+1; j < omimId2IndexMap.size(); ++j){
				pSimilarity = anotherDiseaseSimilarity.getTwoDiseaseSimilarity(index2OmimIdMap.get(i), index2OmimIdMap.get(j));
				rowsMap.get(i).row[j] = (1.0 - alpha) * rowsMap.get(i).row[j] + alpha * pSimilarity;
				rowsMap.get(j).row[i] = rowsMap.get(i).row[j];
			}
		}
	}
	
	private void read(){
		String similarityFilepath = "E:/2013疾病研究/疾病数据/Disease-DiseaseSimilarity/disease_disease_similarity.tsv";
		
		read(similarityFilepath);
	}
	
	private void read(String similarityFilepath){
		omimId2IndexMap = new HashMap<String, Integer>();
		index2OmimIdMap = new HashMap<Integer, String>();
		rowsMap = new HashMap<Integer, Row>();
		int index = 0;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(similarityFilepath)));
			String line = null;
			String[] cols = null;
			Row row = null;
			while((line = in.readLine()) != null){
				cols = line.split("\t");
				omimId2IndexMap.put(cols[0], index);
				index2OmimIdMap.put(index, cols[0]);
				
				if(cols.length != 5081){
					System.out.println(cols.length);
				}
				
				row = new Row(cols.length - 1);
				rowsMap.put(index, row);
				for(int i = 1; i < cols.length; ++i){
					row.row[i - 1] = Double.parseDouble(cols[i]);
				}
				
				++index;
			}
			//System.out.println(rowsMap.size());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		DiseaseDiseasePhenotypeSimilarity similarity = new DiseaseDiseasePhenotypeSimilarity();
		Runtime s_runtime = Runtime.getRuntime();
		
		System.out.println((s_runtime.totalMemory() - s_runtime.freeMemory()) / (1024*1024));
		
		String omimId1 = "114550";
		String omimId2 = "114500";
		
		System.out.println(similarity.getTwoDiseaseSimilarity(omimId1, omimId2));
		System.out.println(similarity.getTwoDiseaseSimilarity("100675", "100050"));
	}
}
