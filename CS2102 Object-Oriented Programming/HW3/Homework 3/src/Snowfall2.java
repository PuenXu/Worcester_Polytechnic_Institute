import java.util.LinkedList;

class Snowfall2 {
  
	Snowfall2(){}

	/**
	 * Determine whether a reading is a date or not
	 * @param anum a reading to be determined
	 * @return true if it is a date, false otherwise
	 */
	// checks whether a datum is a date
	boolean isDate(double anum) { 
		
		return (int)anum >= 101; 
	} // Jan 1st
  
	/**
	 * To extract the month from a date
	 * @param dateNum a date
	 * @return the month
	 */
	// extracts the month from an 4-digit date
	int extractMonth(double dateNum) { 
		
		return ((int)( dateNum / 100)); 
	}
 
	
	/**
	 * To consume a list of data and generate a list of data excluding negative values
	 * @param data a list of data
	 * @return the list of cleaned data
	 */
	//cleaning
	public LinkedList<Double> cleanData(LinkedList<Double> data){
		
		LinkedList<Double> cleanedList = new LinkedList<Double>();  //create an empty list for accumulating
		
		for(double datum : data) {   //add only dates and valid readings to the cleaned list
			if(datum >= 0 && datum <= 99) {  
				cleanedList.add(datum);
			}
			else if(this.isDate(datum)) {
				cleanedList.add(datum);
			}
		}
		
		return cleanedList;
	}
	
	/**
	 * To generate a list of max inch reports according to the list of data
	 * @param data a list of data
	 * @return the list of max inch reports
	 */
	//parsing
	public LinkedList<MaxInchReport> dailyMax(LinkedList<Double> data){
		
		LinkedList<MaxInchReport> dailyMax = new LinkedList<MaxInchReport>();  //create an empty list for accumulating
		LinkedList<Double> cleanedData = this.cleanData(data);  //clean the data
		
		int reportNum = -1;  //the index of the list of max inch reports, initially -1 so that when it proceeds, it will be in correct order
		
		for (int i = 0; i < cleanedData.size(); i++) {  //loop through the list of data
			if (this.isDate(cleanedData.get(i))){   //check whether it is a date
				dailyMax.add(new MaxInchReport(cleanedData.get(i), 0));   
				//if it is a date, create a new max inch report with the ith data as the date and 0
				//using 0 as the length parameter, so that if all readings are negative, then it will still be 0
				reportNum ++;  //proceed the index of the max inch report
			}
			else {  
				//we don't need to check whether it is negative or invalid because they are already eliminated during cleaning
				if(cleanedData.get(i) > dailyMax.get(reportNum).maxInches) {   //check if the reading is greater than the previous max reading
					dailyMax.get(reportNum).maxInches = cleanedData.get(i);  //if it is, then replace the value with this reading
				}
			}
		}
		
		return dailyMax;
	}
	
	/**
	 * To generate a list of max inch reports according to the list of data and a month
	 * @param data a list of data
	 * @return the list of max inch reports
	 */
	//parsing
	public LinkedList<MaxInchReport> dailyMaxForMonth(LinkedList<Double> data, int month) {
	  
		LinkedList<MaxInchReport> dailyMax = this.dailyMax(data);  //generate a list of max inch reports of all months
		LinkedList<MaxInchReport> dailyMaxForMonth = new LinkedList<MaxInchReport>();   //create an empty list for accumulating
		
		for (MaxInchReport report : dailyMax) {  //loop through all the reports
			if (this.extractMonth(report.date) == month) {   //if the month of the report is equal to the input month, add it to the list
				dailyMaxForMonth.add(report);
			}
		}
		return dailyMaxForMonth;
	}
}   

