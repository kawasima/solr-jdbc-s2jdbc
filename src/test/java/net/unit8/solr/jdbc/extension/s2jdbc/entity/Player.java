package net.unit8.solr.jdbc.extension.s2jdbc.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Player {
	@Id
	public Integer playerId;
	public String playerName;

	public String team;
	public List<String> position;

    @Lob
    public String description;

    @Temporal(TemporalType.DATE)
	public Date registeredAt;
}
