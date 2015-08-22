package hk.hku.cs.comp7502.model;

public class ImageModel {
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
	public ImageModel(int width, int height, byte[] img) {
		this.width = width;
		this.height = height;
		this.img = new byte[width * height];
		
	}
	
	
}
