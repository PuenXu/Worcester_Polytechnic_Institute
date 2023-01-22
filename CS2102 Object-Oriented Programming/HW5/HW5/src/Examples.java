import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class Examples {

	//objects for test cases
	Survey survey1 = new Survey(2,10);
	Survey survey2 = new Survey(5,7);
	Survey survey3 = new Survey(1,18);
	
	Survey survey4 = new Survey(1,19);
	Survey survey5 = new Survey(4,12);
	
	LinkedList<Survey> surveys = new LinkedList<Survey>();
	LinkedList<Survey> surveys2 = new LinkedList<Survey>();
	
	GregorianCalendar date1 = new GregorianCalendar(2016, 11, 17);
	GregorianCalendar date2 = new GregorianCalendar(2016, 11, 18);
	GregorianCalendar date3 = new GregorianCalendar(2016, 11, 19);
	
	LinkedList<Integer> listOfRankings1 = new LinkedList<Integer>();
	LinkedList<Integer> listOfDownloads1 = new LinkedList<Integer>();
	LinkedList<Integer> listOfRankings2 = new LinkedList<Integer>();
	LinkedList<Integer> listOfDownloads2 = new LinkedList<Integer>();
	
	LinkedList<TodaysRatings> list1 = new LinkedList<TodaysRatings>();
	
	TodaysRatings todaysRatings1 = new TodaysRatings(date1);
	
	TodaysRatingsLL LL1 = new TodaysRatingsLL(new LinkedList<TodaysRatings>());
	
	RadioStation radioStation1 = new RadioStation(new TodaysRatingsLL(new LinkedList<TodaysRatings>()), date1);
		
	//Array version of implement
	ArrayList<TodaysRatings> list2 = new ArrayList<TodaysRatings>();
	RadioStation radioStation1AL = new RadioStation(new TodaysRatingsAL(new ArrayList<TodaysRatings>()), date1);
	
	public Examples(){
		
		listOfRankings1.add(1);
		listOfRankings1.add(4);
		listOfDownloads1.add(19);
		listOfDownloads1.add(12);
		
		listOfRankings2.add(2);
		listOfRankings2.add(5);
		listOfRankings2.add(1);
		listOfDownloads2.add(10);
		listOfDownloads2.add(7);
		listOfDownloads2.add(18);
		
		surveys.add(survey1);
		surveys.add(survey2);
		surveys.add(survey3);
		
		surveys2.add(survey4);
		surveys2.add(survey5);

	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//tests for add today's ratings
	
	//test add today's ratings, initially empty
	@Test
	public void testAddTodaysRatings() {
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 17);
		GregorianCalendar date2 = new GregorianCalendar(2016, 11, 18);
		
		TodaysRatings todaysRatings2 = new TodaysRatings(date1);
		todaysRatings2.gatherSurveys(surveys);
		
		LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();		
		aList.add(todaysRatings2);
		
		TodaysRatingsLL LL1 = new TodaysRatingsLL(new LinkedList<TodaysRatings>());
		TodaysRatingsLL LL2 = new TodaysRatingsLL(aList);
		
		RadioStation radioStation1 = new RadioStation(LL1, date1);
		RadioStation expectedRadioStation = new RadioStation(LL2, date2);
		
		assertEquals(radioStation1.addTodaysSurveys(surveys), expectedRadioStation);
	}
	
	//test add today's ratings, initially non-empty
	@Test
	public void testAddTodaysRatings2() {
		
		TodaysRatings todaysRatings1 = new TodaysRatings(date1, listOfRankings1,listOfDownloads1);
		TodaysRatings todaysRatings2 = new TodaysRatings(date2, listOfRankings2,listOfDownloads2);
		
		LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();		
		aList.add(todaysRatings1);
		
		LinkedList<TodaysRatings> aList2 = new LinkedList<TodaysRatings>();		
		aList2.add(todaysRatings1);
		aList2.add(todaysRatings2);
		
		TodaysRatingsLL LL1 = new TodaysRatingsLL(aList);
		TodaysRatingsLL LL2 = new TodaysRatingsLL(aList2);
		
		RadioStation radioStation2 = new RadioStation(LL1,date2);
		RadioStation expectedRadioStation = new RadioStation(LL2, date3);
		
		radioStation2.addTodaysSurveys(surveys);
		
		assertEquals(radioStation2, expectedRadioStation);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//tests for total song downloads
	
	//add empty surveys to the radio station
	@Test
	public void testTotalDownloads() {
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 1);
		
		RadioStation radioStation1 = new RadioStation(new TodaysRatingsLL(new LinkedList<TodaysRatings>()), date1);
		
		for (int i = 0; i < 30; i++) {
			
			radioStation1.addTodaysSurveys(new LinkedList<Survey>());
		}
		
		assertEquals(radioStation1.totalSongDownloads(11, 2016), 0);
	}
	
	//add same surveys, all of them in input month
	@Test
	public void testTotalDownloads2() {
		
		Survey survey1 = new Survey(2,10);
		Survey survey2 = new Survey(5,15);
		
		LinkedList<Survey> surveys = new LinkedList<Survey>();
		surveys.add(survey1);
		surveys.add(survey2);
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 1);
		
		RadioStation radioStation1 = new RadioStation(new TodaysRatingsLL(new LinkedList<TodaysRatings>()), date1);
		
		for (int i = 0; i < 30; i++) {
			
			radioStation1.addTodaysSurveys(surveys);
		}
		
		assertEquals(radioStation1.totalSongDownloads(11, 2016), 750);
	}
	
	//add same surveys, some of them not in input month
	@Test
	public void testTotalDownloads3() {
		
		Survey survey1 = new Survey(2,10);
		Survey survey2 = new Survey(5,15);
		
		LinkedList<Survey> surveys = new LinkedList<Survey>();
		surveys.add(survey1);
		surveys.add(survey2);
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 1);
		GregorianCalendar date2 = new GregorianCalendar(2015, 11, 1);
		GregorianCalendar date3 = new GregorianCalendar(2016, 1, 1);
		GregorianCalendar date4 = new GregorianCalendar(2016, 11, 2);
		
		TodaysRatings todaysRatings1 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings2 = new TodaysRatings(date2, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings3 = new TodaysRatings(date3, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings4 = new TodaysRatings(date4, listOfRankings1, listOfDownloads1);
		
		LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();
		aList.add(todaysRatings1);
		aList.add(todaysRatings2);
		aList.add(todaysRatings3);
		aList.add(todaysRatings4);
		
		TodaysRatingsLL LL1 = new TodaysRatingsLL(aList);
		
		RadioStation radioStation1 = new RadioStation(LL1, date4);
		
		for (int i = 0; i < 29; i++) {
			
			radioStation1.addTodaysSurveys(surveys);
		}
		
		assertEquals(radioStation1.totalSongDownloads(11, 2016), 787);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//tests for best rank this month
	
	//test if it produce -1 if the input is an empty list
	@Test
	public void testBestRankThisMonth() {
		
		assertEquals(radioStation1.bestRankThisMonth(), -1);
	}
	
	//test the best rank this month
	@Test
	public void testBestRankThisMonth2() {
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 1);
		GregorianCalendar date2 = new GregorianCalendar(2015, 11, 1);
		GregorianCalendar date3 = new GregorianCalendar(2016, 1, 1);
		GregorianCalendar date4 = new GregorianCalendar(2016, 11, 2);
		
		LinkedList<Integer> listOfRankings1 = new LinkedList<Integer>();
		listOfRankings1.add(13);
		listOfRankings1.add(2);		
		LinkedList<Integer> listOfRankings2 = new LinkedList<Integer>();
		listOfRankings2.add(3);
		listOfRankings2.add(5);
		LinkedList<Integer> listOfRankings3 = new LinkedList<Integer>();
		listOfRankings3.add(7);
		listOfRankings3.add(5);
		LinkedList<Integer> listOfRankings4 = new LinkedList<Integer>();
		listOfRankings4.add(13);
		listOfRankings4.add(25);
		
		TodaysRatings todaysRatings1 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings2 = new TodaysRatings(date2, listOfRankings2, listOfDownloads1);
		TodaysRatings todaysRatings3 = new TodaysRatings(date3, listOfRankings3, listOfDownloads1);
		TodaysRatings todaysRatings4 = new TodaysRatings(date4, listOfRankings4, listOfDownloads1);
		
		LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();
		aList.add(todaysRatings1);
		aList.add(todaysRatings2);
		aList.add(todaysRatings3);
		aList.add(todaysRatings4);
		
		TodaysRatingsLL LL1 = new TodaysRatingsLL(aList);
		
		RadioStation radioStation1 = new RadioStation(LL1, date4);

		assertEquals(radioStation1.bestRankThisMonth(), 2);
	}
	
	//test the best rank this month
	@Test
	public void testBestRankThisMonth3() {
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 1);
		GregorianCalendar date2 = new GregorianCalendar(2017, 11, 1);
		GregorianCalendar date3 = new GregorianCalendar(2016, 1, 1);
		GregorianCalendar date4 = new GregorianCalendar(2016, 11, 2);
		
		LinkedList<Integer> listOfRankings1 = new LinkedList<Integer>();
		listOfRankings1.add(13);
		listOfRankings1.add(2);		
		LinkedList<Integer> listOfRankings2 = new LinkedList<Integer>();
		listOfRankings2.add(3);
		listOfRankings2.add(5);
		LinkedList<Integer> listOfRankings3 = new LinkedList<Integer>();
		listOfRankings3.add(7);
		listOfRankings3.add(5);
		LinkedList<Integer> listOfRankings4 = new LinkedList<Integer>();
		listOfRankings4.add(13);
		listOfRankings4.add(25);
		
		TodaysRatings todaysRatings1 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings2 = new TodaysRatings(date2, listOfRankings2, listOfDownloads1);
		TodaysRatings todaysRatings3 = new TodaysRatings(date3, listOfRankings3, listOfDownloads1);
		TodaysRatings todaysRatings4 = new TodaysRatings(date4, listOfRankings4, listOfDownloads1);
		
		LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();
		aList.add(todaysRatings1);
		aList.add(todaysRatings2);
		aList.add(todaysRatings3);
		aList.add(todaysRatings4);
		
		TodaysRatingsLL LL1 = new TodaysRatingsLL(aList);
		RadioStation radioStation1 = new RadioStation(LL1, date2);

		assertEquals(radioStation1.bestRankThisMonth(), 3);
	}
	
	//test the best rank this month
	@Test
	public void testBestRankThisMonth4() {
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 1);
		GregorianCalendar date2 = new GregorianCalendar(2017, 11, 1);
		GregorianCalendar date3 = new GregorianCalendar(2016, 1, 1);
		GregorianCalendar date4 = new GregorianCalendar(2016, 11, 2);
		
		LinkedList<Integer> listOfRankings1 = new LinkedList<Integer>();
		listOfRankings1.add(13);
		listOfRankings1.add(7);		
		LinkedList<Integer> listOfRankings2 = new LinkedList<Integer>();
		listOfRankings2.add(3);
		listOfRankings2.add(5);
		LinkedList<Integer> listOfRankings3 = new LinkedList<Integer>();
		listOfRankings3.add(7);
		listOfRankings3.add(5);
		LinkedList<Integer> listOfRankings4 = new LinkedList<Integer>();
		listOfRankings4.add(4);
		listOfRankings4.add(25);
		
		TodaysRatings todaysRatings1 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings2 = new TodaysRatings(date2, listOfRankings2, listOfDownloads1);
		TodaysRatings todaysRatings3 = new TodaysRatings(date3, listOfRankings3, listOfDownloads1);
		TodaysRatings todaysRatings4 = new TodaysRatings(date4, listOfRankings4, listOfDownloads1);
		
		LinkedList<TodaysRatings> aList = new LinkedList<TodaysRatings>();
		aList.add(todaysRatings1);
		aList.add(todaysRatings2);
		aList.add(todaysRatings3);
		aList.add(todaysRatings4);
		
		TodaysRatingsLL LL1 = new TodaysRatingsLL(aList);
		
		RadioStation radioStation1 = new RadioStation(LL1, date4);

		assertEquals(radioStation1.bestRankThisMonth(), 4);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	//Array version of implementation
	//add empty surveys to the radio station
	@Test
	public void testTotalDownloadsAL() {
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 1);
		
		RadioStation radioStation1 = new RadioStation(new TodaysRatingsAL(new ArrayList<TodaysRatings>()), date1);
		
		for (int i = 0; i < 30; i++) {
			
			radioStation1.addTodaysSurveys(new LinkedList<Survey>());
		}
		
		assertEquals(radioStation1.totalSongDownloads(11, 2016), 0);
	}
	
	//test if it produce -1 if the input is an empty array list
	@Test
	public void testBestRankThisMonthAL() {
		
		assertEquals(radioStation1AL.bestRankThisMonth(), -1);
	}
	
	//test add today's ratings, initially empty
	@Test
	public void testAddTodaysRatingsAL() {
		
		GregorianCalendar date1 = new GregorianCalendar(2016, 11, 17);
		GregorianCalendar date2 = new GregorianCalendar(2016, 11, 18);
		
		TodaysRatings todaysRatings2 = new TodaysRatings(date1);
		todaysRatings2.gatherSurveys(surveys);
		
		ArrayList<TodaysRatings> aList = new ArrayList<TodaysRatings>();		
		aList.add(todaysRatings2);
		
		TodaysRatingsAL AL1 = new TodaysRatingsAL(new ArrayList<TodaysRatings>());
		TodaysRatingsAL AL2 = new TodaysRatingsAL(aList);
		
		RadioStation radioStation1 = new RadioStation(AL1, date1);
		RadioStation expectedRadioStation = new RadioStation(AL2, date2);
		
		assertEquals(radioStation1.addTodaysSurveys(surveys), expectedRadioStation);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//tests for helper methods
	
	//test whether it gathers surveys to into an todaysRatings object
	@Test
	public void testGatherSurveys() {
		
		TodaysRatings expectedRatings = new TodaysRatings(date1, listOfRankings2, listOfDownloads2);
		
		assertEquals(todaysRatings1.gatherSurveys(surveys),expectedRatings);
	}
	
	//test whether a todaysRatings object's date is within given month
	@Test
	public void testDateWithinMonth() {
		
		assertTrue(todaysRatings1.withinMonth(11,2016));
	}
	
	//test whether a todaysRatings object's date is within given month, incorrect month
	@Test
	public void testDateWithinMonth2() {
		
		date1 = new GregorianCalendar(2016, 12, 17);
		TodaysRatings todaysRatings1 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		
		assertFalse(todaysRatings1.withinMonth(11,2016));
	}
	
	//test whether a todaysRatings object's date is within given month, incorrect year
	@Test
	public void testDateWithinMonth3() {
		
		date1 = new GregorianCalendar(2017, 12, 17);
		TodaysRatings todaysRatings1 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		
		assertFalse(todaysRatings1.withinMonth(12,2016));
	}
	
	//test if two todaysRatings object are equal based on the override equals method
	@Test
	public void testTodaysRatingsEqauls() {
		
		GregorianCalendar date3 = new GregorianCalendar(2016, 11, 17);

		TodaysRatings todaysRatings2 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings3 = new TodaysRatings(date3, listOfRankings1, listOfDownloads1);
		
		assertEquals(todaysRatings2,todaysRatings3);
	}
	
	//test if two todaysRatings object are equal based on the override equals method, wrong date
	@Test
	public void testTodaysRatingsEquals2() {
		
		GregorianCalendar date3 = new GregorianCalendar(2016, 11, 18);

		TodaysRatings todaysRatings2 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings3 = new TodaysRatings(date3, listOfRankings1, listOfDownloads1);
		
		assertFalse(todaysRatings2.equals(todaysRatings3));
	}
	
	//test if two todaysRatings object are equal based on the override equals method, wrong lists
	@Test
	public void testTodaysRatingsEquals3() {
		
		GregorianCalendar date3 = new GregorianCalendar(2016, 11, 17);

		TodaysRatings todaysRatings2 = new TodaysRatings(date1, listOfRankings1, listOfDownloads1);
		TodaysRatings todaysRatings3 = new TodaysRatings(date3, listOfRankings2, listOfDownloads1);
		
		assertFalse(todaysRatings2.equals(todaysRatings3));
	}
	
	//test if two radioStation object are equal based on the override equals method
	@Test
	public void testRadioStationEquals() {
		
		GregorianCalendar date2 = new GregorianCalendar(2016, 11, 17);
		
		RadioStation radioStation1 = new RadioStation(new TodaysRatingsLL(new LinkedList<TodaysRatings>()), date1);
		RadioStation radioStation2 = new RadioStation(new TodaysRatingsLL(new LinkedList<TodaysRatings>()), date2);
			
		assertTrue(radioStation1.equals(radioStation2));
	}
	
	//test if two radioStation object are equal based on the override equals method, wrong list
	@Test
	public void testRadioStationEquals2() {
		
		LL1.add(todaysRatings1);
		RadioStation radioStation1 = new RadioStation(new TodaysRatingsLL(new LinkedList<TodaysRatings>()), date1);
		RadioStation radioStation2 = new RadioStation(LL1, date1);
			
		assertFalse(radioStation1.equals(radioStation2));
	}
	
}

