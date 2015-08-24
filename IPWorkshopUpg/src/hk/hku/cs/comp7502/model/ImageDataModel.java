package hk.hku.cs.comp7502.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageDataModel {
	private String url;
	
	private String name;

	private final byte[] byteArray;
	
	private int width;
	private int height;
	
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
		
		this.byteArray = new byte[img.length];
		System.arraycopy(img, 0, this.byteArray, 0, img.length);
	}
	
	public ImageDataModel(BufferedImage bufImg) {
		this(bufImg.getWidth(), bufImg.getHeight(),  ((DataBufferByte) bufImg.getRaster().getDataBuffer()).getData());
	}
	
	/**
	 * convert the image data to BufferedImage,
	 * this method will copy data
	 * @return
	 */
	public BufferedImage toBufferedImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		byte[] array = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(byteArray, 0, array, 0, array.length);
		return image;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public byte getPixel(int x, int y) {
		return byteArray[x * width + y];
	}
	
	public void setPixel(int x, int y, byte val) {
		byteArray[x * width + y] = val;
	}

	public byte[] getByteArray() {
		return byteArray;
	}
	
	
	
}
