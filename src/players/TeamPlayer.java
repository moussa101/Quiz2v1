package players;

public class TeamPlayer extends Player {

	private String teamSport;
	private PlayerPosition position;

	public PlayerPosition getPosition() {
		return position;
	}

	public void setPosition(PlayerPosition position) {
		if (this.calculateExperience() >= 3)
			this.position = position;
	}

	public String getTeamSport() {
		return teamSport;
	}

	public TeamPlayer(String name, Gender gender, int age, int joiningYear,
			String teamSport, PlayerPosition position) {
		super(name, gender, age, joiningYear);
		this.teamSport = teamSport;
		this.position = position;
	}

}
