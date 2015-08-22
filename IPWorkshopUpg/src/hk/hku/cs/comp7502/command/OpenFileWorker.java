package hk.hku.cs.comp7502.command;

import hk.hku.cs.comp7502.ui.ImageInternalFrame;
import hk.hku.cs.comp7502.ui.ImagePanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class OpenFileWorker extends SwingWorker<BufferedImage, Integer> {
	private String url;
	private ImageInternalFrame parentFrame;

	private BufferedImage img;

	public OpenFileWorker(String url, ImageInternalFrame parentFrame) {
		this.url = url;
		this.parentFrame = parentFrame;
	}

	@Override
	protected BufferedImage doInBackground() throws Exception {
		BufferedImage bufImage = null;

		try {
			bufImage = ImageIO.read(new URL(url));
			img = bufImage;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bufImage != null) {
			return colorToGray(bufImage);
		}

		return null;
	}

	public BufferedImage colorToGray(BufferedImage source) {
		BufferedImage grayImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = grayImage.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return grayImage;
	}

	@Override
	protected void done() {
		// Executed on the Event Dispatch Thread after the doInBackground method is finished.
		ImagePanel parentImagePanel = parentFrame.getImagePanel();
		if (img != null) {
			parentImagePanel.setBufImg(img);
		}
	}

}
