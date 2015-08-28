package hk.hku.cs.comp7502.worker;

import hk.hku.cs.comp7502.ui.ImageInternalFrame;
import hk.hku.cs.comp7502.ui.ImagePanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class OpenFileWorker extends SwingWorker<BufferedImage, Integer> {
	private URL url;
	private ImageInternalFrame parentFrame;

	private double timeConsumed = 0;
	
	public OpenFileWorker(URL url, ImageInternalFrame parentFrame) {
		this.url = url;
		this.parentFrame = parentFrame;
	}

	@Override
	protected BufferedImage doInBackground() throws Exception {
		
		long start = System.nanoTime();
		BufferedImage bufImage = null;

		try {
			bufImage = ImageIO.read(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bufImage != null) {
			double seconds = (System.nanoTime() - start) / 1000000000.0;
			timeConsumed = seconds;
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
		BufferedImage img = null;
		try {
			img = get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		if (img == null) {
			parentFrame.getImgStatusLabel().setText("the url is not accessible");
		} else {
			ImagePanel parentImagePanel = parentFrame.getImagePanel();
			parentFrame.getImgStatusLabel().setText(String.format("load time consumed = %.8f seconds", timeConsumed));
			parentImagePanel.setBufImg(img);
			parentFrame.repaint();
		}
	}
}


