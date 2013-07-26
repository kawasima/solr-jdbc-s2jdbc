package net.unit8.solr.jdbc.extension.s2jdbc;

import net.unit8.solr.jdbc.extension.s2jdbc.entity.Player;
import net.unit8.solr.jdbc.extension.s2jdbc.service.PlayerService;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;


public class S2JDBCTest extends S2TestCase {
    private static final Logger logger = LoggerFactory.getLogger(S2JDBCTest.class);
	PlayerService playerService;
	JdbcManager jdbcManager;
	UserTransaction userTransaction;

	@Override
	public void setUp() {
		include("app.dicon");
	}


	public void testSearchSimple() throws Exception {
		try {
			jdbcManager.updateBySql("DROP TABLE PLAYER").execute();
		} catch(Exception ignore) {
            logger.warn("Drop failure", ignore);
		}
		jdbcManager.updateBySql(
                "CREATE TABLE PLAYER(" +
                        "PLAYER_ID number, TEAM varchar(10), PLAYER_NAME varchar(50), " +
                        "POSITION varchar(10) ARRAY, DESCRIPTION TEXT, REGISTERED_AT DATE)").execute();
		Player player1 = new Player();
		player1.playerId = 1;
		player1.playerName = "高橋慶彦";
		player1.team = "カープ";
		player1.position = CollectionsUtil.newArrayList();
		player1.position.add("二塁手");
		player1.position.add("遊撃手");
        player1.description = "高橋は、スイッチヒッターとして打率3割を5度、20本塁打以上を4度記録し、33試合連続安打という日本記録まで樹立した。この成功により、一躍日本球界にスイッチヒッターの有効性が知れ渡ることとなり、後進に計り知れない影響を及ぼした。";
        Date now = new Date();
        player1.registeredAt = now;
		userTransaction.begin();
		playerService.insert(player1);

		Player player2 = new Player();
		player2.playerId = 2;
		player2.playerName = "山崎隆造";
		player2.team = "カープ";
		player2.position = null;
        player2.description = "崇徳高校では3年春の甲子園で優勝（現・早稲田大学野球部監督の應武篤良は同期でチームメイト）。超高校級の遊撃手と騒がれ、1976年ドラフト1位で広島東洋カープに入団。";
		playerService.insert(player2);
		userTransaction.commit();

		List<Player> playerList = playerService.findAll();
		assertEquals(2, playerList.size());
		Player player = playerList.get(0);
		assertEquals("Entityに値が入っている", "高橋慶彦", player.playerName);
		assertEquals("日付が一致すること", now.getYear(), player.registeredAt.getYear());
        assertEquals("日付が一致すること", now.getMonth(), player.registeredAt.getMonth());
        assertEquals("日付が一致すること", now.getDate(), player.registeredAt.getDate());

		player.playerName = "野村謙二郎";
		userTransaction.begin();
		int res = playerService.update(player);
		userTransaction.commit();

		assertEquals("1件更新される", 1, res);

		player = playerService.find(1);
		assertEquals("playerNameの値が更新されている", "野村謙二郎", player.playerName);

	}

	public void test2WaySQL() throws Exception {
		try {
			jdbcManager.updateBySql("DROP TABLE PLAYER").execute();
		} catch(Exception ignore) {
            logger.warn("Drop failure", ignore);
		}
		jdbcManager.updateBySql(
                "CREATE TABLE PLAYER(" +
                        "PLAYER_ID number, TEAM varchar(10), PLAYER_NAME varchar(50), " +
                        "POSITION varchar(10) ARRAY, DESCRIPTION TEXT, REGISTERED_AT DATE)").execute();
		Player player1 = new Player();
		player1.playerId = 1;
		player1.playerName = "高橋慶彦";
		player1.team = "カープ";
		player1.position = CollectionsUtil.newArrayList();
		player1.position.add("二塁手");
		player1.position.add("遊撃手");
		userTransaction.begin();
		playerService.insert(player1);

        Player player2 = new Player();
		player2.playerId = 2;
		player2.playerName = "山崎隆造";
		player2.team = "カープ";
		player2.position = null;
		playerService.insert(player2);
		userTransaction.commit();

		List<Player> playerList = playerService.findBySql();
		assertEquals("二塁手", playerList.get(0).position.get(0));
		assertEquals("遊撃手", playerList.get(0).position.get(1));

	}
}
