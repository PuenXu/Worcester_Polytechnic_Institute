
public class ChallengeResult {
	
	ReadingResult readingResult;
	WritingResult writingResult;
	
	public ChallengeResult(ReadingResult readingResult, WritingResult writingResult) {
		this.readingResult = readingResult;
		this.writingResult = writingResult;
		
	}
	
	public double howClose() {
		return this.readingResult.differenceFromGoal() * 10000 + this.writingResult.differenceFromGoal();
	}
	

}
