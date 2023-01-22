import java.util.LinkedList;

class Programme {
	
	LinkedList<Movie> matinee;
	LinkedList<Movie> primetime;
	LinkedList<Movie> soiree;
	
	Programme()
	{
		this.matinee = new LinkedList<Movie>();
		this.primetime = new LinkedList<Movie>();
		this.soiree = new LinkedList<Movie>();
	}
	
	Programme(LinkedList<Movie> matinee, LinkedList<Movie> primetime, LinkedList<Movie> soiree)
	{
		this.matinee = matinee;
		this.primetime = primetime;
		this.soiree = soiree;
	}
	
	/**
	 * Compares two programmes to see if their size of the lists are equal
	 * @param aProgramme The programme you're comparing.
	 * @return True if the two programmes have equal size of the lists. False otherwise.
	 */
	public boolean checkPairSizeEqual(Programme aProgramme) {
		
		if (this.matinee.size() == aProgramme.matinee.size() &&
			this.primetime.size() == aProgramme.primetime.size() &&
			this.soiree.size() == aProgramme.soiree.size())
		{
			return true;
		}
		else {
			return false;
		}
		
	}
	
	/**
	 * Compares two programmes to see if they have same movies in same order
	 * @param aProgramme The programme you're comparing.
	 * @return True if the two programmes have same movies in same order. False otherwise.
	 */
	public boolean checkSameMovieSameOrder(Programme aProgramme) {
		
		//loop all the movies in all three lists
		for(int i = 0; i < aProgramme.matinee.size(); i++) {		
			if (!this.matinee.get(i).equals(aProgramme.matinee.get(i))) {
				return false;
			}
		}
		
		for(int i = 0; i < aProgramme.primetime.size(); i++) {		
			if (!this.primetime.get(i).equals(aProgramme.primetime.get(i))) {
				return false;
			}
		}
		
		for(int i = 0; i < aProgramme.soiree.size(); i++) {		
			if (!this.soiree.get(i).equals(aProgramme.soiree.get(i))) {
				return false;
			}
		}
		
		return true;	
	}
	
	//override
	/**
	 * Compares two programmes to see if they're equal by testing to see if the size, movies, order are equal
	 * @param obj The object you're comparing.
	 * @return True if the two programmes are equal. False otherwise.
	 */
	public boolean equals(Object obj) {
		//casting
		Programme aProgramme = (Programme) obj;
		
		if (this.checkPairSizeEqual(aProgramme)) {			
			return this.checkSameMovieSameOrder(aProgramme);
		}
		else {
			return false;
		}
	}


}