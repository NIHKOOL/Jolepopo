package logic;

import java.util.List;
import entities.Character;

public class PlayerTeamManager {
	private final List<Character> team;
	private int currentIndex = 0;

	public PlayerTeamManager(List<Character> team) {
		this.team = team;
	}

	public Character getCurrentCharacter() {
		return team.get(currentIndex);
	}

	public void switchToNext() {
		currentIndex = (currentIndex + 1) % team.size();
	}

	public void switchTo(int index) {
		if (index >= 0 && index < team.size()) {
			currentIndex = index;
		}
	}

	public List<Character> getTeam() {
		return team;
	}
}
