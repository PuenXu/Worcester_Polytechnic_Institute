
class Movie {

	String title;
	double showtime;
	int runtime;
	boolean simulcast;
	
	public Movie(String title, double showtime, int runtime, boolean simulcast)
	{
		this.title = title;
		this.showtime = showtime;
		this.runtime = runtime;
		this.simulcast = simulcast;
	}
	
	//override
	/**
	 * Compares two movies to see if they're equal by testing to see if the title and show time are equal
	 * @param obj The object you're comparing.
	 * @return True if the two movies are equal. False otherwise.
	 */
	public boolean equals(Object obj) {
		//casting
		Movie aMovie = (Movie) obj;
		
		if (this.title.equals(aMovie.title) &&
			this.showtime == aMovie.showtime)
		{
			return true;
		}
		else {
			return false;
		}
		

	}
	
}
