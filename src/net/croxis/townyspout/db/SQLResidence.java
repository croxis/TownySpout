package net.croxis.townyspout.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "townyspout_residences")
public class SQLResidence {
	@Id
    private int id;

	private String playerName;
	
	private String capeURL;
	
	private String musicURL;

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

	public String getCapeURL() {
		return capeURL;
	}

	public void setCapeURL(String capeURL) {
		this.capeURL = capeURL;
	}

	public String getMusicURL() {
		return musicURL;
	}

	public void setMusicURL(String musicURL) {
		this.musicURL = musicURL;
	}

	
}
