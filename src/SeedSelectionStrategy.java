import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public abstract class SeedSelectionStrategy {
	public abstract double[] selectSeeds(String goodSeedFilename, AdjacencyGraph g, int L);
	
	public static Set<String> readSeedFile(String goodSeedFilename){
		Set<String> goodSeedSet = new HashSet<String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(goodSeedFilename)));
			String line = null;
			
			while((line = in.readLine()) != null){
				goodSeedSet.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return goodSeedSet;
	}
}
