package praxis.slipcor.pvpstats.uuid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import praxis.slipcor.pvpstats.PSMySQL;
import praxis.slipcor.pvpstats.PVPStats;

public class UUIDUpdater {

	public UUIDUpdater(PVPStats plugin, String dbTable) {
		if (PVPStats.useUUIDs && !plugin.getConfig().getBoolean("UUIDs-(do not change!)")) {
		
			List<String> players = PSMySQL.getAllPlayers(dbTable);
			
			if (players != null && players.size() > 0) {
			
				UUIDFetcher fetcher = new UUIDFetcher(players);
				
				try {
					Map<String, UUID> result = fetcher.call();
					for (Entry<String, UUID> set : result.entrySet()) {
						add(dbTable, set);
					}
					commit(dbTable);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			plugin.getConfig().set("UUIDs-(do not change!)", true);
			plugin.saveConfig();
		}
	}
	
	private void commit(String dbTable) {
		PSMySQL.commit(dbTable, map);
		
		map.clear();
	}

	private final Map<String, UUID> map = new HashMap<String, UUID>();

	private void add(String dbTable, Entry<String, UUID> set) {
		map.put(set.getKey(), set.getValue());
		if (map.size() > 9) {
			commit(dbTable);
		}
	}
}
