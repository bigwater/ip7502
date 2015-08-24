package hk.hku.cs.comp7502.config;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	private Map<String, Object> configMap = new HashMap<String, Object> ();
	
	public Object getConfig(String name) {
		if (name == null || !configMap.containsKey(name)) {
			throw new RuntimeException("config not exist");
		}
		
		return configMap.get(name);
	}
	
	public void setConfig(String name, Object config) {
		configMap.put(name, config);
	}

}




