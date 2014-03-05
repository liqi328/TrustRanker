

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GraphReader {
	
	public static AdjacencyGraph read(String filename, AdjacencyGraph g){
		//AdjacencyGraph g = new AdjacencyGraph();
		try {
			readNodes(filename, g);
			readEdges(filename, g);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return g;
	}
	
	private static void readNodes(String filename, AdjacencyGraph g) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
		String line = null;
		String[] cols = null;
		while((line = in.readLine()) != null){
			cols = line.split(" |\t|,");
			g.addNode(cols[0].trim());
			g.addNode(cols[1].trim());
		}
		
		in.close();
	}
	
	private static void readEdges(String filename, AdjacencyGraph g) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
		String line = null;
		String[] cols = null;
		double w = 1.0;
		while((line = in.readLine()) != null){
			cols = line.split(" |\t|,");
			/* È¥³ý×Ô»· */
			if(cols[0].equals(cols[1])){
				continue;
			}
			if(cols.length > 2){
				w = Double.parseDouble(cols[2]);
			}
			g.addEdge(cols[0].trim(), cols[1].trim(), w);
		}
		
		in.close();
	}

}
