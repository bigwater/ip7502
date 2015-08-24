package hk.hku.cs.comp7502.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.StateEdit;
import javax.swing.undo.UndoManager;

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

		UndoManager manager = new UndoManager();

		imagePanel = new ImagePanel();
		imagePanel.addUndoableEditListener(manager);
		
		JScrollPane scrollFrame = new JScrollPane(imagePanel);
		scrollFrame.setPreferredSize(new Dimension(300,300));
		
		imagePanel.setAutoscrolls(true);
		
		
		JToolBar toolbar = new JToolBar();
		JButton undoButton = new JButton(UndoManagerHelper.getUndoAction(manager));
		toolbar.add(undoButton);
		JButton redoButton = new JButton(UndoManagerHelper.getRedoAction(manager));
		toolbar.add(redoButton);

		JButton gegegeButton = new JButton("gegege");
		toolbar.add(gegegeButton);
		gegegeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        StateEdit stateEdit = new StateEdit(imagePanel);
				for (int i = 0; i < 100; i++) {
					for (int j = 0; j < 100; j++) {
						imagePanel.getBufImg().setRGB(i, j, 0);
					}
				}
		        stateEdit.end();
		        imagePanel.repaint();
		        imagePanel.undoableEditSupport.postEdit(stateEdit);				
			}
		});
		
		setSize(300, 300);

		getContentPane().add(scrollFrame, BorderLayout.CENTER);
		getContentPane().add(imgStatusLabel, BorderLayout.SOUTH);

		add(toolbar, BorderLayout.NORTH);

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

	@Override
	public void dispose() {
		//System.out.println("dispose image internalframe");
		super.dispose();
	}

}

class UndoManagerHelper {

	public static Action getUndoAction(UndoManager manager, String label) {
		return new UndoAction(manager, label);
	}

	public static Action getUndoAction(UndoManager manager) {
		return new UndoAction(manager, (String) UIManager.get("AbstractUndoableEdit.undoText"));
	}

	public static Action getRedoAction(UndoManager manager, String label) {
		return new RedoAction(manager, label);
	}

	public static Action getRedoAction(UndoManager manager) {
		return new RedoAction(manager, (String) UIManager.get("AbstractUndoableEdit.redoText"));
	}

	private abstract static class UndoRedoAction extends AbstractAction {
		UndoManager undoManager = new UndoManager();

		String errorMessage = "Cannot undo";

		String errorTitle = "Undo Problem";

		protected UndoRedoAction(UndoManager manager, String name) {
			super(name);
			undoManager = manager;
		}

		public void setErrorMessage(String newValue) {
			errorMessage = newValue;
		}

		public void setErrorTitle(String newValue) {
			errorTitle = newValue;
		}

		protected void showMessage(Object source) {
			if (source instanceof Component) {
				JOptionPane
						.showMessageDialog((Component) source, errorMessage, errorTitle, JOptionPane.WARNING_MESSAGE);
			} else {
				System.err.println(errorMessage);
			}
		}
	}

	public static class UndoAction extends UndoRedoAction {
		public UndoAction(UndoManager manager, String name) {
			super(manager, name);
			setErrorMessage("Cannot undo");
			setErrorTitle("Undo Problem");
		}

		public void actionPerformed(ActionEvent actionEvent) {
			try {
				undoManager.undo();
			} catch (CannotUndoException cannotUndoException) {
				showMessage(actionEvent.getSource());
			}
		}
	}

	public static class RedoAction extends UndoRedoAction {
		public RedoAction(UndoManager manager, String name) {
			super(manager, name);
			setErrorMessage("Cannot redo");
			setErrorTitle("Redo Problem");
		}

		public void actionPerformed(ActionEvent actionEvent) {
			try {
				undoManager.redo();
			} catch (CannotRedoException cannotRedoException) {
				showMessage(actionEvent.getSource());
			}
		}
	}
}
