package hk.hku.cs.comp7502.workshop;

import hk.hku.cs.comp7502.model.ImageDataModel;
import hk.hku.cs.comp7502.processor.ImageProcessor;

public class NegativeTransformationProcessor implements ImageProcessor {

	@Override
	public ImageDataModel process(ImageDataModel image) {
		for (int i = 0; i < image.getHeight(); i++ ) {
			for (int j = 0; j < image.getWidth(); j++) {
				byte nVal = (byte) (255 - (image.getPixel(i, j) & 0xFF));
				image.setPixel(i, j, nVal);
			}
		}
		
		return image;
	}

}


