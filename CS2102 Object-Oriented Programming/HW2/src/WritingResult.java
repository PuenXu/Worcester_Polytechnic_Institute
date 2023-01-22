public class WritingResult extends AbsResult{
	
	//Constructor
	public WritingResult(double initialWords, int lastUpdatedDay) {
		
		super(initialWords, lastUpdatedDay);
	}
	
	//Methods
	
	/**
	 * Add a chapter to the writing word list
	 * @param chapterWords the number of words of the chapter
	 * @return this itself after being modified
	 */
	
	public WritingResult nextChapter(double chapterWords) {
		
		this.wordsWritten.add(chapterWords);
		return this;
	}
	
	/**
	 * Calculate the difference from reading goal
	 * @return the difference from reading goal
	 */
	
	public double differenceFromGoal() {
		
		int daysLeft = 30 - this.lastUpdatedDay + 1;
		return (50000 - this.totalWordsWritten()) / daysLeft;
	}
	

}