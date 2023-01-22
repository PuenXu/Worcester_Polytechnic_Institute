
import java.util.ArrayList;

public class TodaysRatingsAL implements ISet{

	//fields
	private ArrayList<TodaysRatings> listOfRatings = new ArrayList<TodaysRatings>();
	
	//constructor
	public TodaysRatingsAL (ArrayList<TodaysRatings> listOfRatings) {
		this.listOfRatings = listOfRatings;
	}
	
	//equals method override, for test cases
	/**
	 * Compares two todays ratings linked list to see if they are equal
	 * @param obj The object you're comparing.
	 * @return True if the two linked list are equal. False otherwise.
	 */
	public boolean equals(Object obj) {
		
		TodaysRatingsAL aLL = (TodaysRatingsAL) obj;
		
		return this.listOfRatings.equals(aLL.listOfRatings);
	}
	
	/**
	 * Add a todaysRating to the list
	 * @param aRatings a rating to add
	 * @return updated linked list
	 */
	public TodaysRatingsAL add(TodaysRatings aRatings){
		
		this.listOfRatings.add(aRatings);
		
		return this;
	}
	
	/**
	 * Find and produce a list of ratings within given month
	 * @param month, year input month and year
	 * @return a list of ratings within given month
	 */
	public ArrayList<TodaysRatings> findRatings(int month, int year){
	
	ArrayList<TodaysRatings> aList = new ArrayList<TodaysRatings>();
	
	for(TodaysRatings aRatings : this.listOfRatings) {
		if(aRatings.withinMonth(month, year)) {
			aList.add(aRatings);
		}
	}
	
	return aList;
	}
	
	/**
	 * Produce the number of total song downloads within the given month
	 * @param month, year input month and year
	 * @return total song downloads
	 */
	public int totalSongDownloads(int month, int year) {
		
		ArrayList<TodaysRatings> aList = this.findRatings(month, year);
		
		int totalDownloads = 0;
		
		for(TodaysRatings aRatings : aList) {
			
			totalDownloads += aRatings.totalDownloadsToday();
		}
		
		return totalDownloads;
	}
	
	/**
	 * Produce the best rank in this month
	 * @param month, year input month and year
	 * @return the best rank
	 */
	public int bestRankByMonth(int month, int year) {
		
		int smallestRank ;
		
		ArrayList<TodaysRatings> aList = new ArrayList<TodaysRatings>();
		aList = this.findRatings(month,year);
		
		if(!aList.isEmpty()) {
			
			smallestRank = aList.get(0).bestRankToday();
			
			for(TodaysRatings aRatings : aList) {
				
				if(aRatings.bestRankToday() < smallestRank) {
					smallestRank = aRatings.bestRankToday();
				}
			}
		}
		else {
			
			//if empty list, produce -1
			smallestRank = -1;
		}
		
		return smallestRank;
	}
}
