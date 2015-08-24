package hk.hku.cs.comp7502.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class ConfigReader {
	public static WorkshopConfig[] readWorkshopImgConfig() throws IOException {
		Gson gson = new Gson();
		WorkshopConfig[] config = gson.fromJson(new FileReader(new File("resources/predefined_imgs.json")), WorkshopConfig[].class);
		return config;
	}
	
	public static WorkshopProcessorConfig[] readWorkshopProcessorConfig() throws IOException {
		Gson gson = new Gson();
		WorkshopProcessorConfig[] config = gson.fromJson(new FileReader(new File("resources/image_processors.json")), WorkshopProcessorConfig[].class);
		return config;
	}
	
}
