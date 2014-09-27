package FPL2014_15;

import java.util.List;

public class BoundedKnapsack extends ZeroOneKnapsack {
	protected static String[][] clubs = new String[20][2];
	protected static String[][] pos = new String[4][2];
	protected static int total = 15;

	public BoundedKnapsack() {
	}

	public BoundedKnapsack(int _maxWeight) {
		setMaxWeight(_maxWeight);
		// //////////////////
		// String[][] clubs = new String[20][2];
		String allInitial = "Arsenal,3:Aston Villa,3:Burnley,3:Chelsea,3:Crystal Palace,3:Everton,3:Hull,3:Leicester,3:Liverpool,3:Man City,3:Man Utd,3:Newcastle,3:QPR,3:Southampton,3:Stoke,3:Sunderland,3:Swansea,3:Spurs,3:West Brom,3:West Ham,3";
		String[] clubAndCount = allInitial.split(":");
		for (int i = 0; i < clubAndCount.length; i++) {
			clubs[i] = clubAndCount[i].split(",");
		}
		// String[][] pos = new String[4][2];
		allInitial = "GOAL,2:DEF,5:MID,5:FWD,3";
		String[] posAndCount = allInitial.split(":");
		for (int i = 0; i < posAndCount.length; i++) {
			pos[i] = posAndCount[i].split(",");
		}
		// int total = 15;
		// /////////////////////
	}

	public BoundedKnapsack(List<Item> _itemList) {
		setItemList(_itemList);
	}

	public BoundedKnapsack(List<Item> _itemList, int _maxWeight) {
		setItemList(_itemList);
		setMaxWeight(_maxWeight);
	}

	@Override
	public List<Item> calcSolution() {
		int n = itemList.size();

		// add items to the list, if bounding > 1
		for (int i = 0; i < n; i++) {
			Item item = itemList.get(i);

			for (int j = 0; j < clubs.length; j++) {
				for (int k = 0; k < pos.length; k++) {
					if (item.getBounding() > 0
							&& (item.getTeam().equals(clubs[j][0]) && Integer
									.parseInt(clubs[j][1]) > 0)
									&& (item.getPosition().equals(pos[k][0]) && Integer
											.parseInt(pos[k][1]) > 0) && total > 0) {
						for (int l = 1; l < item.getBounding(); l++) {
							add(item.getName(), item.getWeight(),
									item.getValue());
						}
						item.setBounding(0);
						clubs[j][1] = "" + (Integer.parseInt(clubs[j][1]) - 1);
						pos[k][1] = "" + (Integer.parseInt(pos[k][1]) - 1);
						total--;
					}
				}
			}
			// if (item.getBounding() > 1) {
			// for (int j = 1; j < item.getBounding(); j++) {
			// add(item.getName(), item.getWeight(), item.getValue());
			// }
			// }
		}

		super.calcSolution();

		// delete the added items, and increase the original items
		while (itemList.size() > n) {
			Item lastItem = itemList.get(itemList.size() - 1);

			if (lastItem.getInKnapsack() == 1) {
				for (int i = 0; i < n; i++) {
					Item iH = itemList.get(i);

					if (lastItem.getName().equals(iH.getName())) {
						iH.setInKnapsack(1 + iH.getInKnapsack());
						break;
					}
				}
			}

			itemList.remove(itemList.size() - 1);
		}

		return itemList;
	}

	// add an item to the item list
	public void add(String name, int weight, int value, String team,
			/*int bounding*/ String position) {
		if (name.equals("")) {
			name = "" + (itemList.size() + 1);
		}

		itemList.add(new Item(name, weight, value, team, position /*bounding*/));
		setInitialStateForCalculation();
	}
}
