package util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class WriterUtil {
	public static void write(String filename, String content){
		File file = new File(filename);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(content);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void write(String filename, Set<?> set){
		StringBuffer sb = new StringBuffer();
		Iterator<?> itr = set.iterator();
		while(itr.hasNext()){
			sb.append(itr.next()).append("\n");
		}
		
		WriterUtil.write(filename, sb.toString());
	}
	
//	public static String array2String(double[] rankScores, AdjacencyGraph g){
//		StringBuffer sb = new StringBuffer();
//		
//		for(int i = 0; i < rankScores.length; ++i){
//        	sb.append(g.getNodeName(i)).append("\t");
//        	sb.append(rankScores[i]).append("\n");
//        }
//		
//		return sb.toString();
//	}
}
