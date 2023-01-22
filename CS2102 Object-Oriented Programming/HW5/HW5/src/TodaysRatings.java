
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class TodaysRatings {

	//fields
	private GregorianCalendar date;
	private LinkedList<Integer> listOfRankings;
	private LinkedList<Integer> listOfDownloads;
	
	//constructor
	public TodaysRatings(GregorianCalendar date) {
		
		this.date = date;
		this.listOfRankings = new LinkedList<Integer>();
		this.listOfDownloads = new LinkedList<Integer>();
	}
	
	//constructor overload
	public TodaysRatings(GregorianCalendar date, LinkedList<Integer> listOfRankings, LinkedList<Integer> listOfDownloads) {
		
		this.date = date;
		this.listOfRankings = listOfRankings;
		this.listOfDownloads = listOfDownloads;
	}
	
	//equals method override, for test cases
	/**
	 * Compares two todaysRatings to see if the list of rankings and downloads and date are equal
	 * @param obj The object you're comparing.
	 * @return True if the two todaysRatings are equal. False otherwise.
	 */
	public boolean equals(Object aObj) {
		
		TodaysRatings aRatings = (TodaysRatings) aObj;
		
		return this.date.equals(aRatings.date) &&
			   this.listOfRankings.equals(aRatings.listOfRankings) &&
			   this.listOfDownloads.equals(aRatings.listOfDownloads);
	}
	
	/**
	 * Add a list of surveys to a todaysRating object
	 * @param listOfSurveys a list of survey to gather
	 * @return updated todaysRating object
	 */
	public TodaysRatings gatherSurveys(LinkedList<Survey> listOfSurveys) {
		
		for(Survey aSurvey : listOfSurveys) {
			
			this.listOfRankings.add(aSurvey.getRanking());
			this.listOfDownloads.add(aSurvey.getNumOfDownloads());
		}
		
		return this;
	}
	
	/**
	 * Determine whether the date of the object is within given month
	 * @param month, year input month and year
	 * @return true if it is within given month, false otherwise
	 */
	public boolean withinMonth(int month, int year) {
		return this.date.get(GregorianCalendar.MONTH) == month &&
			   this.date.get(GregorianCalendar.YEAR) == year;
	}
	
	/**
	 * Produce the total song downloads today
	 * @return total song downloads today
	 */
	public int totalDownloadsToday() {
		
		int downloads = 0;
		
		for(Integer a : this.listOfDownloads) {
			
			downloads+= a;
		}
		
		return downloads;
	}
	
	/**
	 * Produce the best ranking today
	 * @return best rank today
	 */
	public int bestRankToday() {
		
		int bestRank = this.listOfRankings.get(0);
		
		for(Integer a : this.listOfRankings) {
			if(a < bestRank) {
				bestRank = a;
			}
		}

		return bestRank;
	}
}

