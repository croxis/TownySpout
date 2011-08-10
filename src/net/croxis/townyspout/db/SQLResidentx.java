package net.croxis.townyspout.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "townyspout_residents")
public class SQLResidentx {
	@Id
    private int id;

	private String playerName;
	
	private boolean repeateMusic;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public boolean isRepeateMusic() {
		return repeateMusic;
	}

	public void setRepeateMusic(boolean repeateMusic) {
		this.repeateMusic = repeateMusic;
	}
	
	

}
