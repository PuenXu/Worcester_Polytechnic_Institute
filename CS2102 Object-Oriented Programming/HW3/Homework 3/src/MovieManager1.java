import java.util.LinkedList;

class MovieManager1 {
	
	MovieManager1() {}
	
	/**
	 * Exclude movies that are simulcast or have invalid showtime from a list of movies
	 * @param movies a linkedlist of movies to clean
	 * @return the linkedlist with the movies qualified
	 */
	//cleaning
	public LinkedList<Movie> cleanMovies(LinkedList<Movie> movies) 
	{
		LinkedList<Movie> cleanedMovies = new LinkedList<Movie>();
		
		for(Movie aMovie : movies) {
			if (!aMovie.simulcast && aMovie.showtime <= 2400 && aMovie.showtime >= 0) {
			//include only movies that are not simulcast and showtime ranges from 0 to 2400
				cleanedMovies.add(aMovie);
			}
		}
		
		return cleanedMovies;		
	}
	
	/**
	 * Put movies in the list excepts simulcast into the corresponding field of a programme and return the programme
	 * @param movies a linkedlist of movies to organize
	 * @return the programme with the movies organized
	 */
	//parsing
	public Programme organizeMovies(LinkedList<Movie> movies)
	{
		LinkedList<Movie> aMatinee = new LinkedList<Movie>();  //create lists
		LinkedList<Movie> aPrimetime = new LinkedList<Movie>();
		LinkedList<Movie> aSoiree = new LinkedList<Movie>();
		
		LinkedList<Movie> cleanedMovies = this.cleanMovies(movies);  //clean the data
		
		for(Movie aMovie : cleanedMovies) {
			if(aMovie.showtime >= 700 && aMovie.showtime < 1200) {  //check the type of show according to showtime
				aMatinee.add(aMovie);  //add the movie to correspinding list
			}
			else if(aMovie.showtime >= 1200 && aMovie.showtime < 2000) {
				aPrimetime.add(aMovie);
			}
			else if(aMovie.showtime >= 2000 || aMovie.showtime < 200) {
					aSoiree.add(aMovie);
			}
		}
		
		Programme aProgramme = new Programme(aMatinee, aPrimetime,aSoiree);  //create a programme with the organized lists
		
		return aProgramme;
	}
	
}
