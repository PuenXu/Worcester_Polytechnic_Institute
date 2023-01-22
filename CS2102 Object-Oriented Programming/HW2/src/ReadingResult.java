import java.util.LinkedList;

public class ReadingResult implements IChallenge {
	
	//fields declaration
	int booksPerDay;
	LinkedList<BooksRead> booksReadList = new LinkedList<BooksRead>();
	
	//Constructor
	public ReadingResult(int booksPerDay, BooksRead fiction, BooksRead nonFiction) {
		this.booksPerDay = booksPerDay;
		this.booksReadList.add(fiction);
		this.booksReadList.add(nonFiction);
	}
	
	//Overloading, to test edge cases
	public ReadingResult(int booksPerDay) {
		this.booksPerDay = booksPerDay;
	}

	//Methods
	
	/**
	 * Add a book to the books read list
	 * @param booksRead1 a book to add
	 * @return this itself after being modified
	 */
	
	public ReadingResult readSomeBooks(BooksRead booksRead1) {
		
		this.booksReadList.add(booksRead1);
		return this;
	}
	
	/**
	 * To calculate the total books read in a reading result
	 * @return total number of books read
	 */
	
	public double totalBooksRead() {
		
		double totalBooksRead = 0;
		for (BooksRead books: this.booksReadList) 
		{ 
			totalBooksRead += books.booksRead;
		}
		return totalBooksRead;
	}
	
	/**
	 * To calculate the average books read per day
	 * @return average number of books read per day
	 */
	
	public double averagePerDay() {
		
		double totalBooksRead = this.totalBooksRead();
		double daysOctober = 31;
		return totalBooksRead / daysOctober;
		
	}
	
	/**
	 * Calculate the difference from reading goal
	 * @return the difference from reading goal
	 */
	
	public double differenceFromGoal() {
		
		double difference;
		if (this.booksPerDay - this.averagePerDay() >= 0) {
			difference = this.booksPerDay - this.averagePerDay();
		}
		else {
			difference = 0;
		}
		return difference;
		
	}
	
	
	/**
	 * Filter our the best genre in a reading result
	 * @param investedReading a boolean indicating whether to consider only invested reading
	 * @return bestSoFar a string of the best genre
	 */
	
	public String bestGenreByType (boolean investedReading) {
		
		String bestSoFar = ""; //the string of the best genre
		double highestNumSoFar = 0; //the highest number of books read
		boolean investedAtAll = false; //whether there is a book in the list that is not skimmed
		
		if (!this.booksReadList.isEmpty()) {  //first check if list is empty
		if (investedReading) {  //when the boolean parameter is true
			for (BooksRead books: this.booksReadList) //then check if there is a book in the list that is not skimmed
			{ 
				if (!books.skimmedOrNot) {
					investedAtAll = true;
				}
			}
			if (investedAtAll) {  //if there is book that is not skimmed, then search for best genre
				for (BooksRead books: this.booksReadList) 
				{ 
					if (!books.skimmedOrNot && books.booksRead > highestNumSoFar) {
						highestNumSoFar = books.booksRead;
						bestSoFar = books.genre;
					}
				}
			}
			else {  //if all books skimmed, return null
				return null;
			}
			
		}
		else {  //when the boolean parameter is false
			for (BooksRead books: this.booksReadList) 
			{ 
				if (books.booksRead > highestNumSoFar) {
					highestNumSoFar = books.booksRead;
					bestSoFar = books.genre;
				}
			}
	
		}
	}
	else {
		return null; //empty list, return null
	}
		
		return bestSoFar;
	}

	/**
	 * To calculate the books read score of a reading result
	 * @return the books read score of a reading result
	 */
	
	public int booksReadScore(){
		
		int score = 0; 
		
		for (BooksRead books: this.booksReadList) {
			if (!books.skimmedOrNot) {  //check whether book is skimmed
				score += (int) ((5 * books.booksRead));
			}
		}
		
		return score;
	}
}
