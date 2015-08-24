package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.model.ImageDataModel;
import hk.hku.cs.comp7502.util.ImageConverter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoableEditSupport;

public class ImagePanel extends JPanel implements StateEditable {
	private static final long serialVersionUID = 8578099555635463985L;

	static final String IMAGE_PANEL_STATE_KEY = "ImageKey";
	private BufferedImage bufImg;

	UndoableEditSupport undoableEditSupport = new UndoableEditSupport(this);

	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoableEditSupport.addUndoableEditListener(undoableEditListener);
	}

	public void removeUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoableEditSupport.removeUndoableEditListener(undoableEditListener);
	}

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
		setPreferredSize(new Dimension(bufImg.getWidth(), bufImg.getHeight()));
		this.bufImg = bufImg;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		System.out.println("store state");
		state.put(IMAGE_PANEL_STATE_KEY, new ImageDataModel(bufImg.getWidth(), bufImg.getHeight(), ImageConverter.toByteArray(bufImg)));
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		System.out.println("restore state");
		ImageDataModel storedImg = (ImageDataModel) state.get(IMAGE_PANEL_STATE_KEY);
		if (storedImg != null) {
			bufImg = storedImg.toBufferedImage();
			repaint();
		}
	}
	
}



