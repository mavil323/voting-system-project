import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Screen {
	/* **************************************************
	 * Loops until  user enters 'exit'
	 * Choices:  vote screen, admin screen, exit program
	 * **************************************************/
	public static void homeScreen(Database db, Scanner input) {
		int flag = 0;				// used to exit the program
		String givenString = "";	// used to store user input, which is then used to "change screens"
		while(flag == 0) {
			for(int i = 0; i < 25; i++) {
				System.out.println();
			}
			System.out.println();
			System.out.println("######## ########  #### ##    ##    ##     ##  #######  ######## #### ##    ##  ######   ");
			System.out.println("##       ##     ##  ##  ##   ##     ##     ## ##     ##    ##     ##  ###   ## ##    ##  ");
			System.out.println("##       ##     ##  ##  ##  ##      ##     ## ##     ##    ##     ##  ####  ## ##        ");
			System.out.println("######   ########   ##  #####       ##     ## ##     ##    ##     ##  ## ## ## ##   #### ");
			System.out.println("##       ##         ##  ##  ##       ##   ##  ##     ##    ##     ##  ##  #### ##    ##  ");
			System.out.println("##       ##         ##  ##   ##       ## ##   ##     ##    ##     ##  ##   ### ##    ##  ");
			System.out.println("######## ##        #### ##    ##       ###     #######     ##    #### ##    ##  ######   ");
			System.out.println();
			System.out.println("\tWelcome to Epik Voting System. \n\n"+
					 		   "\tEnter 'vote' to place a vote. \n" +
					 		   "\tEnter 'admin' for admin options. \n" +
					 		   "\tEnter 'exit' to exit. \n");
			System.out.print(  "> ");
			givenString = input.next();
			// exits the program
			if(givenString.equalsIgnoreCase("exit"))
				flag++;
			// brings user to login screen
			else if(givenString.equalsIgnoreCase("vote") || givenString.equalsIgnoreCase("admin"))
				loginScreen(db, input, givenString);
			// print statement if an incorrect input is given
			else 
				System.out.println("Unknown command.");
		}
	}
	
	/* ***************************************************
	 * Login screen for admin page.
	 * Loop until correct credentials have been entered.
	 * ***************************************************/
	public static void loginScreen(Database db, Scanner sn, String menu) {
		int exit = 0;				// used to keep loops going if incorrect inputs are entered
		String voterID = "";		// stores the entered voterID to be used further in the menus
		String adminID = "";		// used to verify valid admin ID is entered
		String password = "";		// used to verify valid admin password is entered
		
		System.out.println();		
		while(exit == 0) {		    // brings user to vote screen if requirements met
			if(menu.equalsIgnoreCase("vote")) {
				System.out.print("Voter ID >  "); // Voter IDs included in readme file
				voterID = sn.next();
				if(voterID.equalsIgnoreCase("exit"))
					break;
				if(!isNumeric(voterID))
					continue;
				for(int i = 0; i < db.getVoters().size(); i++) {
					if(db.getVoters().get(i).getID() == Integer.parseInt(voterID)) {
						voteScreen(db,sn,Integer.parseInt(voterID));
						exit++;
						break;
					}
				}
			}
			
			if(menu.equalsIgnoreCase("admin")) {	// brings user to admin screen if requirements met
				System.out.print("Username > ");
				adminID = sn.next();
				if(adminID.equalsIgnoreCase("exit"))
					break;
				System.out.print("Password > ");
				password = sn.next();
				for(Admin a: db.getAdmin()) {
					if(adminID.equalsIgnoreCase(a.getUsername()) && password.equalsIgnoreCase(a.getPassword())) {
						adminScreen(db,sn);
						exit++;
						break;
					}
				}
			}
			
			if(exit == 0)	// if exit variable is still zero, because incorrect info given
				System.out.println("\nIncorrect login information. Try again or type 'exit' to return to previous screen.\n");
		}
	}
	
	/* ***************************************************************************************
	 * Loop continues to repeat until the user enters 'exit'
	 * Prompts user to enter the year election to participate in
	 * ***************************************************************************************/
	public static void voteScreen(Database db, Scanner sn, int id) {		
		int exiter = 0;								// used to keep loops going if incorrect inputs are entered
		int counter = 0;							// used to keep loops going if incorrect inputs are entered
		int givenNumber = 0;						// used to store the year election to participate in
		int[] ballots = db.getElectionIDs();		// used to print out the ballots to choose from
		
		System.out.println();	
		/* ******************************************************************************************
		 * Locate election that voter wants to participate in and then create an array to hold votes
		 * ******************************************************************************************/
		System.out.println("Available ballots: ");
		for(int i = 0; i < ballots.length; i++) {
			System.out.print("\t" + ballots[i]);
		}
		System.out.print("\nType the year of the listed election that you want to participate in: ");
		givenNumber = sn.nextInt();
		for(Voter v: db.getVoters()) {
			if(v.getID() == id) {
				for(int i: v.getElection()) {
					if(givenNumber == i) {
						System.out.println("\nYou have already voted in that election.");
						counter++;
					}
				}
			}
		}
		if(counter == 0) {
			System.out.println("\n");
			Set<Integer> numOffices = new HashSet<Integer>();	// used to store the officeIDs of all offices
			for(Candidate c: db.getBallot(givenNumber)) {
				numOffices.add(c.getOfficeID());
			}
			int[] votes = new int[numOffices.size()];	// used to store voter's votes on the ballot
			
			/* *******************************************************************************
			 * Asks the voter to place votes 1 at a time based on the offices on the ballot. *
			 * *******************************************************************************/
			int count = 0;
			for(int office: numOffices) {
				exiter = 0;
				ArrayList<Integer> ids = new ArrayList<Integer>();
				System.out.print("\t" + String.format("%-30s", "Running for " + db.getBallot(givenNumber).get(0).generateOffice(office) + ":"));
				System.out.print("\t" + String.format("%25s", "Candidate ID:") + "\n");
				for(int j = 0; j < db.getBallot(givenNumber).size(); j++) {
					Candidate c = db.getBallot(givenNumber).get(j);
					if(c.getOfficeID() == office) {
						System.out.print("\t" + String.format("%-15s", c.getFirstName()));
						System.out.print("\t" + String.format("%-15s", c.getLastName()));
						System.out.print("\t" + String.format("%25s", c.getCandidateID()) + "\n");
						ids.add(c.getCandidateID());
					}
				}
				while(exiter == 0) {
					System.out.print("\tEnter Candidate ID that you want to vote for: ");
					votes[count] = sn.nextInt();
					for(int i: ids) {
						if(i == votes[count]) {
							exiter++;
							break;
						}
					}
					if(exiter == 0) 
						System.out.println("\tThat is not a valid Candidate ID for this office.");
				}
				System.out.println();
				count++;
			}
			
			/* ****************************************************
			 * Places vote in correct Candidate object
			 * Creates a Vote object/places it in the database. 
			 * Contains who the user voted for.
			 * ****************************************************/
			for(int i = 0; i < votes.length; i++) {
				for(Candidate c: db.getBallot(givenNumber)) {
					if(votes[i] == c.getCandidateID()) {
						c.addVote();
						break;
					}
				}
			}
			db.addVote(new Vote(givenNumber,votes));
			db.recordVote(givenNumber, id);
			System.out.println();
			System.out.println("Vote recorded Successfully.");
			System.out.println();
		}
	}
	
	/* *************************************************************************
	 * Loop that continues to repeat until the user enters 'exit'
	 * Loop continues to ask the user for which admin menu they want access to.
	 * *************************************************************************/
	public static void adminScreen(Database db, Scanner sn) {	
		int exit = 0;
		String givenString = "";
		
		while(exit == 0) {	
			System.out.println();
			System.out.println("\t Enter 'voter' if you want to see voter information.");
			System.out.println("\t Enter 'reports' if you want to see ballot information.");
			System.out.println("\t Enter 'register' if you want to register a voter");
			System.out.println("\t Enter 'exit' if you want to return to previous options.");
			System.out.print("\n > ");
			givenString = sn.next();
			
			switch (givenString) {	//Switch on the admin choices
				
				case "voter": 
					lookupVoterScreen(db,sn);
					break;
				
				case "reports": 
					reportsScreen(db,sn);
					break;
				
				case "register": 
					registerVoterScreen(db,sn);
					break;
				
				case "exit": 
					exit++;
					break;
				
				default: // If input is wrong, print out message and then ask again.
					System.out.println("Input Error.");
					break;
			}
		}
	}
	
	/* ************************************************************************
	 * Register a new voter
	 * Prompt user to enter voter information in sequence
	 * Check to see if voter exists
	 * If voter doesn't exist - create new voter object 
	 * If voter exists - notify and break;
	 * ************************************************************************/
	public static void registerVoterScreen(Database db, Scanner sn) {
		int id = 0;								// used to see if the voter already exists in the system
		String[] voterInfo = new String[5];		// used to store user input about voter info, and also used for verification
		
		System.out.print("\tEnter first name: ");
		voterInfo[0] = sn.next();
		System.out.print("\tEnter last name: ");
		voterInfo[1] = sn.next();
		System.out.print("\tEnter street address: ");
		voterInfo[2] = getInput();
		System.out.print("\tEnter city: ");
		voterInfo[3] = getInput();
		System.out.print("\tEnter state: ");
		voterInfo[4] = sn.next();
		Voter v = new Voter(voterInfo[0],voterInfo[1],voterInfo[2],voterInfo[3],voterInfo[4]);
		id = v.getID();
		int counter = 0;
		for(int i = 0; i < db.getVoters().size(); i++) {
			//System.out.println(db.getVoters().get(i).getID() + " - " + id); // For testing
			if(db.getVoters().get(i).getID() == id) {
				System.out.println("\nVoter already exists");
				counter++;
				break;
			}
		}
		if (counter == 0) {
			db.addVoter(v);
			System.out.println("\nVoter registered successfully.");
			System.out.println("\n" + v.toString());
		}
	}
	
	/* **********************************************************************************
	 * Loop that continuously asks for type of report admin wants to view, until 'exit'
	 * Prints selected report 
	 * **********************************************************************************/
	public static void reportsScreen(Database db, Scanner sn) {
		int exit = 0;				// used to exit the screen
		String givenString = "";	// used to store which menu the user has selected
		int givenNumber = 0;		// used to store which year ballot to be looking for
		int[] ballots = db.getElectionIDs();		// used to print out the ballots to choose from
		
		System.out.println();	
		System.out.println();
		// Loop that continues to ask for the year election that you want to view until you give a valid year
		while(exit == 0) {
			System.out.print("Available ballots: ");
			for(int i = 0; i < ballots.length; i++) {
				System.out.print("\t" + ballots[i]);
			}
			System.out.print("\nYear >  ");
			givenNumber = sn.nextInt();
			for(int year: db.getElectionIDs()) {
				// If the given year is valid, exit loop and go to next menu
				if(givenNumber == year) {
					exit++;
				}
			}
			if(exit == 0)
				System.out.println("No election information for that year.");
		}
		exit = 0;
		Report r = new Report(givenNumber, db.getBallot(givenNumber), db.getVotes(givenNumber), db.getVoters());
		// loop that continues to ask what kind of report you want until you enter 'exit'
		while(exit == 0) {
			System.out.println();	
			System.out.println();
			System.out.println("\t Enter 'ballot' if you want to see ballot information.");
			System.out.println("\t Enter 'results' if you want to see " + givenNumber + " election results.");
			System.out.println("\t Enter 'exit' if you want to return to previous options.");
			System.out.print("\n > ");
			givenString = sn.next();
			// depending on what the user enters, the program will either ask more information or print out requested information
			switch (givenString) {
				// Tests the ballot reports
				case "ballot": 
					System.out.println(r.ballot());
					break;
				// Tests the general reports
				case "results": 
					System.out.println(r.electionReport());
					break;
				// Exits the loop, to either close the program or return to main screen options
				case "exit": 
					exit++;
					break;
				// If input is wrong, print out message and then ask again.
				default: 
					System.out.println("Input Error.");
					break;
			}
		}
	}
	
	/* ************************************************************************
	 * Prompt user to enter information about a voter
	 * Once entered, a voter or a list of voters is printed to the screen
	 * then the program returns to the previous menu.
	 * ************************************************************************/
	public static void lookupVoterScreen(Database db, Scanner sn) {
		String givenString = "";				// used in searching the list of voters to find a match in given information
		Report r = new Report(db.getVoters());	// report generated with a list of all voters, used to print formatted strings of voters out
		
		System.out.println();	
		System.out.println();	
		System.out.println("\tEnter last name of voter you want to look up");
		System.out.println("\tEnter 'all' to list all voters.\n\n");
		System.out.print(" > ");
		givenString = sn.next();
		System.out.println("");
		if (givenString.equals("all")) {
			System.out.println("\n    ----------------------------------------------------------\n");
			System.out.println(r.reportVoters());
		}
		else {
			String[] name = givenString.split(" ");
			int counter = 0;
			for(Voter v: db.getVoters()) {
				if(name[0].equalsIgnoreCase(v.getLastName())) {
					System.out.println("\n    ----------------------------------------------------------\n");
					System.out.println(v.toString());
					counter++;
				}
			}
			if(counter == 0)
				System.out.println("Name not found.");
		}
	}
	/* ************************************************************************
	 * Generate a unique ID for voter registeration 
	 * Used to test if the voter is already registered.
	 * ************************************************************************/
	private static int generateID(String fName, String lName, String address, String city, String state) {
        String temp = fName + lName + address + city + state;
        int hash = 7;
        for (int i = 0, n = temp.length(); i < n; i++)
        {
            hash = hash * 31 + temp.charAt(i);
        }
        return hash;
    }
	
	/* ************************************************************************
	 * Test if inputs are in a correct format. 
	 * Takes a string, checks if can convert to an (int) Returns false if can't.
	 * ************************************************************************/
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        @SuppressWarnings("unused")
			int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	/* ************************************************************************
	 * Method for gathering the street address of a voter during registration
	 * Prevent issue reading the program's prompt as user input
	 * ************************************************************************/
	private static String getInput() {
	    @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
	    return scanner.nextLine();
	}
}
