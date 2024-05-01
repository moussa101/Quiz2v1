package players;

public class IndividualPlayer extends Player {

	private IndividualSportTypes sport;
	private int numOfMedals;

	public IndividualPlayer(String name, Gender gender, int age,
			int joiningYear, IndividualSportTypes sport, int numOfMedals) {
		super(name, gender, age, joiningYear);
		this.sport = sport;
		this.numOfMedals = numOfMedals;
		// TODO Auto-generated constructor stub
	}

	public boolean isExperienced() {
		if (this.calculateExperience() >= 10)
			return true;
		else
			return false;
	}

	public int getNumOfMedals() {
		return numOfMedals;
	}

	public void setNumOfMedals(int numOfMedals) {
		this.numOfMedals = numOfMedals;
	}

	public IndividualSportTypes getSport() {
		return sport;
	}

}
