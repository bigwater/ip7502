package hk.hku.cs.comp7502.workshop;

import hk.hku.cs.comp7502.model.ImageDataModel;
import hk.hku.cs.comp7502.processor.ImageProcessor;

public class BoxFilter implements ImageProcessor {
	private int filtersize;
	@Override
	public ImageDataModel process(ImageDataModel image) {
		//System.out.println(filtersize);
		byte[] origImg = image.getByteArray();
		byte[] img = new byte[origImg.length];
		System.arraycopy(origImg, 0, img, 0, img.length);
		
		int range = filtersize / 2;
		
		for (int currentX = 0; currentX < image.getHeight(); currentX ++) {
			for (int currentY = 0; currentY < image.getWidth(); currentY ++) {
				int sum = 0;
				for (int u = currentX - range; u <= currentX + range; u ++) {
					for (int v = currentY - range; v <= currentY + range; v++) {
						if (u >= 0 && u < image.getHeight() && v >= 0 && v < image.getWidth()) {
							sum += origImg[u * image.getWidth() + v] & 0xFF;
						}
					}
				}
				
				int avg = sum / (filtersize * filtersize);
				
				img[currentX * image.getWidth() + currentY] = (byte) avg;
			}
		
		}
		
		System.arraycopy(img, 0, image.getByteArray(), 0, img.length);
		return image;
	}
	
	public int getFiltersize() {
		return filtersize;
	}
	public void setFiltersize(int filtersize) {
		this.filtersize = filtersize;
	}
}
