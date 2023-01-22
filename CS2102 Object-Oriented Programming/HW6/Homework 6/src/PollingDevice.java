
import java.util.Scanner;

public class PollingDevice {

	//fields
	private Scanner keyboard = new Scanner(System.in);
	private VotingData data = new VotingData();
	
	/**
	 * print the names of the candidates on the ballot
	 */
	public void printBallot() {
  
		System.out.println("The candidates are ");
		for (String s : this.data.getCandidateList()) {
			System.out.println(s);
		}
	}

	/**
	 * ask the user to nominate or vote for candidates
	 */
	public void screen() {
		
		this.printBallot();
		
		System.out.println("Do you want to vote or nominate? Enter v or V to vote. Enter n or N to nominate. Enter any other key to exit.");
		
		String voteOrNominate = keyboard.next();
		
		//ask the user to chose whether to vote or nominate, v or V for vote
		if(voteOrNominate.equals("v") || voteOrNominate.equals("V")) {
			
			//record the 1st, 2nd, and third vote respectively
			System.out.println("Who do you want to vote for? Please enter you first, second, and third vote, respectively");
			
			String candidate1 = keyboard.next();
			String candidate2 = keyboard.next();
			String candidate3 = keyboard.next();
			
			try {
				
				//submit vote
				this.data.submitVote(candidate1, candidate2, candidate3);
				
				System.out.println("You have successfully voted. Enter y or Y to continue. Enter any other key to exit.");
				
				String ifContinue = keyboard.next();
				
				//ask user if he/she wants to continue
				if(ifContinue.equals("y") || ifContinue.equals("Y")) {
					
					this.screen();
				}
				
				else {
					System.out.println("You have exited.");
					return;
				}
			}
			
			//catch exception
			catch(CandidateNotFoundException e) {
				
				String candidate = e.getName();
				System.out.print(candidate);
				System.out.println(" is not found.");
				System.out.println("Do you want to add the candidate's name to the ballot? Enter y or Y to add. Enter any other key to exit.");
				
				String choice = keyboard.next();
				
				if(choice.equals("y") || choice.equals("Y")) {
					
					this.addWriteIn(candidate);
					this.screen();
				}
				
				else {
					System.out.println("You have exited.");
					return;
				}
			}
			
			//catch exception
			catch(CandidateChosenMoreThanOnceException e) {
				
				String candidate = e.getName();
				System.out.print(candidate);
				System.out.println(" is chosen more than once.");				
				System.out.println("You cannot vote for the same candidate more than once.");
				this.screen();  //restart process
			}
		}
		
		//ask the user to chose whether to vote or nominate, n or N for vote
		else if(voteOrNominate.equals("n") || voteOrNominate.equals("N")) {
			
			System.out.println("Who do you want to nominate?");
			
			String nomination = keyboard.next();
			
			this.addWriteIn(nomination);
			
			System.out.println("Enter y or Y to continue. Enter any other key to exit.");
			
			String continueOrNot = keyboard.next();
			
			if(continueOrNot.equals("y") || continueOrNot.equals("Y")) {
				
				this.screen();
			}
			
			else {
				System.out.println("You have exited.");
				return;
			}
		}
		
		//ask the user to chose whether to vote or nominate, other keys to exit
		else {
			System.out.println("You have exited.");
			return;
		}

		
	}
	
	/**
	 * nominate an input candidate
	 * @param candidate a candidate to be nominated
	 */
	public void addWriteIn(String candidate) {
		try {
			this.data.nominateCandidate(candidate);
			System.out.println("The candidate is successfully added.");
		}
		catch(RedundantCandidateException e) {
			System.out.println("The user that you nominated already exists.");
		}
	}
}
