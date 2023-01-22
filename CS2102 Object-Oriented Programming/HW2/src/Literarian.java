
public class Literarian {
	
	//fields declaration
	String penName;
	ChallengeResult challengeResult;
	
	//Constructor
	
	public Literarian(String penName, ChallengeResult challengeResult) {
		this.penName = penName;
		this.challengeResult = challengeResult;
	}
	
	//Methods
	
	/**
	 * Check if the difference from reading goal of this is lower of higher than the other literarian
	 * @param literarian the other literarian to compare with
	 * @return true if the difference from reading goal of this is lower, false otherwise
	 */
	
	public boolean betterBookworm(Literarian literarian) {
		if (this.challengeResult.readingResult.differenceFromGoal() < literarian.challengeResult.readingResult.differenceFromGoal()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the difference from writing goal of this is lower of higher than the other literarian
	 * @param literarian the other literarian to compare with
	 * @return true if the difference from writing goal of this is lower, false otherwise
	 */
	
	
	public boolean wittierWordsmith(Literarian literarian) {
		if (this.challengeResult.writingResult.averagePerDay() > literarian.challengeResult.writingResult.averagePerDay()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if the literarian is a better book worm or wittier wordsmith than the other literarian
	 * @param literarian the other literarian to compare with
	 * @return true if the literarian is either a better book worm or wittier wordsmith
	 */
	
	
	public boolean successfulScholar(Literarian literarian) {
		if(this.betterBookworm(literarian) || this.wittierWordsmith(literarian)) {
			return true;
		}
		else {
			return false;
		}
	}

}
