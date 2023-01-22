
class MaxInchReport {
  
	double date;
	double maxInches;
	
	MaxInchReport(double date, double maxInches) {
	    
		this.date = date;
	    this.maxInches = maxInches;
	}
	
	/**
	 * Test whether two max inch reports are equal to each other
	 * @param other the other max inch report to compare with
	 * @return true if they are equal, false otherwise
	 */
	// the equals method for use in testing
	public boolean equals(Object other) 
	{
		if(!(other instanceof MaxInchReport))
		{
			return false;
		}
			
		MaxInchReport otherR = (MaxInchReport) other;
			
		return otherR.date == this.date && otherR.maxInches == this.maxInches;
		}
	  
	/**
	 * Display a max inch report
	 * @return a string display of the report
	 */
	// the toString method so that reports display when tests fail
	public String toString()
	{
		return "Snow | " + date + ": " + maxInches + "\"";
	}
}
