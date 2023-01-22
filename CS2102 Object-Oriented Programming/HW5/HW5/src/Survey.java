
public class Survey {

	//fields
	private int ranking;
	private int numOfDownloads;
	
	//constructor
	public Survey(int ranking, int numOfDownloads) {
		
		this.ranking = ranking;
		this.numOfDownloads = numOfDownloads;
	}
	
	/**
	 * Get the ranking of the survey
	 * @return ranking of the survey
	 */
	public int getRanking() {
		
		return this.ranking;
	}
	
	/**
	 * Get the number of downloads of the survey
	 * @return number of downloads
	 */
	public int getNumOfDownloads() {
		
		return this.numOfDownloads;
	}
}
