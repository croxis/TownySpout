package net.croxis.townyspout.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ca.xshade.bukkit.towny.db.SQLTown;


@Entity()
@Table(name = "townyspout_towns")
public class SQLTownx {
	@Id
    private int id;
	
	@OneToOne
	private int townId;
	
	private String texturePackURL;
	
	private String capeURL;
	
	private String musicURL;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTownId() {
		return townId;
	}

	public void setTownId(int townId) {
		this.townId = townId;
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
