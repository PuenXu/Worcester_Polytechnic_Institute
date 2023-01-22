
public class ReadingResult implements IChallenge {
	
	int booksPerDay;
	BooksRead fictionBook;
	BooksRead nonFictionBook;
	
	public ReadingResult(int booksPerDay, BooksRead fictionBook, BooksRead nonFictionBook) {
		this.booksPerDay = booksPerDay;
		this.fictionBook = fictionBook;
		this.nonFictionBook = nonFictionBook;
	}

	public double averagePerDay() {
		double totalBooksRead = this.fictionBook.booksRead + this.nonFictionBook.booksRead;
		double daysOctober = 31;
		return totalBooksRead / daysOctober;
	}
	
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

}
