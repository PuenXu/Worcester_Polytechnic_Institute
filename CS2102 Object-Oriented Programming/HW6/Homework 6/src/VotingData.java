import java.util.LinkedList;
import java.util.Scanner;
import java.util.HashMap;

public class VotingData {

	//fields
	private LinkedList<String> candidateList;
	private HashMap<String, LinkedList<Integer>> votes;
	
	//constructor
	public VotingData() {
		
		this.candidateList = new LinkedList<String>();
		this.votes = new HashMap<String, LinkedList<Integer>>();
	}
	
	//private helper methods
	/**
	 * get the number of first place votes of a candidate
	 * @param candidate a candidate to find
	 * @return the number of first place votes of the candidate
	 */
	private int candidateFirstPlaceVote(String candidate) {
		return this.votes.get(candidate).get(0);
	}
	
	/**
	 * get the number of second place votes of a candidate
	 * @param candidate a candidate to find
	 * @return the number of second place votes of the candidate
	 */
	private int candidateSecondPlaceVote(String candidate) {
		return this.votes.get(candidate).get(1);
	}
	
	/**
	 * get the number of third place votes of a candidate
	 * @param candidate a candidate to find
	 * @return the number of third place votes of the candidate
	 */
	private int candidateThirdPlaceVote(String candidate) {
		return this.votes.get(candidate).get(2);
	}
	
	/**
	 * get the total number of first place votes in a ballot
	 * @return the total number of first place votes in a ballot
	 */
	private int totalFirstPlaceVotes() {
		
		int total = 0;
		
		//loop thru all the candidates
		for (String candidate: this.candidateList) {
			
			total += this.candidateFirstPlaceVote(candidate);
		}
		
		return total;
	}
	
	/**
	 * determine whether a candidate  has more than half of the total first place votes
	 * @param candidate a candidate to check
	 * @return true if a candidate  has more than half of the total first place votes, false otherwise
	 */
	private boolean hasMoreThanHalfFirstPlaceVotes(String candidate) {
		
		return this.candidateFirstPlaceVote(candidate) > this.totalFirstPlaceVotes() / 2;
	}
	
	/**
	 * get the candidate point of a candidate
	 * @param candidate a candidate to check
	 * @return the candidate point of a candidate
	 */
	private int candidatePoint(String candidate) {
		
		int pt = 0;
		
		if(this.candidateFirstPlaceVote(candidate) > pt) {
			
			pt = this.candidateFirstPlaceVote(candidate);
		}
		
		if(this.candidateSecondPlaceVote(candidate) > pt) {
			
			pt = this.candidateSecondPlaceVote(candidate);
		}
		
		if(this.candidateThirdPlaceVote(candidate) > pt) {
			
			pt = this.candidateThirdPlaceVote(candidate);
		}
		
		return pt;
	}
	
	//getter
	/**
	 * get the candidate list
	 * @return the candidate list
	 */
	public LinkedList<String> getCandidateList(){
		return this.candidateList;
	}
	
	/**
	 * let a user to submit 1st, 2nd, and 3rd place votes
	 * @param firstChoice the first place vote
	 * @param secondChoice the second place vote
	 * @param thirdChoice the third place vote
	 * @throws CandidateNotFoundException when a user to vote is not found in the ballot
	 * @throws CandidateChosenMoreThanOnceException when a user to vote is chosen more than once
	 */
	public void submitVote(String firstChoice, String secondChoice, String thirdChoice) throws CandidateNotFoundException, CandidateChosenMoreThanOnceException{
		
		//check if a candidate is not found
		if(!this.candidateList.contains(firstChoice)) {
			throw new CandidateNotFoundException(firstChoice);
		}
		else if(!this.candidateList.contains(secondChoice)) {
			throw new CandidateNotFoundException(secondChoice);
		}
		else if(!this.candidateList.contains(thirdChoice)) {
			throw new CandidateNotFoundException(thirdChoice);
		}
		
		//check if a candidate is chosen more than once
		else if(firstChoice.equals(secondChoice)) {
			throw new CandidateChosenMoreThanOnceException(firstChoice);
		}
		else if(firstChoice.equals(thirdChoice)) {
			throw new CandidateChosenMoreThanOnceException(firstChoice);
		}
		else if(secondChoice.equals(thirdChoice)) {
			throw new CandidateChosenMoreThanOnceException(secondChoice);
		}
		else {
			
			//get the votes for a candidate, and add the corresponding vote
			int first1 = candidateFirstPlaceVote(firstChoice) + 1;
			int first2 = candidateSecondPlaceVote(firstChoice);
			int first3 = candidateThirdPlaceVote(firstChoice);
			
			int second1 = candidateFirstPlaceVote(secondChoice);
			int second2 = candidateSecondPlaceVote(secondChoice) + 1;
			int second3 = candidateThirdPlaceVote(secondChoice);
			
			int third1 = candidateFirstPlaceVote(thirdChoice);
			int third2 = candidateSecondPlaceVote(thirdChoice);
			int third3 = candidateThirdPlaceVote(thirdChoice) + 1;
			
			//put the votes back into the vote hashmap of a candidate
			LinkedList<Integer> first = new LinkedList<Integer>();
			first.add(first1);
			first.add(first2);
			first.add(first3);
			
			LinkedList<Integer> second = new LinkedList<Integer>();
			second.add(second1);
			second.add(second2);
			second.add(second3);
			
			LinkedList<Integer> third = new LinkedList<Integer>();
			third.add(third1);
			third.add(third2);
			third.add(third3);
			
			this.votes.put(firstChoice, first);
			this.votes.put(secondChoice, second);
			this.votes.put(thirdChoice, third);
		}
	}
	
