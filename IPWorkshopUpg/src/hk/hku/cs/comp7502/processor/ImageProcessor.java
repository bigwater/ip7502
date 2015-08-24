package hk.hku.cs.comp7502.processor;

import hk.hku.cs.comp7502.model.ImageDataModel;

public interface ImageProcessor {
	/**
	 * a abstract interface that represents an operation on a certain image
	 * @param image
	 * @return
	 */
	public ImageDataModel process(ImageDataModel image);
}
