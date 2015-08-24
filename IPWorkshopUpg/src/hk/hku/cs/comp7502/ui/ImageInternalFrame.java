package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.worker.ImageProcessingWorker;
import hk.hku.cs.comp7502.workshop.NegativeTransformationProcessor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.undo.UndoManager;

public class ImageInternalFrame extends JInternalFrame {
	private static final long serialVersionUID = 1123712289181496599L;

	private ImagePanel imagePanel;

	static final int xPosition = 30, yPosition = 30;

	private MainFrame mainFrame;
	private UndoManager undoManager;
	
	public ImageInternalFrame(String imageName, int openFrameCount, MainFrame mainFrame) {
		super(imageName, true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable
		
		this.mainFrame = mainFrame;
		undoManager = new UndoManager();
		imagePanel = new ImagePanel(mainFrame, this);
		imagePanel.addUndoableEditListener(undoManager);
		
		JScrollPane scrollFrame = new JScrollPane(imagePanel);
		scrollFrame.setPreferredSize(new Dimension(300,300));
		
		imagePanel.setAutoscrolls(true);
		
		JToolBar toolbar = new JToolBar();
		JButton gegegeButton = new JButton("gegege");
		toolbar.add(gegegeButton);
		gegegeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageProcessingWorker worker = new ImageProcessingWorker("gege", new NegativeTransformationProcessor(), imagePanel);
				worker.execute();
			}
		});
		
		setSize(300, 300);

		getContentPane().add(scrollFrame, BorderLayout.CENTER);

		add(toolbar, BorderLayout.NORTH);

		setLocation(xPosition * openFrameCount, yPosition * openFrameCount);
		
		addInternalFrameListener(new InternalFrameAdapter() {
		    public void internalFrameClosed(InternalFrameEvent e) {
		    	if (mainFrame.getOpenedFrameNumber() == 0) {
		    		mainFrame.getSaveAsMenuItem().setEnabled(false);
		    	}
		    }

			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				mainFrame.refreshUndoAndRedo(undoManager);
			}
			
			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				mainFrame.refreshUndoAndRedo(undoManager);
				System.out.println("opened");
			}
			
			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				mainFrame.getImgStatusLabel().setText("");
			}
		});
		
		setVisible(true);

	}

	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public JLabel getImgStatusLabel() {
		return mainFrame.getImgStatusLabel();
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

}

