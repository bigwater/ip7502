package hk.hku.cs.comp7502.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageDataModel {
	private String url;
	
	private String name;

	private final byte[] img;
	
	public final int width;
	public final int height;
	
	/**
	 * The constructor will copy the img byte
	 * @param width
	 * @param height
	 * @param img
	 */
	public ImageDataModel(int width, int height, byte[] img) {
		if (width * height != img.length) {
			throw new RuntimeException("width * height must equal img.length");
		}
		
		this.width = width;
		this.height = height;
		
		this.img = new byte[img.length];
		System.arraycopy(img, 0, this.img, 0, img.length);
	}
	
	/**
	 * convert the image data to BufferedImage
	 * @return
	 */
	public BufferedImage toBufferedImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		byte[] array = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(img, 0, array, 0, array.length);
		return image;
	}
}
