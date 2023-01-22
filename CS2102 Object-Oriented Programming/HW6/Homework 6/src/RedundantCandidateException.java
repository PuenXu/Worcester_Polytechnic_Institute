
public class RedundantCandidateException extends Exception{

	//fields
	private String name;
	
	//constructor
	public RedundantCandidateException(String name) {
		
		this.name = name;
	}
}
