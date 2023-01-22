
public class CandidateNotFoundException extends Exception {

	//fields
	private String name;
	
	//constructor
	public CandidateNotFoundException(String name) {
		
		this.name = name;
	}
	
	/**
	 * get the name that is not found on the ballot
	 * @return the name that is not found on the ballot
	 */
	//getter
	public String getName() {
		
		return this.name;
	}
}
