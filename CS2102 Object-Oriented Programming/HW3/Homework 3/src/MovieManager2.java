import java.util.LinkedList;

class MovieManager2 {
	
	MovieManager2() {}

	/**
	 * Put movies in the list excepts simulcast into the corresponding field of a programme and return the programme
	 * @param movies a linkedlist of movies to organize
	 * @return the programme with the movies organized
	 */
	//parsing
	public Programme organizeMovies(LinkedList<Movie> movies) {
		
		Programme aProgramme = new Programme(); // create an empty program
		
		for(Movie aMovie : movies) {
			if (!aMovie.simulcast) { 
				if(aMovie.showtime >= 700 && aMovie.showtime < 1200) {  //check the type of show according to showtime
					aProgramme.matinee.add(aMovie);  //add the movie to corresponding field of the programme
				}
				else if(aMovie.showtime >= 1200 && aMovie.showtime < 2000) {
					aProgramme.primetime.add(aMovie);
				}
				else if(aMovie.showtime >= 2000 || aMovie.showtime < 200) {
					aProgramme.soiree.add(aMovie);
				}
			}
		}
		
		return aProgramme;
	}
	
}
