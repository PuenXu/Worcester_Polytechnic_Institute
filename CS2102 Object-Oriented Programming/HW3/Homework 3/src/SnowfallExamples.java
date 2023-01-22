import static org.junit.Assert.*;

import org.junit.Test;

import java.util.LinkedList;

public class SnowfallExamples {
	
	Snowfall1 E1 = new Snowfall1();
	Snowfall2 E2 = new Snowfall2();
	LinkedList<Double> noData = new LinkedList<Double>();
	LinkedList<Double> twoDates = new LinkedList<Double>();
	LinkedList<Double> threeDates = new LinkedList<Double>();  
	LinkedList<MaxInchReport> DecReports = new LinkedList<MaxInchReport>();
	LinkedList<MaxInchReport> NovReports = new LinkedList<MaxInchReport>();
	LinkedList<MaxInchReport> reports = new LinkedList<MaxInchReport>();
  
	public SnowfallExamples() {
		threeDates.add(1113.0);
		threeDates.add(10.0);
		threeDates.add(5.0);
		threeDates.add(1120.0);
    	threeDates.add(4.0);
    	threeDates.add(5.0);
	    threeDates.add(4.5);
	    threeDates.add(1201.0);
	    threeDates.add(6.0);
	    threeDates.add(-0.5);
	    DecReports.add(new MaxInchReport(1201.0,6.0));
		NovReports.add(new MaxInchReport(1113.0,10.0));
		NovReports.add(new MaxInchReport(1120.0,5.0));
	  }
	
	@Test
	public void testDailyMax1() { 
		
		reports.add(new MaxInchReport(1113.0,10.0));
		reports.add(new MaxInchReport(1120.0,5.0));
		reports.add(new MaxInchReport(1201.0,6.0));
		assertEquals(reports, E2.dailyMax(threeDates));
	}
	
	//empty list
	@Test
	public void testDailyMax2() { 

		assertEquals(reports, E2.dailyMax(noData));
	}
	
	@Test
	public void instructorTestEQ() { 
		assertEquals(DecReports, E2.dailyMaxForMonth(threeDates, 12));
	}
	
	//test when the input month is November
	@Test
	public void testDailyMaxForMonth1() { 
		
		assertEquals(NovReports, E2.dailyMaxForMonth(threeDates, 11));
	}
	
	//empty list
	@Test
	public void testDailyMaxForMonth2() { 

		assertEquals(reports, E2.dailyMaxForMonth(noData,11));
	}
	
	//test only negative readings in one day
	@Test
	public void testDailyMaxForMonth3() { 

		twoDates.add(1113.0);
		twoDates.add(10.0);
		twoDates.add(5.0);
	    twoDates.add(1201.0);  //this date, only negative readings
	    twoDates.add(-6.0);
	    twoDates.add(-0.5);
	    
	    reports.add(new MaxInchReport(1201.0,0));  //only negative readings, then max is 0
	    
		assertEquals(reports, E2.dailyMaxForMonth(twoDates,12));
	}

}

/*
 * Subtasks in problem 2
 * 
 *   - Determine if the data is a date or reading
 *   - Clear error readings
 *   - Select the readings according to the input month parameter
 *   - arrange the date and the corresponding reading into the max inch report
 *   - loop through all the data to create the list of reports
 *   - return the list
 */ 
