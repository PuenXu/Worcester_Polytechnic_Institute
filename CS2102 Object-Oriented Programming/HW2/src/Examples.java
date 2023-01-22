import static org.junit.Assert.*;

import org.junit.Test;

import java.util.LinkedList;

public class Examples {
	
	//instantiating objects for test cases
	WritingResult writingResult1 = new WritingResult (6000, 10);
	
	PoemResult poemResult1 = new PoemResult (500, 10, 2);
	
    BooksRead booksRead1 = new BooksRead(2.0, "fiction",true);
    BooksRead booksRead2 = new BooksRead(3.0, "non-fiction", true);
	BooksRead booksRead3 = new BooksRead (5.0,"sci-fi",false);

    ReadingResult readingResult1 = new ReadingResult(2, booksRead1, booksRead2); 
    ReadingResult readingResult2 = new ReadingResult(2, null, null); 
    
	ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
	
    Literarian literarian1 = new Literarian("Litty", challengeResult1);
    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
    LinkedList<Literarian> lits = new LinkedList<Literarian>();
	
    //testing total words written for writing result
	@Test
	public void testWritingResultTotalWordsWritten1() {

		assertEquals(writingResult1.totalWordsWritten(), 6000,0.1);
	}
	
    //testing total words written for writing result with nextChapter method applied
	@Test
	public void testWritingResultTotalWordsWritten2() {

		writingResult1.nextChapter(4000);
		
		assertEquals(writingResult1.totalWordsWritten(), 10000,0.1);
	}
	
	//testing average per day for writing result
	@Test
	public void testWritingResultAvgPerDay1() {

		assertEquals(writingResult1.averagePerDay(), 600,0.1);
	}
	
	//testing total words written for poem result
	@Test
	public void testPoemResultTotalWordsWritten1() {

		assertEquals(poemResult1.totalWordsWritten(), 500,0.1);
	}
	
    //testing total words written for poem result with nextPoem method applied
	@Test
	public void testPoemResultTotalWordsWritten2() {
	
		poemResult1.nextPoem(450,false);

		assertEquals(poemResult1.totalWordsWritten(), 950,0.1);
	}
	
	//testing if no new poem is submitted, the weeksSubmitted stays the same or not
	@Test
	public void testPoemResultTotalWordsWritten3() {
		
		poemResult1.nextPoem(450,false);
		assertEquals(poemResult1.weeksSubmitted, 2, 0.1);
	}
	
	//testing if new poem is submitted, the weeksSubmitted increases by 1
	@Test
	public void testPoemResultTotalWordsWritten4() {
		
		poemResult1.nextPoem(450,true);
		assertEquals(poemResult1.weeksSubmitted, 3, 0.1);
	}
	
	//testing average per day for poem result
	@Test
	public void testPoemResultAvgPerDay1() {

		assertEquals(poemResult1.averagePerDay(), 50,0.1);
	}
	
	//testing difference from goal for poem result
	@Test
	public void testPoemResultDiffFromGoal1() {

		assertEquals(poemResult1.differenceFromGoal(), 500.0 / 22.0 , 0.01);
	}
	
	//testing total books read in a reading result
	@Test
	public void testTotalBooksRead1() {
		
		assertEquals(readingResult1.totalBooksRead(), 5 ,0.1);
	}
	
	//testing total books read in a reading result with readSomeBooks method applied
	@Test
	public void testTotalBooksRead2() {

		readingResult1.readSomeBooks(booksRead3);
		
		assertEquals(readingResult1.totalBooksRead(), 10, 0.1);
	}
	
	//testing average books per day for a reading result
	@Test
	public void testAvgBooksPerDay1() {
		
		assertEquals(readingResult1.averagePerDay(), 5.0/31.0 ,0.1);
	}
	
	//testing best genre by type when the boolean parameter is false
	@Test
	public void testBestGenre1() {
		
		BooksRead techManuals = new BooksRead(1, "technical manuals", false);
		readingResult1 = readingResult1.readSomeBooks(techManuals);
		String s = readingResult1.bestGenreByType(false);
		assertEquals(s, "non-fiction");
	}
	
	//testing best genre by type when the boolean parameter is true
	@Test
	public void testBestGenre2() {
		
		BooksRead techManuals = new BooksRead(1, "technical manuals", false);
		readingResult1 = readingResult1.readSomeBooks(techManuals);
		String s = readingResult1.bestGenreByType(true);
		assertEquals(s, "technical manuals");
	}
	
	//testing best genre by type when the boolean is true and no books in the list is not skimmed
	@Test
	public void testBestGenre3() {
		
		BooksRead techManuals = new BooksRead(1, "technical manuals", true);
		readingResult1 = readingResult1.readSomeBooks(techManuals);
		String s = readingResult1.bestGenreByType(true);
		assertEquals(s, null);
	}
	
