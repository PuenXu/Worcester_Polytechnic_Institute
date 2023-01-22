import java.util.LinkedList;

//the abstract class for poem result and writing result
public abstract class AbsResult  implements IChallenge{

	//fields declaration
	LinkedList<Double> wordsWritten = new LinkedList<Double>();
	int lastUpdatedDay;
	
	//Constructor
	
	public AbsResult(double initialWords, int lastUpdatedDay) {
		
		this.wordsWritten .add(initialWords);
		this.lastUpdatedDay = lastUpdatedDay;
	}
	
	//Methods
	
	/**
	 * Calculate the total words written in a reading or poem result
	 * @return the total words written
	 */
	
	public double totalWordsWritten() {
		
		double totalWordsWritten = 0;
		for (double words : this.wordsWritten) 
		{ 
		    totalWordsWritten += words;
		}
		return totalWordsWritten;
	}
	
	/**
	 * Calculate the words written per day
	 * @return the words written per day
	 */
	
	
	public double averagePerDay() {
		
		return this.totalWordsWritten() / this.lastUpdatedDay;
		
	}

}
