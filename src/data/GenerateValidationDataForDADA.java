package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.FileUtil;
import util.WriterUtil;


/*
 * 产生交叉验证的输入数据：使用的候选基因是随机产生的。
 * */

public class GenerateValidationDataForDADA {
	
	public static void main(String[] args){
		String diseasenameFile = "E:/2013疾病研究/实验数据/TrustRanker/19diseasename.txt";
		
		StringBuffer configFilePathBuffer = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(diseasenameFile));
			String line = null;
			String seedFilename = null;
			
			while((line = in.readLine()) != null){
				seedFilename = "E:/2013疾病研究/实验数据/TrustRanker/19disease/" + line.split("\t")[0] + ".txt";
				List<String> seedList = readSeedList(seedFilename);
				
				configFilePathBuffer.append(generateValidationData(line.split("\t")[0], seedList));
			}
			
			System.out.println(configFilePathBuffer.toString());
			
			WriterUtil.write("E:/2013疾病研究/实验数据/TrustRanker/19disease/input/all_config.txt", configFilePathBuffer.toString());
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String generateValidationData(String diseaseName, List<String> seedList){
		StringBuffer configFilePathBuffer = new StringBuffer();
		String diseaseDir = "E:/2013疾病研究/实验数据/TrustRanker/19disease/input/" + diseaseName;
		FileUtil.makeDir(new File(diseaseDir));
		String targetDir = null;
		Set<String> seedSet = new HashSet<String>(seedList);
		for(String seed: seedList){
			seedSet.remove(seed);
			
			targetDir = diseaseDir + "/" + seed;
			FileUtil.makeDir(new File(targetDir));
			//System.out.println(targetDir);
			
			/* 生成configFile
			 * */
			StringBuffer configFileContent = new StringBuffer();
			configFileContent.append("ppiFilename=./input/HPRD_ppi.txt").append("\n");
			configFileContent.append("diseaseSeedFilename=./input/").append(diseaseName + "/" + seed + "/seedSet.txt").append("\n");
			configFileContent.append("outputdir=./output/").append(diseaseName + "_output/").append(seed).append("\n");
			
			WriterUtil.write(targetDir + "/config.txt", configFileContent.toString());
			//WriterUtil.write(targetDir + "/seedSet.txt", seedSet);
			writeSeedSet(targetDir + "/seedSet.txt", seedSet);
			
			configFilePathBuffer.append(targetDir + "/config.txt\n");
			
			seedSet.add(seed);
		}
		
		return configFilePathBuffer.toString();
	}
	
	private static void writeSeedSet(String filename, Set<String> seedSet){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			
			for(String seed : seedSet){
				out.write(seed + "\n");
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> readConfigFile(String configFilename) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(configFilename));
		
		Map<String, String> configMap = new HashMap<String, String>();
		String line = null;
		String[] cols = null;
		while((line = in.readLine()) != null){
			cols = line.split("=");
			configMap.put(cols[0], cols[1]);
		}
		in.close();
		return configMap;
	}
	
	public static List<String> readSeedList(String seedFilename) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(seedFilename));
		
		List<String> seedList = new ArrayList<String>();
		String line = null;
		while((line = in.readLine()) != null){
			seedList.add(line.split("\t")[1]);
		}
		in.close();
		return seedList;
	}		
	
}
