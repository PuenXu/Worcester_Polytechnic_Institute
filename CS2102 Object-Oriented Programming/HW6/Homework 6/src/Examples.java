import static org.junit.Assert.*;

import org.junit.Test;

public class Examples {

	//object for test cases
	VotingData vote1 = new VotingData();
	 
	//test pick winner most first choice
	@Test
	public void testMostFirstWinner1 () {
	  assertEquals ("gompei", vote1.setup1().pickWinnerMostFirstChoice());
	}
	
	//test pick winner most first choice, no winner
	@Test
	public void testMostFirstWinner2 () {
	  assertEquals ("*Requires Runoff Poll*", vote1.setup2().pickWinnerMostFirstChoice());
	}
	
	//test winner most agreeable
	@Test
	public void testWinnerMostAgreeable1 () {
	  assertEquals ("ziggy", vote1.setup1().pickWinnerMostAgreeable());
	}
	
	//test winner most agreeable, there is a tie
	@Test
	public void testWinnerMostAgreeable2 () {
	  assertEquals ("husky", vote1.setup2().pickWinnerMostAgreeable());
	}
	
	//test nominate candidate, gompei is already in the list
	@Test(expected=RedundantCandidateException.class)
	public void testNominateCandidate1() throws RedundantCandidateException
	{		
    	vote1.setup1().nominateCandidate("gompei");
	}
	
	//test submit vote, husky chosen more than once
	@Test(expected=CandidateChosenMoreThanOnceException.class)
	public void testSubmitVote1() throws CandidateChosenMoreThanOnceException
	{	
		try {
			vote1.setup1().submitVote("gompei", "husky", "husky");
		}
		catch(CandidateNotFoundException e) {
			
		}
	}
	
	//test submit vote, cuneo is not nominated
	@Test(expected=CandidateNotFoundException.class)
	public void testSubmitVote2() throws CandidateNotFoundException
	{	
		try {
			vote1.setup1().submitVote("gompei", "cuneo", "husky");
		}
		catch(CandidateChosenMoreThanOnceException e) {
			
		}
	}
	
	//test submit vote, cuneo is not nominated, and gompei chosen more than once
	@Test(expected=CandidateNotFoundException.class)
	public void testSubmitVote3() throws CandidateNotFoundException
	{	
		try {
			vote1.setup1().submitVote("gompei", "cuneo", "gompei");
		}
		catch(CandidateChosenMoreThanOnceException e) {
			
		}
	}
}
