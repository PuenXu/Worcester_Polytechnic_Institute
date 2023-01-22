import static org.junit.Assert.*;

import org.junit.Test;

import java.util.LinkedList;

public class MovieExamples
{
	MovieManager1 mm1 = new MovieManager1();
	MovieManager2 mm2 = new MovieManager2();
	LinkedList<Movie> movies = new LinkedList<Movie>();
	Programme programme1 = new Programme();

	Movie starTrek = new Movie("Star Trek Into Darkness", 1400, 132, false);
	Movie starTrekDirectorCut = new Movie("Star Trek Into Darkness", 1400, 150, false);
	Movie pinicchio = new Movie("GUILLERMO DEL TORO'S PINOCCHIO", 945, 114, false);
	Movie pinicchioDirectorCut = new Movie("GUILLERMO DEL TORO'S PINOCCHIO", 1245, 114, false);
	Movie swampThing = new Movie("RiffTrax Live: The Return of Swamp Thing", 2030, 118, true);
	Movie sevenSamurai = new Movie("Seven Samurai", 0, 207, false);

	public MovieExamples()
	{
		movies.add(starTrek);
		programme1.primetime.add(starTrek);

		movies.add(pinicchio);
		programme1.matinee.add(pinicchio);

		movies.add(swampThing);
		programme1.soiree.add(swampThing);

		movies.add(sevenSamurai);
		programme1.soiree.add(sevenSamurai);
	}
	
	//testing movies with same title and showtime
	@Test
	public void testMovieEquals1() {
		assertTrue(starTrek.equals(starTrekDirectorCut));
	}
	
	//testing movies with same title and different showtime
	@Test
	public void testMovieEquals2() {
		assertFalse(pinicchio.equals(pinicchioDirectorCut));
	}

	//testing movies with same size in each pair of lists
	@Test
	public void testProgrammePairSize1() {
		Programme programme2 = new Programme();

		programme2.primetime.add(starTrek);
		programme2.matinee.add(pinicchio);
		programme2.soiree.add(sevenSamurai);
		programme2.soiree.add(swampThing);

		assertTrue(programme1.checkPairSizeEqual(programme2));
	}
	
	//testing movies with different size in one pair of lists
	@Test
	public void testProgrammePairSize2() {
		Programme programme2 = new Programme();

		programme2.primetime.add(starTrek);
		programme2.matinee.add(pinicchio);
		programme2.soiree.add(sevenSamurai);

		assertFalse(programme1.checkPairSizeEqual(programme2));
	}
	
	//testing same movies with wrong order
	@Test
	public void testSameMovieSameOrder1() {
		Programme programme2 = new Programme();

		programme2.primetime.add(starTrek);
		programme2.matinee.add(pinicchio);
		programme2.soiree.add(sevenSamurai);
		programme2.soiree.add(swampThing);

		assertFalse(programme1.checkSameMovieSameOrder(programme2));
	}
	
	//testing same movies with correct order
	@Test
	public void testSameMovieSameOrder2() {
		Programme programme2 = new Programme();

		programme2.primetime.add(starTrek);
		programme2.matinee.add(pinicchio);
		programme2.soiree.add(sevenSamurai);
		programme2.soiree.addFirst(swampThing);  //different order

		assertTrue(programme1.checkSameMovieSameOrder(programme2));
	}
	
	//testing different movies with correct order
	@Test
	public void testSameMovieSameOrder3() {
		Programme programme2 = new Programme();

		programme2.primetime.add(starTrek);
		programme2.matinee.add(pinicchioDirectorCut);  //different movie
		programme2.soiree.add(sevenSamurai);
		programme2.soiree.addFirst(swampThing);

		assertFalse(programme1.checkSameMovieSameOrder(programme2));
	}

	@Test
	public void instructorTestProgramme_EmptyReport() {
		Programme report2 = new Programme();
		assertFalse(programme1.equals(report2));
	}
	
	@Test
	public void instructorTestProgramme_WrongOrder() {
		Programme programme2 = new Programme();

		programme2.primetime.add(starTrek);
		programme2.matinee.add(pinicchio);
		programme2.soiree.add(sevenSamurai);
		programme2.soiree.add(swampThing);

		assertFalse(programme1.equals(programme2));
	}

	@Test
	public void instructorTestProgramme_DifferentInstances() {
		Programme programme2 = new Programme();

		Movie starTrek2 = new Movie("Star Trek Into Darkness", 1400, 132, false);
		Movie pinocchio2 = new Movie("GUILLERMO DEL TORO'S PINOCCHIO", 945, 114, false);
		Movie swampThing2 = new Movie("RiffTrax Live: The Return of Swamp Thing", 2030, 118, true);
		Movie sevenSamurai2 = new Movie("Seven Samurai", 0, 207, false);

		programme2.primetime.add(starTrek2);
		programme2.matinee.add(pinocchio2);
		programme2.soiree.add(swampThing2);
		programme2.soiree.add(sevenSamurai2);

		assertTrue(programme1.equals(programme2));
	}

	@Test
	public void instructorTestProgramme_SameInstances() {
		Programme programme2 = programme1;
		assertTrue(programme1.equals(programme2));
	}
	
	@Test
	public void instructorTestOrganizeMovies()
	{
		Programme programme2 = mm1.organizeMovies(movies);
		programme1.soiree.remove(swampThing); //it's a simulcast
		assertEquals(programme1, programme2);
	}
	
	//empty list
	@Test
	public void testOrganizeShows1()
	{
		LinkedList<Movie> movies = new LinkedList<Movie>();
		Programme programme1 = new Programme();		
		Programme programme2 = mm1.organizeMovies(movies);

		assertEquals(programme1, programme2);
	}
	
	//only simulcast shows in the list
	@Test
	public void testOrganizeShows2()
	{
		LinkedList<Movie> movies = new LinkedList<Movie>();
		movies.add(swampThing);
		Programme programme1 = new Programme();		
		Programme programme2 = mm1.organizeMovies(movies);

		assertEquals(programme1, programme2);
	}
	
	//making swamp thing not a simulcast anymore, so we don't need to remove it
	@Test
	public void testOrganizeShows3()
	{
		swampThing.simulcast = false;
		Programme programme2 = mm1.organizeMovies(movies);
		
		assertEquals(programme1, programme2);
	}

}

/*
 * Subtasks in problem 1
 * 
 *  Approach 1
 *   - Determine if the movie is simulcast or not
 *   - Determine if the movie is matinee, primetime, or soiree
 *   - Handle empty list
 *   - Handle invalid showtime
 *   - Create a matinee, a primetime, and a soiree list
 *   - Put movies into their corresponding list (i.e. matinee into matinee list)
 *   - Create a programme with matinee, primetime, and soiree lists
 *   - return the programme
 *  
 */ 
