package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * 110��Disease-Families
 * */
public class ExtractDisease {
	public static void main(String[] args){
		String diseaseFilePath = "E:/2013�����о�/��������/OMIM/110DiseaseFamily/110diseaseFamily.txt";
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(diseaseFilePath)));
			String line = null;
			String[] cols = null;
			int i = 0;
			while((line = in.readLine()) != null){
				if(line.startsWith("[")){
					//System.out.println(++i + ". " + line);
				}
				cols = line.split("\t");
				if(cols.length < 5 && cols.length > 1){
					System.out.println(++i + ". " + line);
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
