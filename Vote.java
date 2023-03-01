/* **************************************************
 * This object represents voter's completed vote
 * Stores Candidates voted for & year 
 * **************************************************/
import java.util.Arrays;

public class Vote {

	private int electionID;		// store which election this vote object belongs to
	private int[] votes;		// store all votes cast on the ballot

	// default constructor not used
	public Vote() {}

	/* **************************************************************
	 *  arg constructor
 	 * Store votes in an int array along with electionID (year)
	 * **************************************************************/
	public Vote(int electionID, int[] votes) {
		this.electionID = electionID;
		this.votes = votes;
	}

	/***************************************************
	 *               Getters and Setters               *
	 ***************************************************/

	public int getElectionID() {
		return electionID;
	}

	public void setElectionID(int electionID) {
		this.electionID = electionID;
	}

	public int[] getVotes() {
		return votes;
	}

	public void setVotes(int[] votes) {
		for (int i : votes)
		{
			this.votes[i] = votes[i];
		}
	}
	
	// Print out each vote's election ID and the votes
	public String toString()
	{
		return electionID + " " + Arrays.toString(votes);
	}
}
