package FPL2014_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FootBot {
	private static ArrayList<String> inData, outData;
	private static ArrayList<String> goal, def, mid, fwd;
	private static String[][] all, clubs = new String[20][2];
	private static int position = 0;
	private static int gk = 0, df = 0, md = 0, fw = 0, total = 0;
	private static boolean isFound;

	public static void main(String[] args) throws IOException {
		scrape("http://fantasy.premierleague.com/player-list/");
		populateClubs();
		findTables();
		outData = new ArrayList<String>();
		processTable(goal);
		processTable(def);
		processTable(mid);
		processTable(fwd);
		all = new String[outData.size()][5];
		allPlayers();
		pickTeam(1000);
	}

	private static void scrape(String website) {
		try {
			BufferedReader scanner = new BufferedReader(new InputStreamReader(
					new URL(website).openConnection().getInputStream()));
			String input = null;
			inData = new ArrayList<String>();

			while ((input = scanner.readLine()) != null) {
				inData.add(input);
			}

			scanner.close();
		} catch (IOException error) {
			System.err.println("error connecting to server.");
			error.printStackTrace();
		}
	}

	private static void populateClubs() {
		String allInitial = "Arsenal,0:Aston Villa,0:Burnley,0:Chelsea,0:Crystal Palace,0:Everton,0:Hull,0:Leicester,0:Liverpool,0:Man City,0:Man Utd,0:Newcastle,0:QPR,0:Southampton,0:Stoke,0:Sunderland,0:Swansea,0:Spurs,0:West Brom,0:West Ham,0";
		String[] clubAndCount = allInitial.split(":");

		for (int i = 0; i < clubAndCount.length; i++) {
			clubs[i] = clubAndCount[i].split(",");
		}
	}

	private static void findTables() {
		isFound = false;
		goal = new ArrayList<String>();
		def = new ArrayList<String>();
		mid = new ArrayList<String>();
		fwd = new ArrayList<String>();
		int tableCount = 0;

		for (int i = 0; i < inData.size(); i++) {
			if (Pattern
					.compile(Pattern.quote("<tbody"), Pattern.CASE_INSENSITIVE)
					.matcher(inData.get(i)).find()) {
				isFound = true;
				tableCount++;
			}

			if (isFound) {
				switch (tableCount) {
				case 1:
					goal.add(inData.get(i));
					break;
				case 2:
					goal.add(inData.get(i));
					break;
				case 3:
					def.add(inData.get(i));
					break;
				case 4:
					def.add(inData.get(i));
					break;
				case 5:
					mid.add(inData.get(i));
					break;
				case 6:
					mid.add(inData.get(i));
					break;
				case 7:
					fwd.add(inData.get(i));
					break;
				case 8:
					fwd.add(inData.get(i));
					break;
				default:
					break;
				}
			}

			if (Pattern
					.compile(Pattern.quote("</tbody"), Pattern.CASE_INSENSITIVE)
					.matcher(inData.get(i)).find()) {
				isFound = false;

				if (tableCount == 8) {
					break;
				}
			}
		}
	}

	private static void processTable(ArrayList<String> toProcess) {
		position++;
		isFound = false;
		String processing = "";

		for (int i = 0; i < toProcess.size(); i++) {
			if (Pattern.compile(Pattern.quote("<tr"), Pattern.CASE_INSENSITIVE)
					.matcher(toProcess.get(i)).find()) {
				isFound = true;
			}

			if (isFound) {
				processing += toProcess.get(i) + ",";
			}

			if (Pattern
					.compile(Pattern.quote("</tr"), Pattern.CASE_INSENSITIVE)
					.matcher(toProcess.get(i)).find()) {
				isFound = false;
				processing += "\n";
			}
		}

		processing = processing.replace("  ", "");

		switch (position) {
		case 0:
			break;
		case 1:
			processing = processing.replace("<tr>,", "GOAL,");
			break;
		case 2:
			processing = processing.replace("<tr>,", "DEF,");
			break;
		case 3:
			processing = processing.replace("<tr>,", "MID,");
			break;
		case 4:
			processing = processing.replace("<tr>,", "FWD,");
			break;
		default:
			break;
		}

		processing = processing.replace("<td>", "");
		processing = processing.replace("</td>", "");
		processing = processing.replace("\n\n", "\n");
		processing = processing.replace(",</tr>,", "");
		processing = processing.replace("Â£", "");
		String[] partialTable = processing.split("\n");
		String[][] finalTable = new String[partialTable.length][5];

		for (int i = 0; i < partialTable.length; i++) {
			finalTable[i] = partialTable[i].split(",");
			outData.add(partialTable[i]);
		}
	}

	private static void allPlayers() {
		for (int i = 0; i < outData.size(); i++) {
			all[i] = outData.get(i).split(",");
			swap(i, 0, 1);
			swap(i, 1, 3);
			swap(i, 2, 4);
			swap(i, 3, 4);
			swap(i, 1, 2);
		}
	}

	private static void swap(int pos, int one, int two) {
		String temp = all[pos][one];
		all[pos][one] = all[pos][two];
		all[pos][two] = temp;
	}

	private static void pickTeam(int funds) {
		//ZeroOneKnapsack solver = new ZeroOneKnapsack(funds);
		BoundedKnapsack solver = new BoundedKnapsack(funds);

		for (int i = 0; i < all.length; i++) {
			solver.add(all[i][0], (int) (Double.parseDouble(all[i][1]) * 10),
					Integer.parseInt(all[i][2]), all[i][3], all[i][4]);
		}

		// calculate the solution:
		List<Item> itemList = solver.calcSolution();

		// write out the solution in the standard output
		if (solver.isCalculated()) {
			System.out.println("Maximum value\t\t= £" + solver.getMaxWeight()
					/ 10.0);
			System.out.println("Total value of solution\t= £"
					+ solver.getSolutionWeight() / 10.0);
			System.out.println("Total points\t\t= " + solver.getProfit());
			System.out.println();
			System.out
			.println("You can buy the following players for your team:");

			for (Item item : itemList) {
				if (item.getInKnapsack() > 0) {
					total++;
					System.out
					.format("%1$-20s %2$-4s %3$-4s %4$-20s %5$-10s \n",
							item.getName(), item.getWeight() / 10.0,
							item.getValue(), item.getTeam(),
							item.getPosition());
					playerDetails(item.getTeam(), item.getPosition());
				}
			}

			totalPlayers();
			clubTotals();
			positionTotals();
		} else {
			System.err
			.println("The problem is not solved. Maybe you gave wrong data.");
		}
	}

	private static void playerDetails(String teams, String pos) {
		switch (teams) {
		case "Arsenal":
			clubs[0][1] = "" + (Integer.parseInt(clubs[0][1]) + 1);
			break;
		case "Aston Villa":
			clubs[1][1] = "" + (Integer.parseInt(clubs[1][1]) + 1);
			break;
		case "Burnley":
			clubs[2][1] = "" + (Integer.parseInt(clubs[2][1]) + 1);
			break;
		case "Chelsea":
			clubs[3][1] = "" + (Integer.parseInt(clubs[3][1]) + 1);
			break;
		case "Crystal Palace":
			clubs[4][1] = "" + (Integer.parseInt(clubs[4][1]) + 1);
			break;
		case "Everton":
			clubs[5][1] = "" + (Integer.parseInt(clubs[5][1]) + 1);
			break;
		case "Hull":
			clubs[6][1] = "" + (Integer.parseInt(clubs[6][1]) + 1);
			break;
		case "Leicester":
			clubs[7][1] = "" + (Integer.parseInt(clubs[7][1]) + 1);
			break;
		case "Liverpool":
			clubs[8][1] = "" + (Integer.parseInt(clubs[8][1]) + 1);
			break;
		case "Man City":
			clubs[9][1] = "" + (Integer.parseInt(clubs[9][1]) + 1);
			break;
		case "Man Utd":
			clubs[10][1] = "" + (Integer.parseInt(clubs[10][1]) + 1);
			break;
		case "Newcastle":
			clubs[11][1] = "" + (Integer.parseInt(clubs[11][1]) + 1);
			break;
		case "QPR":
			clubs[12][1] = "" + (Integer.parseInt(clubs[12][1]) + 1);
			break;
		case "Southampton":
			clubs[13][1] = "" + (Integer.parseInt(clubs[13][1]) + 1);
			break;
		case "Stoke":
			clubs[14][1] = "" + (Integer.parseInt(clubs[14][1]) + 1);
			break;
		case "Sunderland":
			clubs[15][1] = "" + (Integer.parseInt(clubs[15][1]) + 1);
			break;
		case "Swansea":
			clubs[16][1] = "" + (Integer.parseInt(clubs[16][1]) + 1);
			break;
		case "Spurs":
			clubs[17][1] = "" + (Integer.parseInt(clubs[17][1]) + 1);
			break;
		case "West Brom":
			clubs[18][1] = "" + (Integer.parseInt(clubs[18][1]) + 1);
			break;
		case "West Ham":
			clubs[19][1] = "" + (Integer.parseInt(clubs[19][1]) + 1);
			break;
		default:
			break;
		}

		switch (pos) {
		case "GOAL":
			gk++;
			break;
		case "DEF":
			df++;
			break;
		case "MID":
			md++;
			break;
		case "FWD":
			fw++;
			break;
		default:
			break;
		}
	}

	private static void totalPlayers() {
		System.out.println();
		System.out.format("%1$-20s %2$-2s \n", "Total players:", total);
	}

	private static void clubTotals() {
		System.out.println();
		for (int i = 0; i < clubs.length; i++) {
			if (Integer.parseInt(clubs[i][1]) > 0) {
				System.out
				.format("%1$-20s %2$-2s \n", clubs[i][0], clubs[i][1]);
			}
		}
	}

	private static void positionTotals() {
		System.out.println();
		System.out.format("%1$-20s %2$-2s \n", "GOAL", gk);
		System.out.format("%1$-20s %2$-2s \n", "DEF", df);
		System.out.format("%1$-20s %2$-2s \n", "MID", md);
		System.out.format("%1$-20s %2$-2s \n", "FWD", fw);
	}
}
