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
	private URL url;
	private ImageInternalFrame parentFrame;

	private BufferedImage img;

	private long timeConsumed = 0;
	
	public OpenFileWorker(URL url, ImageInternalFrame parentFrame) {
		this.url = url;
		this.parentFrame = parentFrame;
	}

	@Override
	protected BufferedImage doInBackground() throws Exception {
		long startTime = System.currentTimeMillis();
		BufferedImage bufImage = null;

		try {
			bufImage = ImageIO.read(url);
			img = colorToGray(bufImage);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bufImage != null) {
			return colorToGray(bufImage);
		}

		long endTime = System.currentTimeMillis();
		timeConsumed = endTime - startTime;
		
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
		if (img == null) {
			parentFrame.getImgStatusLabel().setText("the url is not accessible");
		} else {
			ImagePanel parentImagePanel = parentFrame.getImagePanel();
			parentFrame.getImgStatusLabel().setText("load time consumed = " + timeConsumed + " milliseconds");
			//double defaultHeight = parentFrame.getImgStatusField().getSize().getHeight();
			//parentFrame.getImgStatusField().setSize(parentFrame.getSize().width-5, (int) defaultHeight);
			parentImagePanel.setBufImg(img);
			parentFrame.repaint();
			//parentImagePanel.repaint();
		}
	}

}


