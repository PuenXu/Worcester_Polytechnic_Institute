import java.util.LinkedList;

public class LitJam {

	//fields declaration
	int numOfGenres;
	LinkedList<Literarian> literarianList = new LinkedList<Literarian>();
	
	//Constructor
	
	public LitJam(int numOfGenres, LinkedList<Literarian> literarianList) {
		this.numOfGenres = numOfGenres;
		this.literarianList = literarianList;
	}
	
	//Methods
	
	/**
	 * To create a list of the names of literarians who did not finish reading
	 * @return nameList the list of the names of literarians
	 */
	
	public LinkedList<String> readingDNF(){
		
		LinkedList<String> nameList = new LinkedList<String>();  //initiate an empty list
		
		for (Literarian literarian: this.literarianList) 
		{ 
			if(literarian.challengeResult.readingResult.booksReadList.size() < this.numOfGenres) {
				nameList.add(literarian.penName);  //add the name to the list if true
			}
		}
		
		return nameList;
	}
	

	/**
	 * To calculate the final score of a literarian in a litjam
	 * @param penName the name of the literarian
	 * @return finalScore the final score of the literarian
	 */
	
	public int finalScoreForLiterarian(String penName) {
		
		int finalScore = 0; //initiate final score variable
		
		for (Literarian literarian: this.literarianList) 
		{ 
			if(literarian.penName == penName) {
				if(literarian.challengeResult.readingResult.booksReadList.size() >= this.numOfGenres) finalScore += 25;
				finalScore += literarian.challengeResult.readingResult.booksReadScore();  //use helper function to calculate the score for reading books
				if(literarian.challengeResult.readingResult.differenceFromGoal() == 0) finalScore += 5;
				if(literarian.challengeResult.writingResult.totalWordsWritten() >= 50000) {
					finalScore += 25;
					finalScore += 5 * (30 - literarian.challengeResult.writingResult.lastUpdatedDay);
				}
					
				
			}
		}
		
		return finalScore;
		
	}
	
	/**
	 * To check if a literarian has improvement than him/herself in the other litjam
	 * @param litJam1 the other litjam to comapre with
	 * @return true if the literarian improved, false otherwise
	 */
	
	public boolean anyImprovement(LitJam litJam1) {
		
		for (Literarian literarian: this.literarianList) //use a nested for loop to compare all the literarians
		{ 
			for (Literarian literarianToCompare: litJam1.literarianList) {
				if (literarian.penName.equals(literarianToCompare.penName)){
					if(this.finalScoreForLiterarian(literarian.penName) > litJam1.finalScoreForLiterarian(literarian.penName)) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}
	
}

// 13.
/*
 * I could have written a helper function to check whether the reading result meets the genre requirement by 
 * comparing the size of the booksRead list to the number of genres required. Also, I could have developed a 
 * helper function to return a boolean if the total words written in a writing result is greater than or equal
 * to 50,000.
 */
