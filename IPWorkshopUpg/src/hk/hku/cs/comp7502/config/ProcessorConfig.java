package hk.hku.cs.comp7502.config;

import hk.hku.cs.comp7502.processor.ImageProcessor;

public class ProcessorConfig {
	private String name;
	private String clazz;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public ImageProcessor getImageProcessor() {
		ImageProcessor proc = null;
		try {
			Class cl = Class.forName(clazz);
			proc = (ImageProcessor) cl.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return proc;
	}
}
