package hk.hku.cs.comp7502.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageConverter {
	
	/**
	 * the method will not copy data
	 * @param bufImg
	 * @return
	 */
	public static byte[] toByteArray(BufferedImage bufImg) {
		byte[] imgData = ((DataBufferByte)bufImg.getRaster().getDataBuffer()).getData();
		return imgData;
	}
	
}
