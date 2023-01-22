
public interface ISet {

	//common methods
	ISet add(TodaysRatings aRatings);
	boolean equals(Object obj);
	int totalSongDownloads(int month, int year);
	int bestRankByMonth(int month, int year);	
}
