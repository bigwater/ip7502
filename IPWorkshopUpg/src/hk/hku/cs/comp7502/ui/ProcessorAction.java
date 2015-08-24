package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.config.ProcessorConfig;
import hk.hku.cs.comp7502.processor.ImageProcessor;
import hk.hku.cs.comp7502.worker.ImageProcessingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.beanutils.BeanUtils;

public class ProcessorAction implements ActionListener {

	private ImagePanel panel;
	private ImageProcessor processor;
	private String processorName;
	private ProcessorConfig procConfig;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			if (panel != null) {
				ImageProcessingWorker worker = new ImageProcessingWorker(processorName, processor, panel);
				
				Map<String, Object> parameters = new HashMap<String, Object> ();
				Map<String, String>[] inputs = procConfig.getInputs();
				
				if (inputs != null) {
					for (Map<String, String> in : inputs) {
						String val = JOptionPane.showInputDialog(panel, "Please input parameter " + in.get("name"), in.get("default"));
						if (val == null) {
							return;
						}
						//System.out.println("val" + val);
						parameters.put(in.get("name"), Integer.valueOf(val));
						
//						if (in.get("type").equals("int")) {
//							parameters.put(in.get("name"), Integer.valueOf(val));
//						} else if (in.get("type").equals("double")) {
//							parameters.put(in.get("name"), Double.valueOf(val));
//						}
					}
					
					for (Map.Entry<String, Object> m : parameters.entrySet()) {
						//System.out.println( m.getKey() + "  " +m.getValue());
						BeanUtils.setProperty(processor, m.getKey(), m.getValue());
					}
				}
				
				worker.execute();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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

	public ProcessorConfig getProcConfig() {
		return procConfig;
	}

	public void setProcConfig(ProcessorConfig procConfig) {
		this.procConfig = procConfig;
	}

	
	
}
