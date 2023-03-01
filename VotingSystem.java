/* ************************************************************************************************
 * Simulates a voting system in the United States. Requires a registered voter or admin to log in *
 * ************************************************************************************************/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class VotingSystem {
	/* ***********************************************************************
	 * Main creates DB and Report objects 
	 * All prompts are built here to handle user input.
	 * Loops are used to take user input, until 'exit'
	 * ***********************************************************************/
	public static void main(String[] args) {
		Database database = new Database();
		Scanner input = new Scanner(System.in);
		Screen.homeScreen(database, input);
		input.close();
		database.writeVoters();
		database.writeVotes();
		System.out.println("\nElection progress saved.\n");
	}	
}