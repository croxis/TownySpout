package net.croxis.townyspout.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.palmergames.bukkit.towny.db.SQLTown;



@Entity()
@Table(name = "townyspout_towns")
public class SQLTownx {
	@Id
    private int id;

	private String townName;
	
	private String texturePackURL;
	
	private String capeURL;
	
	private String musicURL;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public String getTexturePackURL() {
		return texturePackURL;
	}

	public void setTexturePackURL(String texturePackURL) {
		this.texturePackURL = texturePackURL;
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
