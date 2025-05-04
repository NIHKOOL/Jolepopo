package logic;

import java.util.List;

public class PlayerTeamManager {
	private final List<entities.Character> team;
	private int currentIndex = 0;
	
	public PlayerTeamManager(List<entities.Character> team) {
		this.team = team;
	}
	
	public entities.Character getCurrentCharacter() {
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
	
	public List<entities.Character> getTeam() {
		return team;
	}
	
}
