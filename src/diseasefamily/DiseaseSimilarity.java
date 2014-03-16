package diseasefamily;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiseaseSimilarity {
	private class Row{
		public double[] row;
		Row(int length){
			row = new double[length];
		}
	}
	private Map<String, Integer> omimId2IndexMap;
	private Map<Integer, Row> rowsMap;
	
	
	public double getTwoDiseaseSimilarity(String omimId1, String omimId2){
		Integer i = omimId2IndexMap.get(omimId1);
		Integer j = omimId2IndexMap.get(omimId2);
		
		if(i == null || j == null){
			return 0.0;
		}
		
		return rowsMap.get(i).row[j];
	}
	
	public void load(){
		String similarityFilepath = "E:/2013疾病研究/疾病数据/Disease-DiseaseSimilarity/PhenSim.tsv";
		
		load(similarityFilepath);
	}
	
	public void load(String similarityFilepath){
		omimId2IndexMap = new HashMap<String, Integer>();
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
		DiseaseSimilarity similarity = new DiseaseSimilarity();
		similarity.load();
		
		String omimId1 = "114480";
		String omimId2 = "600185";
		
		System.out.println(similarity.getTwoDiseaseSimilarity(omimId1, omimId2));
		System.out.println(similarity.getTwoDiseaseSimilarity("114480", "113300"));
	}
}
