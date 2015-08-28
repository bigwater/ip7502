package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.model.ImageDataModel;
import hk.hku.cs.comp7502.util.ImageConverter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	
	private int xBase = 6;
	private int yBase = 6;
	
	private boolean showAxis = true;
	
	private static final String IMAGE_PANEL_STATE_KEY = "ImageKey";
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
				x = x - xBase;
				y = y - yBase;
				if (parent.isSelected() && bufImg != null) {
					if (x >= 0 && x < bufImg.getWidth() + xBase && y >= 0 && y < bufImg.getHeight() + yBase) {
						parent.getImgStatusLabel().setText(String.format("(w = %d, h = %d)", x, y));
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
	
	private void drawArrow( Graphics2D g, int x, int y, int xx, int yy) {
	    float arrowWidth = 10.0f ;
	    float theta = 0.423f ;
	    int[] xPoints = new int[ 3 ] ;
	    int[] yPoints = new int[ 3 ] ;
	    float[] vecLine = new float[ 2 ] ;
	    float[] vecLeft = new float[ 2 ] ;
	    float fLength;
	    float th;
	    float ta;
	    float baseX, baseY ;

	    xPoints[ 0 ] = xx ;
	    yPoints[ 0 ] = yy ;

	    // build the line vector
	    vecLine[ 0 ] = (float)xPoints[ 0 ] - x ;
	    vecLine[ 1 ] = (float)yPoints[ 0 ] - y ;

	    // build the arrow base vector - normal to the line
	    vecLeft[ 0 ] = -vecLine[ 1 ] ;
	    vecLeft[ 1 ] = vecLine[ 0 ] ;

	    // setup length parameters
	    fLength = (float)Math.sqrt( vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1] ) ;
	    th = arrowWidth / ( 2.0f * fLength ) ;
	    ta = arrowWidth / ( 2.0f * ( (float)Math.tan( theta ) / 2.0f ) * fLength ) ;

	    // find the base of the arrow
	    baseX = ( (float)xPoints[ 0 ] - ta * vecLine[0]);
	    baseY = ( (float)yPoints[ 0 ] - ta * vecLine[1]);

	    // build the points on the sides of the arrow
	    xPoints[ 1 ] = (int)( baseX + th * vecLeft[0] );
	    yPoints[ 1 ] = (int)( baseY + th * vecLeft[1] );
	    xPoints[ 2 ] = (int)( baseX - th * vecLeft[0] );
	    yPoints[ 2 ] = (int)( baseY - th * vecLeft[1] );

	    g.drawLine( x, y, (int)baseX, (int)baseY ) ;
	    g.fillPolygon( xPoints, yPoints, 3 ) ;
	  }
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (bufImg != null) {
			g2d.drawImage(bufImg, xBase, yBase, null);
			if (showAxis) {
				g2d.setColor(new Color(255, 0, 0));
				drawArrow(g2d, xBase, yBase, bufImg.getWidth() + 30 + yBase, yBase);
				drawArrow(g2d, xBase, yBase, xBase, bufImg.getHeight() + 30 + yBase);
				
				g2d.drawString("x", bufImg.getWidth() + 35 + yBase, yBase + 5);
				g2d.drawString("y", xBase + 5, bufImg.getHeight() + 35 + yBase);
			}
		}
	}

	public BufferedImage getBufImg() {
		return bufImg;
	}

	public void setBufImg(BufferedImage bufImg) {
		setPreferredSize(new Dimension(bufImg.getWidth() + xBase * 2, bufImg.getHeight() + yBase * 2));
		parent.setSize(new Dimension(bufImg.getWidth() + 100, bufImg.getHeight() + 100 ));
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
	
	public void setShowAxis(boolean b) {
		showAxis = b;
	}
}



