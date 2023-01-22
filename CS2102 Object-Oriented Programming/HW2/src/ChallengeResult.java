
public class ChallengeResult {
	
	//fields declaration
	ReadingResult readingResult;
	WritingResult writingResult;
	
	//Constructor
	public ChallengeResult(ReadingResult readingResult, WritingResult writingResult) {
		this.readingResult = readingResult;
		this.writingResult = writingResult;
		
	}
	
	//Methods
	
	/**
	 * Calculate the difference from the reading and writing goal
	 * @return the difference from the reading and writing goal
	 */
	
	public double howClose() {
		return this.readingResult.differenceFromGoal() * 10000 + this.writingResult.differenceFromGoal();
	}
	

}
