package hk.hku.cs.comp7502.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class ConfigReader {
	public static WorkshopConfig[] readWorkshopConfig() throws IOException {
		Gson gson = new Gson();
		WorkshopConfig[] config = gson.fromJson(new FileReader(new File("resources/predefined_imgs.json")), WorkshopConfig[].class);
		return config;
	}
	
	public static void main(String[] args) throws Exception {
		ConfigReader r = new ConfigReader();
		WorkshopConfig[] config = r.readWorkshopConfig();
		Gson gson = new Gson();
		System.out.println(gson.toJson(config));
	}
}
