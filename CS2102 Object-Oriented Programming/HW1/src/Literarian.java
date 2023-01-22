
public class Literarian {
	
	ChallengeResult challengeResult;
	
	public Literarian(ChallengeResult challengeResult) {
		this.challengeResult = challengeResult;
	}
	
	public boolean betterBookworm(Literarian literarian) {
		if (this.challengeResult.readingResult.differenceFromGoal() < literarian.challengeResult.readingResult.differenceFromGoal()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean wittierWordsmith(Literarian literarian) {
		if (this.challengeResult.writingResult.averagePerDay() > literarian.challengeResult.writingResult.averagePerDay()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean successfulScholar(Literarian literarian) {
		if(this.betterBookworm(literarian) || this.wittierWordsmith(literarian)) {
			return true;
		}
		else {
			return false;
		}
	}

}
