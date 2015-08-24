package hk.hku.cs.comp7502.config;

import java.util.List;

public class WorkshopConfig {
	private String workshopName;
	private List<WorkshopImageConfig> images;

	public String getWorkshopName() {
		return workshopName;
	}

	public void setWorkshopName(String workshopName) {
		this.workshopName = workshopName;
	}

	public List<WorkshopImageConfig> getImages() {
		return images;
	}

	public void setImages(List<WorkshopImageConfig> images) {
		this.images = images;
	}

}