	/**
	 * to nominate a candidate
	 * @param name the name of a candidate
	 * @throws RedundantCandidateException when the candidate to nominate is already nominated
	 */
	public void nominateCandidate(String name) throws RedundantCandidateException {
		
		//check if it is redundant
		if(!this.candidateList.contains(name)) {
			
	    	LinkedList<Integer> numOfVotes = new LinkedList<Integer>();
	    	numOfVotes.add(0);  //num of 1st choice votes
	    	numOfVotes.add(0);  //num of 2nd choice votes
	    	numOfVotes.add(0);  //num of 3rd choice votes
	    	
	    	this.candidateList.add(name);  //add the name to the list of candidate
			this.votes.put(name, numOfVotes);  //add the number of votes into the hashmap
		}
		else {
			
			throw new RedundantCandidateException(name);
		}
	}
	
	/**
	 * determine if there is a winner and return the name of the winner if there is one
	 * @return the name of the winner if there is a winner, otherwise "*Requires Runoff Poll*"
	 */
	public String pickWinnerMostFirstChoice() {
		
		for(String candidate: this.candidateList) {
			
			if(this.hasMoreThanHalfFirstPlaceVotes(candidate)) {
				return candidate;
			}
		}
		
		return "*Requires Runoff Poll*";
	}
	
	/**
	 * return the name of the winner
	 * @return the name of the winner
	 */
	public String pickWinnerMostAgreeable() {
		
		int maxPtSoFar = 0;
		String winner = "";
		
		
		for(String candidate: this.candidateList) {
			
			if(this.candidatePoint(candidate) > maxPtSoFar) {
				
				maxPtSoFar = this.candidatePoint(candidate);
				winner = candidate;
			}
		}
		
		return winner;
	}
	
	// method to set up a ballot and cast votes for test cases
	/**
	 * set up a voting data object
	 * @return a voting data object after set up
	 */
	public VotingData setup1() {

	    VotingData vd = new VotingData();
	  
	    // put candidates on the ballot
	    try {

	       vd.nominateCandidate("gompei");
	       vd.nominateCandidate("husky");
	       vd.nominateCandidate("ziggy");

	    } catch (Exception e) {}

	    // cast votes

	    try {

	      vd.submitVote("gompei", "husky", "ziggy");
	      vd.submitVote("gompei", "husky", "ziggy");
	      vd.submitVote("husky", "gompei", "ziggy");

	    } catch (Exception e) {}

	    return(vd);

	  }
	
	/**
	 * set up a voting data object
	 * @return a voting data object after set up
	 */
	public VotingData setup2() {

	    VotingData vd = new VotingData();
	  
	    // put candidates on the ballot
	    try {

	       vd.nominateCandidate("gompei");
	       vd.nominateCandidate("husky");
	       vd.nominateCandidate("ziggy");

	    } catch (Exception e) {}

	    // cast votes

	    try {

	      vd.submitVote("gompei", "husky", "ziggy");
	      vd.submitVote("ziggy", "husky", "gompei");
	      vd.submitVote("husky", "gompei", "ziggy");

	    } catch (Exception e) {}

	    return(vd);

	  }

}
