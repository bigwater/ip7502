package hk.hku.cs.comp7502.ui.util;

import hk.hku.cs.comp7502.config.ProcessorConfig;
import hk.hku.cs.comp7502.config.WorkshopConfig;
import hk.hku.cs.comp7502.config.WorkshopImageConfig;
import hk.hku.cs.comp7502.config.WorkshopProcessorConfig;
import hk.hku.cs.comp7502.ui.ImageInternalFrame;
import hk.hku.cs.comp7502.ui.MainFrame;
import hk.hku.cs.comp7502.ui.ProcessorAction;
import hk.hku.cs.comp7502.worker.OpenFileWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuCreator {
	MainFrame mainFrame;

	public MenuCreator(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public List<JMenu> createPredifinedImageMenuItems(WorkshopConfig[] workshopConfig) {
		List<JMenu> menuItems = new ArrayList<JMenu>();
		for (WorkshopConfig wc : workshopConfig) {
			JMenu workshopItem = new JMenu("Open Image from " + wc.getWorkshopName());
			for (WorkshopImageConfig img : wc.getImages()) {
				JMenuItem imgItem = new JMenuItem(img.getName());
				workshopItem.add(imgItem);
				imgItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String urlString = img.getUrl();
						OpenFileWorker worker = null;
						ImageInternalFrame imgFrame = mainFrame.createFrame(urlString);
						try {
							worker = new OpenFileWorker(new URL(urlString), imgFrame);
							worker.execute();
						} catch (MalformedURLException e1) {
							JOptionPane.showMessageDialog(mainFrame, "The URL is not accessible", "Open Image",
									JOptionPane.WARNING_MESSAGE);
						
							imgFrame.dispose();
							e1.printStackTrace();
						}

					}
				});
				
			}

			menuItems.add(workshopItem);
		}

		return menuItems;
	}
	
	public List<JMenu> createWorkshopOperationsMenuItems(WorkshopProcessorConfig[] procConfig) {
		List<JMenu> menuList = new ArrayList<JMenu> ();
		for (WorkshopProcessorConfig pc : procConfig) {
			JMenu menu = new JMenu(pc.getWorkshopName());
			for (ProcessorConfig p : pc.getProcessors()) {
				JMenuItem item = new JMenuItem(p.getName());
				ProcessorAction listener = new ProcessorAction();
				listener.setProcessor(p.getImageProcessor());
				listener.setProcessorName(p.getName());
				listener.setPanel(mainFrame.getCurrentImagePanel());
				item.addActionListener(listener);
				//itemActionMap.put(item, listener);
				menu.add(item);
				//item.setEnabled(false);
			}
			menuList.add(menu);
		}
		return menuList;
	}
}
