/**
 * A Poem Challenge
 */

public class PoemResult extends AbsResult{
	
	//fields declaration
	int weeksSubmitted;
	
	//Constructor 
	
	public PoemResult(double initialWords, int lastUpdatedDay, int weeksSubmitted) {
		
		super(initialWords, lastUpdatedDay);
		this.weeksSubmitted = weeksSubmitted;
	}
	
	/**
	 * Add a poem to the writing word list, and increase weeksSubmitted if a new poem is submitted
	 * @param poemWords the number of words of the chapter, newPoemSubmitted boolean indicating whether a new poem is submitted
	 * @return this itself after being modified
	 */
	
	//Methods
	
	public PoemResult nextPoem(double poemWords, boolean newPoemSubmitted) {
		
		this.wordsWritten.add(poemWords); 
		if(newPoemSubmitted) this.weeksSubmitted += 1;
		return this;
	}
	
	/**
	 * Calculate the difference from poem goal
	 * @return the difference from poem goal
	 */
	
	public double differenceFromGoal() {
		
		int poemSubmitted; 
		if(this.weeksSubmitted >= 1) {
			poemSubmitted = this.weeksSubmitted;
		}
		else {
			poemSubmitted = 1;  //if no poem submitted, set it to 1
		}
		
		int poemLeft = 4 - this.weeksSubmitted;
		
		int daysLeft = 31 - this.lastUpdatedDay + 1;
		
		return this.totalWordsWritten() / poemSubmitted * poemLeft / daysLeft;
	}

}
