package result;

public class Rank implements Comparable<Rank>{
	private String name;
	private Integer id;
	private double score;
	private int rank;

	public Rank(Integer id, double score){
		this(id, null, score, 0);
	}
	
	public Rank(Integer id, String name, double score, int rank){
		this.id = id;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Rank other) {
		// ½µÐò
		return Double.valueOf(other.score).compareTo(Double.valueOf(this.score));
	}
}
