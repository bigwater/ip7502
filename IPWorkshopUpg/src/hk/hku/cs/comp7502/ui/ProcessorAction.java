package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.processor.ImageProcessor;
import hk.hku.cs.comp7502.worker.ImageProcessingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProcessorAction implements ActionListener {

	private ImagePanel panel;
	private ImageProcessor processor;
	private String processorName;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (panel != null) {
			ImageProcessingWorker worker = new ImageProcessingWorker(processorName, processor, panel);
			worker.execute();
		}
	}

	public ImagePanel getPanel() {
		return panel;
	}

	public void setPanel(ImagePanel panel) {
		this.panel = panel;
	}

	public ImageProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(ImageProcessor processor) {
		this.processor = processor;
	}

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

}
