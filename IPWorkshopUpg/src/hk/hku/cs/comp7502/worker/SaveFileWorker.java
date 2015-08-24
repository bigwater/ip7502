package hk.hku.cs.comp7502.worker;

import hk.hku.cs.comp7502.ui.ImagePanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class SaveFileWorker extends SwingWorker<Boolean, Integer> {
	private String url;
	private BufferedImage img;
	private ImagePanel panel;
	
	public SaveFileWorker(String url, BufferedImage bufImage, ImagePanel panel) {
		img = bufImage;
		this.url = url;
		this.panel = panel;
	}

	@Override
	protected Boolean doInBackground() {
		try {
			File outputfile = new File(url);
			outputfile.getAbsoluteFile();
			if (!outputfile.exists()) {
				outputfile.createNewFile();
			}
			ImageIO.write(img, "jpg", outputfile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void done() {
		try {
			if (get()) {
				panel.updateStatusBar("saved to " + url);
			} else {
				panel.updateStatusBar("saved fail");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
