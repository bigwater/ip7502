package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.model.ImageDataModel;
import hk.hku.cs.comp7502.util.ImageConverter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoableEditSupport;

public class ImagePanel extends JPanel implements StateEditable {
	private static final long serialVersionUID = 8578099555635463985L;

	private ImageInternalFrame parent;
	private MainFrame mainFrame;
	
	static final String IMAGE_PANEL_STATE_KEY = "ImageKey";
	private BufferedImage bufImg;

	private UndoableEditSupport undoableEditSupport = new UndoableEditSupport(this);

	public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoableEditSupport.addUndoableEditListener(undoableEditListener);
	}

	public void removeUndoableEditListener(UndoableEditListener undoableEditListener) {
		undoableEditSupport.removeUndoableEditListener(undoableEditListener);
	}

	public ImagePanel(MainFrame mainFrame, ImageInternalFrame parent) {
		this.parent = parent;
		this.mainFrame = mainFrame;
		
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX(); // width
				int y = e.getY(); // height
				
				if (parent.isSelected() && bufImg != null) {
					if (x >= 0 && x < bufImg.getWidth() && y >= 0 && y < bufImg.getHeight()) {
						parent.getImgStatusLabel().setText(String.format("(w = %d, h = %d)", e.getX(), e.getY()));
					} else {
						parent.getImgStatusLabel().setText("");
					}
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
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
		state.put(IMAGE_PANEL_STATE_KEY, new ImageDataModel(bufImg.getWidth(), bufImg.getHeight(), ImageConverter.toByteArray(bufImg)));
		mainFrame.refreshUndoAndRedo(parent.getUndoManager());
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		ImageDataModel storedImg = (ImageDataModel) state.get(IMAGE_PANEL_STATE_KEY);
		if (storedImg != null) {
			bufImg = storedImg.toBufferedImage();
			repaint();
		}
		mainFrame.refreshUndoAndRedo(parent.getUndoManager());
	}

	public UndoableEditSupport getUndoableEditSupport() {
		return undoableEditSupport;
	}

	public void setUndoableEditSupport(UndoableEditSupport undoableEditSupport) {
		this.undoableEditSupport = undoableEditSupport;
	}
	
	public void refreshUndoAndRedo() {
		mainFrame.refreshUndoAndRedo(parent.getUndoManager());
	}

	public void updateStatusBar(String text) {
		mainFrame.getImgStatusLabel().setText(text);
	}
}



