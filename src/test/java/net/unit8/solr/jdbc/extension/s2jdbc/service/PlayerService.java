package net.unit8.solr.jdbc.extension.s2jdbc.service;

import java.util.List;

import org.seasar.extension.jdbc.name.PropertyName;
import org.seasar.extension.jdbc.service.S2AbstractService;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.tiger.CollectionsUtil;
import static org.seasar.extension.jdbc.operation.Operations.*;

import net.unit8.solr.jdbc.extension.s2jdbc.entity.Player;


public class PlayerService extends S2AbstractService<Player> {

	public Player find(int id) {
		BeanMap conditions = new BeanMap();
		conditions.put("playerId", id);
		List<Player> playerList = findByCondition(conditions);
		if(playerList.size() > 0) {
			return playerList.get(0);
		}
		return null;
	}

    @Override
    public List<Player> findAll() {
        return select()
                .orderBy(asc(new PropertyName<Long>("playerId"))).getResultList();
    }

	public List<Player> findBySql() {
		BeanMap conditions = new BeanMap();
		List<String> positionList = CollectionsUtil.newArrayList();
		positionList.add("二塁手");
		positionList.add("遊撃手");
		conditions.put("position", positionList);
		return selectBySqlFile(Player.class,"findPosition.sql", conditions).getResultList();
	}

    public List<Player> findByDescription(String description) {
        return select()
                .where(contains(new PropertyName<String>("description"), description))
                .getResultList();
    }

    public List<Player> findByNameStartsWith(String nameStarts) {
        return select()
                .where(starts(new PropertyName<String>("playerName"), nameStarts))
                .getResultList();
    }

    /** This method throws exception. */
    public List<Player> findByDescriptionStarts(String description) {
        return select()
                .where(starts(new PropertyName<String>("description"), description))
                .getResultList();
    }

    /** This method throws exception. */
    public List<Player> findByNameContains(String nameStarts) {
        return select()
                .where(contains(new PropertyName<String>("playerName"), nameStarts))
                .getResultList();
    }

}