	//testing best genre by type when the list is empty
	@Test
	public void testBestGenre4() {

		ReadingResult readingResult2 = new ReadingResult(2);
		String s = readingResult2.bestGenreByType(false);
		assertEquals(s, null);
	}
	
	//testing reading DNF
	@Test
	public void testReadingDNF1() {
		
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(3,lits);
        LinkedList<String> names = litJam.readingDNF();
        LinkedList<String> testNames = new LinkedList<String>();
        testNames.add("Litty");
        testNames.add("Reeds");
        assertEquals(names, testNames);
	}
	
	//testing reading DNF
	@Test
	public void testReadingDNF2() {
		
		ReadingResult readingResult1 = new ReadingResult(2, booksRead1,booksRead2); 
		ReadingResult readingResult2 = new ReadingResult(2, booksRead1,booksRead2); 
		BooksRead booksRead1 = new BooksRead (5,"sci-fi",false);
		readingResult2.readSomeBooks(booksRead1);
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		ChallengeResult challengeResult2 = new ChallengeResult(readingResult2, writingResult1);
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult2);
        LinkedList<Literarian> lits = new LinkedList<Literarian>();
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(3,lits);
        LinkedList<String> names = litJam.readingDNF();
        LinkedList<String> testNames = new LinkedList<String>();
        testNames.add("Litty");
        assertEquals(names, testNames);
	}
	
	//testing reading DNF, no literarian meets criteria, return empty list
	@Test
	public void testReadingDNF3() {
		
		ReadingResult readingResult1 = new ReadingResult(2, booksRead1,booksRead2); 
		ReadingResult readingResult2 = new ReadingResult(2, booksRead1,booksRead2); 
		BooksRead booksRead1 = new BooksRead (5,"sci-fi",false);
		readingResult2.readSomeBooks(booksRead1);
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		ChallengeResult challengeResult2 = new ChallengeResult(readingResult2, writingResult1);
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult2);
        LinkedList<Literarian> lits = new LinkedList<Literarian>();
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(2,lits);
        LinkedList<String> names = litJam.readingDNF();
        LinkedList<String> testNames = new LinkedList<String>();
        assertEquals(names, testNames);
	}
	
	//testing the helper function booksReadScore
	@Test
	public void testBooksReadScore1() {
		
		assertEquals(readingResult1.booksReadScore(), 0, 0.1);

	}
	
	//testing the helper function booksReadScore
	@Test
	public void testBooksReadScore2() {
		
		readingResult1.readSomeBooks(booksRead3);
		
		assertEquals(readingResult1.booksReadScore(), 25, 0.1);

	}
	
	//testing final score
	@Test
	public void testFinalScore1() {
		
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(3,lits);

		assertEquals(litJam.finalScoreForLiterarian("Litty"), 0, 0.1);

	}

	//testing final score after modifying reading result
	@Test
	public void testFinalScore2() {
		
		WritingResult writingResult1 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(2.0, "fiction",true);
	    BooksRead booksRead2 = new BooksRead(3.0, "non-fiction", true);

	    ReadingResult readingResult1 = new ReadingResult(2, booksRead1, booksRead2); 
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(3,lits);
        
		assertEquals(litJam.finalScoreForLiterarian("Litty"), 125, 0.1);

	}
	
	//testing final score after modifying reading result
	@Test
	public void testFinalScore3() {
		
		WritingResult writingResult1 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(2.0, "fiction",false);
	    BooksRead booksRead2 = new BooksRead(3.0, "non-fiction", true);

	    ReadingResult readingResult1 = new ReadingResult(2, booksRead1, booksRead2); 
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(3,lits);
        
		assertEquals(litJam.finalScoreForLiterarian("Litty"), 15 + 50 + 125 - 55, 0.1);

	}
	
	//testing final score after modifying reading result
	@Test
	public void testFinalScore4() {
		
		WritingResult writingResult1 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(20, "fiction",true);
	    BooksRead booksRead2 = new BooksRead(11, "non-fiction", true);


	    ReadingResult readingResult1 = new ReadingResult(1, booksRead1, booksRead2); 
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(3,lits);
        
		assertEquals(litJam.finalScoreForLiterarian("Litty"), 5 + 125, 0.1);

	}
	
	//testing final score after modifying unique genre requirement
	@Test
	public void testFinalScore5() {
		
		WritingResult writingResult1 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(20, "fiction",true);
	    BooksRead booksRead2 = new BooksRead(11, "non-fiction", true);


	    ReadingResult readingResult1 = new ReadingResult(1, booksRead1, booksRead2); 
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam = new LitJam(2,lits);
        
		assertEquals(litJam.finalScoreForLiterarian("Litty"), 5 + 125 + 25, 0.1);

	}
	
	//testing any improvement method when the score does not change
	@Test
	public void testAnyImprovement1() {
		
		WritingResult writingResult1 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(20, "fiction",true);
	    BooksRead booksRead2 = new BooksRead(11, "non-fiction", true);


	    ReadingResult readingResult1 = new ReadingResult(1, booksRead1, booksRead2); 
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    
        lits.add(literarian1);
        lits.add(literarian2);
        LitJam litJam1 = new LitJam(2,lits);
        LitJam litJam2 = new LitJam(2,lits);
        
		assertFalse(litJam2.anyImprovement(litJam1));

	}
	
	//testing any improvement method when the score increases
	@Test
	public void testAnyImprovement2() {
		
		WritingResult writingResult1 = new WritingResult (2000, 10);
		WritingResult writingResult2 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(20, "fiction",true);
	    BooksRead booksRead2 = new BooksRead(11, "non-fiction", true);


	    ReadingResult readingResult1 = new ReadingResult(1, booksRead1, booksRead2); 
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		ChallengeResult challengeResult2 = new ChallengeResult(readingResult1, writingResult2);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    Literarian literarian3 = new Literarian("Litty", challengeResult2);
	    Literarian literarian4 = new Literarian("Reeds", challengeResult2);
	    
	    LinkedList<Literarian> lits1 = new LinkedList<Literarian>();
	    LinkedList<Literarian> lits2 = new LinkedList<Literarian>();
	    
        lits1.add(literarian1);
        lits1.add(literarian2);
        lits2.add(literarian3);
        lits2.add(literarian4);
        
        LitJam litJam1 = new LitJam(2,lits1);
        LitJam litJam2 = new LitJam(2,lits2);
        
		assertTrue(litJam2.anyImprovement(litJam1));

	}
	
	//testing any improvement method when the score increases
	@Test
	public void testAnyImprovement3() {
		
		WritingResult writingResult1 = new WritingResult (50000, 10);
		WritingResult writingResult2 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(20, "fiction",true);
	    BooksRead booksRead2 = new BooksRead(11, "non-fiction", true);


	    ReadingResult readingResult1 = new ReadingResult(1, booksRead1, booksRead2); 
	    ReadingResult readingResult2 = new ReadingResult(1, booksRead1, booksRead2); 
	    readingResult2.readSomeBooks(booksRead3);
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		ChallengeResult challengeResult2 = new ChallengeResult(readingResult2, writingResult2);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    Literarian literarian3 = new Literarian("Litty", challengeResult2);
	    Literarian literarian4 = new Literarian("Reeds", challengeResult2);
	    
	    LinkedList<Literarian> lits1 = new LinkedList<Literarian>();
	    LinkedList<Literarian> lits2 = new LinkedList<Literarian>();
	    
        lits1.add(literarian1);
        lits1.add(literarian2);
        lits2.add(literarian3);
        lits2.add(literarian4);
        
        LitJam litJam1 = new LitJam(2,lits1);
        LitJam litJam2 = new LitJam(2,lits2);
        
		assertTrue(litJam2.anyImprovement(litJam1));

	}
	
	//testing any improvement method when the score decreases
	@Test
	public void testAnyImprovement4() {
		
		WritingResult writingResult1 = new WritingResult (50000, 10);
		WritingResult writingResult2 = new WritingResult (50000, 10);
		
	    BooksRead booksRead1 = new BooksRead(20, "fiction",true);
	    BooksRead booksRead2 = new BooksRead(11, "non-fiction", true);


	    ReadingResult readingResult1 = new ReadingResult(1, booksRead1, booksRead2); 
	    ReadingResult readingResult2 = new ReadingResult(1, booksRead1, booksRead2); 
	    readingResult2.readSomeBooks(booksRead3);
	    
		ChallengeResult challengeResult1 = new ChallengeResult(readingResult1, writingResult1);
		ChallengeResult challengeResult2 = new ChallengeResult(readingResult2, writingResult2);
		
	    Literarian literarian1 = new Literarian("Litty", challengeResult1);
	    Literarian literarian2 = new Literarian("Reeds", challengeResult1);
	    Literarian literarian3 = new Literarian("Litty", challengeResult2);
	    Literarian literarian4 = new Literarian("Reeds", challengeResult2);
	    
	    LinkedList<Literarian> lits1 = new LinkedList<Literarian>();
	    LinkedList<Literarian> lits2 = new LinkedList<Literarian>();
	    
        lits1.add(literarian1);
        lits1.add(literarian2);
        lits2.add(literarian3);
        lits2.add(literarian4);
        
        LitJam litJam1 = new LitJam(2,lits1);
        LitJam litJam2 = new LitJam(2,lits2);
        
		assertFalse(litJam1.anyImprovement(litJam2));

	}
	
}
