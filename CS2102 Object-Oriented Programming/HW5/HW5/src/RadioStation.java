
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class RadioStation {

	//fields
	private ISet setOfTodaysRatings;
	private GregorianCalendar date;
	
	//constructor
	public RadioStation(ISet setOfTodaysRatings, GregorianCalendar date) {
		this.setOfTodaysRatings = setOfTodaysRatings;
		this.date = date;
	}
	
	//equals method override, for test cases
	/**
	 * Compares two radio stations to see if the set of ratings and date are equal
	 * @param obj The object you're comparing.
	 * @return True if the two radio station are equal. False otherwise.
	 */
	public boolean equals(Object aObj) {
		
		RadioStation aStation = (RadioStation) aObj;
		
		return this.setOfTodaysRatings.equals(aStation.setOfTodaysRatings) &&
			   this.date.equals(aStation.date);
	}
	
	/**
	 * Produce the number of total song downloads within the given month
	 * @param month, year input month and year
	 * @return total song downloads
	 */
	public int totalSongDownloads(int month, int year) {
		
		return this.setOfTodaysRatings.totalSongDownloads(month, year);
	}
	
	/**
	 * Gather a list of survey and update the radio station
	 * @param listOfSurveys a list of surveys
	 * @return the updated radio station
	 */
	public RadioStation addTodaysSurveys(LinkedList<Survey> listOfSurveys) {
		
	    int year = this.date.get(GregorianCalendar.YEAR);
	    int month = this.date.get(GregorianCalendar.MONTH);
	    int day = this.date.get(GregorianCalendar.DAY_OF_MONTH);
	    
		GregorianCalendar oldDate = new GregorianCalendar(year, month, day); 		
		TodaysRatings todaysRatings = new TodaysRatings(oldDate);
		todaysRatings.gatherSurveys(listOfSurveys);		
		this.setOfTodaysRatings.add(todaysRatings);
		
		GregorianCalendar newDate = new GregorianCalendar(year, month, day); 		
		newDate.add(GregorianCalendar.DAY_OF_YEAR, 1);
		this.date = newDate;

		return this;
	}
	
	/**
	 * Produce the best rank in this month
	 * @return the best rank
	 */
	public int bestRankThisMonth() {
		
		int month = this.date.get(GregorianCalendar.MONTH);
		int year = this.date.get(GregorianCalendar.YEAR);
		
		return this.setOfTodaysRatings.bestRankByMonth(month, year);
	}
}

//Answers:
/*
	1. We can use an array list as an alternative data structure. This is because dates are in numerical order. If we use an array list, 
	   a todaysRatings object can be easily found by the date. Let's say list[0]'s date is 2022/12/01, then if we want to find the todays 
	   ratings object whose date is 2022/12/05, then it should be at list[4].
	2. If I use an array list. I just need to create methods that functions as it were a linked list. Let's say, for a linkedlist class, if I
	   need to add an object, I use this.linkedList.add(object). For an arraylist class, we can do the same like this.arrayList.add(object). 
	   For total downloads and best rank function, we can also loop through the list to get the corresponding value like what we did for linked list.
	3. Yes, we just need to make the new class implement the methods from the interface. And write the method body in the new class
	   to make it do the same thing the other class do, just like what is explained in question 2. Then in the example or main class, 
	   all we need to do is to construct a radio station object, which uses the new class.
*/
