package FPL2014_15;

public class Item {
	protected String name = "";
	protected int weight = 0;
	protected int value = 0;
	protected String team;
	protected String position;
	protected int bounding = 1; // the maximal limit of item's pieces
	protected int volume = 1;
	protected static int gk, df, md, fw, club, tot;
	protected static final int MAXGK = 2, MAXDF = 5, MAXMD = 5, MAXFW = 3;
	protected static final int MAXCLUB = 3, MAXTOT = 15;
	protected int inKnapsack = 0; // the pieces of item in solution

	public Item() {
	}

	public Item(Item item) {
		setName(item.name);
		setWeight(item.weight);
		setValue(item.value);
		setBounding(item.bounding);
	}

	public Item(int _weight, int _value) {
		setWeight(_weight);
		setValue(_value);
	}

	public Item(int _weight, int _value, int _bounding) {
		setWeight(_weight);
		setValue(_value);
		setBounding(_bounding);
	}

	public Item(String _name, int _weight, int _value) {
		setName(_name);
		setWeight(_weight);
		setValue(_value);
	}

	public Item(String _name, int _weight, int _value, String _team,
			String _position) {
		setName(_name);
		setWeight(_weight);
		setValue(_value);
		setTeam(_team);
		setPosition(_position);
	}

	public Item(String _name, int _weight, int _value, int _bounding) {
		setName(_name);
		setWeight(_weight);
		setValue(_value);
		setBounding(_bounding);
	}

	public Item(String _name, int _weight, int _value, String _team,
			int _bounding) {
		setName(_name);
		setWeight(_weight);
		setValue(_value);
		setTeam(_team);
		setBounding(_bounding);
	}

	public int getVolume() {
		return volume;
	}

	public void setName(String _name) {
		name = _name;
	}

	public void setWeight(int _weight) {
		weight = Math.max(_weight, 0);
	}

	public void setValue(int _value) {
		value = Math.max(_value, 0);
	}

	public void setTeam(String _team) {
		team = _team;
	}

	public void setPosition(String _position) {
		position = _position;
	}

	public void setInKnapsack(int _inKnapsack) {
		inKnapsack = Math.min(getBounding(), Math.max(_inKnapsack, 0));
	}

	public void setBounding(int _bounding) {
		bounding = Math.max(_bounding, 0);

		if (bounding == 0) {
			inKnapsack = 0;
		}
	}

	public void checkMembers() {
		setWeight(weight);
		setValue(value);
		setBounding(bounding);
		setInKnapsack(inKnapsack);
	}

	public String getName() {
		return name;
	}

	public int getWeight() {
		return weight;
	}

	public int getValue() {
		return value;
	}

	public String getTeam() {
		return team;
	}

	public String getPosition() {
		return position;
	}

	public int getInKnapsack() {
		return inKnapsack;
	}

	public int getBounding() {
		return bounding;
	}
}
