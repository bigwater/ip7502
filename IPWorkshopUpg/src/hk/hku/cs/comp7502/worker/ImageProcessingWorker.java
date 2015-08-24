package hk.hku.cs.comp7502.worker;

import hk.hku.cs.comp7502.model.ImageDataModel;
import hk.hku.cs.comp7502.processor.ImageProcessor;
import hk.hku.cs.comp7502.ui.ImagePanel;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.undo.StateEdit;

public class ImageProcessingWorker extends SwingWorker<ImageDataModel, Integer> {
	
	private ImageProcessor processor;
	private ImagePanel imagePanel;
	
	private double timeConsumed = 0;
	
	String name;
	
	public ImageProcessingWorker(String name, ImageProcessor processor, ImagePanel imagePanel) {
		this.processor = processor;
		this.imagePanel = imagePanel;
		this.name = name;
	}
	
	@Override
	protected ImageDataModel doInBackground() throws Exception {
		long start = System.nanoTime();
		ImageDataModel imgModel = processor.process(new ImageDataModel(imagePanel.getBufImg()));
		double seconds = (System.nanoTime() - start) / 1000000000.0;
		timeConsumed = seconds;
		return imgModel;
	}
	
	@Override
	protected void done() {
		try {
			StateEdit stateEdit = new StateEdit(imagePanel);
			ImageDataModel m = get();
			imagePanel.setBufImg(m.toBufferedImage());
			stateEdit.end();
			imagePanel.getUndoableEditSupport().postEdit(stateEdit);
			imagePanel.repaint();
			imagePanel.refreshUndoAndRedo();
			imagePanel.updateStatusBar(String.format("%s costs = %.8f seconds", name, timeConsumed));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
