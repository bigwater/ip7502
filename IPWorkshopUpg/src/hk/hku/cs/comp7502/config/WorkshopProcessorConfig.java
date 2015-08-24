package hk.hku.cs.comp7502.config;

import java.util.List;

public class WorkshopProcessorConfig {
	private String workshopName;
	private List<ProcessorConfig> processors;

	public String getWorkshopName() {
		return workshopName;
	}

	public void setWorkshopName(String workshopName) {
		this.workshopName = workshopName;
	}

	public List<ProcessorConfig> getProcessors() {
		return processors;
	}

	public void setProcessors(List<ProcessorConfig> processors) {
		this.processors = processors;
	}

}
