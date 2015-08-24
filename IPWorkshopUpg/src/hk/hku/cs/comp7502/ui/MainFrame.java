package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.command.OpenFileWorker;
import hk.hku.cs.comp7502.config.Configuration;
import hk.hku.cs.comp7502.config.WorkshopConfig;
import hk.hku.cs.comp7502.ui.util.MenuCreator;
import hk.hku.cs.comp7502.util.NativeUIUtils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame extends JFrame {
	
	private int openedImageNumber = 0;
	
    private JDesktopPane jdpDesktop;
    
	private static final long serialVersionUID = 3204111789442234205L;
	List<ImageInternalFrame> imageDocumentList = new ArrayList<> ();
	
	private MenuCreator menuCreator;
	private Configuration config = null;
	public MainFrame(Configuration config) {
		this.config = config;
		menuCreator = new MenuCreator(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setJMenuBar(createMenuBar());
		
        jdpDesktop = new JDesktopPane() {
			private static final long serialVersionUID = 1L;

			@Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 600);
            }
        };
        
        setContentPane(jdpDesktop);
        
        if (System.getProperty("os.name").startsWith("Mac OS X")) {
        	NativeUIUtils.enableOSXFullscreen(this);
        }
        
        setVisible(true);
	}
	

	protected JMenu createEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		JMenuItem undoItem = new JMenuItem("Undo");
		JMenuItem redoItem = new JMenuItem("Redo");
		return null;
	}
    
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem openImageMenuItem = new JMenuItem("Open Image from file...");
        JMenuItem openURLMenuItem = new JMenuItem("Open Image from URL...");
        
        
        openImageMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		JFileChooser jFileChooser = new JFileChooser();
        		jFileChooser.setCurrentDirectory(new File("~"));
        		FileFilter fileFilter = new FileNameExtensionFilter("image file", new String[] {"jpg", "jpeg", "bmp", "png", "gif"});
        		jFileChooser.setFileFilter(fileFilter);
        		
        		int result = jFileChooser.showOpenDialog(MainFrame.this);
        		
        		if (result == JFileChooser.APPROVE_OPTION) {
        		    File selectedFile = jFileChooser.getSelectedFile();
        		    URL fileURL = null;
        		    
					try {
						fileURL = selectedFile.toURI().toURL();
						ImageInternalFrame imgFrame = createFrame(fileURL.toString());
	    				OpenFileWorker worker = new OpenFileWorker(fileURL, imgFrame);
	    				worker.execute();
					} catch (MalformedURLException e1) {
						JOptionPane.showMessageDialog(MainFrame.this,
							    "The file is not accessible",
							    "Open Image",
							    JOptionPane.WARNING_MESSAGE);
						e1.printStackTrace();
					}
        		}
            }
        });
        
        openURLMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String urlString = JOptionPane.showInputDialog(MainFrame.this, "Please input the image URL (start with protocal)");
				if (urlString == null) {
					return;
				}
				
				OpenFileWorker worker = null;
				ImageInternalFrame imgFrame = createFrame(urlString);
				try {
					worker = new OpenFileWorker(new URL(urlString), imgFrame);
					worker.execute();
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(MainFrame.this,
						    "The URL is not supported",
						    "Open Image",
						    JOptionPane.WARNING_MESSAGE);
					imgFrame.dispose();
					e1.printStackTrace();
				}

			}
		});
        
        List<JMenu> workshopConfigItems = menuCreator.createPredifinedImageMenuItems((WorkshopConfig[]) config.getConfig("workshopConfig"));
        
        fileMenu.add(openImageMenuItem);
        fileMenu.add(openURLMenuItem);
        
        for (JMenu item : workshopConfigItems) {
        	fileMenu.add(item);
        }
        
        menuBar.add(fileMenu);
        return menuBar;
    }

    public ImageInternalFrame createFrame(String title) {
    	openedImageNumber++;
        ImageInternalFrame fr = new ImageInternalFrame(title, openedImageNumber);
        jdpDesktop.add(fr);
        
        try {
            fr.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        	e.printStackTrace();
        }
        
        return fr;
    }
    
}
