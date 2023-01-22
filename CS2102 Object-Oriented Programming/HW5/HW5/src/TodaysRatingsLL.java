
import java.util.LinkedList;

public class TodaysRatingsLL implements ISet{

	//fields
	private LinkedList<TodaysRatings> listOfRatings = new LinkedList<TodaysRatings>();
	
	//constructor
	public TodaysRatingsLL (LinkedList<TodaysRatings> listOfRatings) {
		this.listOfRatings = listOfRatings;
	}
	
	//equals method override, for test cases
	/**
	 * Compares two todays ratings linked list to see if they are equal
	 * @param obj The object you're comparing.
	 * @return True if the two linked list are equal. False otherwise.
	 */
	public boolean equals(Object obj) {
		
		TodaysRatingsLL aLL = (TodaysRatingsLL) obj;
		
		return this.listOfRatings.equals(aLL.listOfRatings);
	}
	
	/**
	 * Add a todaysRating to the list
	 * @param aRatings a rating to add
	 * @return updated linked list
	 */
	public TodaysRatingsLL add(TodaysRatings aRatings){
		
		this.listOfRatings.add(aRatings);
		
		return this;
	}
	
	/**
	 * Find and produce a list of ratings within given month
	 * @param month, year input month and year
	 * @return a list of ratings within given month
	 */
	public LinkedList<TodaysRatings> findRatings(int month, int year){
	
	LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();
	
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
		
		LinkedList<TodaysRatings> aList = this.findRatings(month, year);
		
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
		
		LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();
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
