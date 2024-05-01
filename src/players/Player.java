package players;


public abstract class Player{
	private String name;
	private final Gender gender;
	private int age;
	private int joiningYear;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getJoiningYear() {
		return joiningYear;
	}

	public void setJoiningYear(int joiningYear) {
		this.joiningYear = joiningYear;
	}

	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

	public Player(String name, Gender gender, int age, int joiningYear) {
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.joiningYear = joiningYear;
	}

	public int calculateExperience() {
		return 2024 - joiningYear;
	}


}
