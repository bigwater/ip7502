package hk.hku.cs.comp7502.ui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ImageInternalFrame extends JInternalFrame {
	private static final long serialVersionUID = 1123712289181496599L;

	private ImagePanel imagePanel;

	private JLabel imgStatusLabel = new JLabel();
	
	static final int xPosition = 30, yPosition = 30;

	public ImageInternalFrame(String imageName, int openFrameCount) {
		super(imageName, true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable

		imagePanel = new ImagePanel();

		setSize(300, 300);

		getContentPane().add(imagePanel, BorderLayout.CENTER);
		getContentPane().add(imgStatusLabel, BorderLayout.SOUTH);

		setLocation(xPosition * openFrameCount, yPosition * openFrameCount);
		setVisible(true);
		
	}


	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
	}


	public JLabel getImgStatusLabel() {
		return imgStatusLabel;
	}

	
}
