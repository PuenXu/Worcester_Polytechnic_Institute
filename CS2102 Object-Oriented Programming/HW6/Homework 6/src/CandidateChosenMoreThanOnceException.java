
public class CandidateChosenMoreThanOnceException extends Exception{

	//fields
	private String name;
	
	//constructor
	public CandidateChosenMoreThanOnceException(String name) {
		
		this.name = name;
	}
	
	/**
	 * get the name that is chose more than once
	 * @return the name that is chose more than once
	 */
	//getter method
	public String getName(){
		
		return this.name;
	}
}
