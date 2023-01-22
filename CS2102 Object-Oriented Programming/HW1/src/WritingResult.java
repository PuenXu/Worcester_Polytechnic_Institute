
public class WritingResult implements IChallenge{
	
	double wordsWritten;
	int lastUpdatedDay;
	
	public WritingResult(double wordsWritten, int lastUpdatedDay) {
		this.wordsWritten = wordsWritten;
		this.lastUpdatedDay = lastUpdatedDay;
	}
	
	public double averagePerDay() {
		double wordsPerDay = this.wordsWritten / this.lastUpdatedDay;
		return wordsPerDay;
		
	}
	public double differenceFromGoal() {
		int daysLeft = 30 - this.lastUpdatedDay + 1;
		return (50000 - this.wordsWritten) / daysLeft;
	}

}
