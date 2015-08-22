package hk.hku.cs.comp7502.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 8578099555635463985L;
	
	private BufferedImage bufImg;
	
	public ImagePanel() {
		//setSize(100,100);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (bufImg != null) {
			g2d.drawImage(bufImg, 0, 0, null);
		}
	}

	public BufferedImage getBufImg() {
		return bufImg;
	}

	public void setBufImg(BufferedImage bufImg) {
		this.bufImg = bufImg;
	}
	
	
}
