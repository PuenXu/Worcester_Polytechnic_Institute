import static org.junit.Assert.*;

import org.junit.Test;

public class Examples {

    BooksRead booksRead1 = new BooksRead(1.0);
    BooksRead booksRead2 = new BooksRead(3.5);
    BooksRead booksRead3 = new BooksRead(15.0);
    BooksRead booksRead4 = new BooksRead(30.0);

    ReadingResult readingResult1 = new ReadingResult(2, booksRead1, booksRead2); //goal average books per day, fiction books read, non fiction books read.
    ReadingResult readingResult2 = new ReadingResult(1, booksRead1, booksRead4);
    ReadingResult readingResult3 = new ReadingResult(1, booksRead3, booksRead4);
    WritingResult writingResult1 = new WritingResult(25000.5, 15); //words written, last day novel was updated
    WritingResult writingResult2 = new WritingResult(30000.0, 15);
    
    ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
    ChallengeResult challengeResult2 = new ChallengeResult(readingResult2, writingResult2);
    
    Literarian literarian1 = new Literarian(challengeResult1);
    Literarian literarian2 = new Literarian(challengeResult2);
    
	@Test
	public void testAvgReadingPerDay() {
		assertEquals(4.5/31, readingResult1.averagePerDay(),0.1);
	}
	
	@Test
	public void testDifferenceFromReadingGoal1() {
		assertEquals(2 - 4.5/31, readingResult1.differenceFromGoal(),0.1);
	}
	
	//edge cases: read same amount of books per day as goal
	@Test
	public void testDifferenceFromReadingGoal2() {
		assertEquals(0, readingResult2.differenceFromGoal(),0.1);
	}
	
	//edge cases: read more books per day than goal
	@Test
	public void testDifferenceFromReadingGoal3() {
		assertEquals(0, readingResult3.differenceFromGoal(),0.1);
	}
	
	@Test
	public void testAvgWritingPerDay() {
		assertEquals(25000.5/15, writingResult1.averagePerDay(),0.1);
	}
	
	@Test
	public void testDifferenceFromWritingGoal() {
		assertEquals((50000-25000.5)/(30-15+1), writingResult1.differenceFromGoal(),0.1);
	}
	
	@Test
	public void testHowClose() {
		assertEquals((2 - 4.5/31)*10000 + (50000-25000.5)/(30-15+1), challengeResult1.howClose(),0.1);
	}
	
	@Test
	public void testBetterBookWorm() {
		assertFalse(literarian1.betterBookworm(literarian2));
	}
	
	@Test
	public void testWittierWordsmith() {
		assertFalse(literarian1.wittierWordsmith(literarian2));
	}
	
	@Test
	public void testBetterScholar() {
		assertFalse(literarian1.successfulScholar(literarian2));
	}

}
