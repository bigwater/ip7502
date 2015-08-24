package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.config.Configuration;
import hk.hku.cs.comp7502.config.WorkshopConfig;
import hk.hku.cs.comp7502.ui.util.MenuCreator;
import hk.hku.cs.comp7502.util.NativeUIUtils;
import hk.hku.cs.comp7502.worker.OpenFileWorker;
import hk.hku.cs.comp7502.worker.SaveFileWorker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class MainFrame extends JFrame {
	
	private int openedImageNumber = 0;
	
    private JDesktopPane jdpDesktop;
    
	private JLabel imgStatusLabel = new JLabel();

	private static final long serialVersionUID = 3204111789442234205L;
	List<ImageInternalFrame> imageDocumentList = new ArrayList<> ();
	
	private MenuCreator menuCreator;
	private Configuration config = null;
	
	private JMenuItem undoItem, redoItem, saveAsMenuItem;
	
	public MainFrame(Configuration config) {
		this.config = config;
		menuCreator = new MenuCreator(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setJMenuBar(createMenuBar());
		
        jdpDesktop = new JDesktopPane();
        
        add(jdpDesktop, BorderLayout.CENTER);
        
        add(imgStatusLabel, BorderLayout.SOUTH);
		

        if (System.getProperty("os.name").startsWith("Mac OS X")) {
        	NativeUIUtils.enableOSXFullscreen(this);
        }
        
        setVisible(true);
	}

	protected JMenu createEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		setEnableUndoRedo(false, false);
		return editMenu;
	}
	
	public void refreshUndoAndRedo(UndoManager manager) {
		setEnableUndoRedo(manager.canUndo(), manager.canRedo());
		for (ActionListener a : undoItem.getActionListeners()) {
			undoItem.removeActionListener(a);
		}
		for (ActionListener a : redoItem.getActionListeners()) {
			redoItem.removeActionListener(a);
		}
		
		undoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.undo();
			}
		});
		
		redoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.redo();
			}
		});
		
	}
	
	public void setEnableUndoRedo(boolean u, boolean r) {
		undoItem.setEnabled(u);
		redoItem.setEnabled(r);
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		undoItem.setAccelerator(stroke);
		
		KeyStroke stroke1 = KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		redoItem.setAccelerator(stroke1);
		
	}
	
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
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
        
        fileMenu.addSeparator();
        saveAsMenuItem = new JMenuItem("Save As...");
        fileMenu.add(saveAsMenuItem);
        saveAsMenuItem.setEnabled(false);
        saveAsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JInternalFrame[] frames = jdpDesktop.getAllFrames();
				ImageInternalFrame f = null;
				for (JInternalFrame frame : frames) {
					if (frame.isSelected()) {
						f = (ImageInternalFrame) frame;
						break;
					}
				}
				
				if (f == null) {
					saveAsMenuItem.setEnabled(false);
					return;
				}
				
				JFileChooser jFileChooser = new JFileChooser();
        		jFileChooser.setCurrentDirectory(new File("~"));
        		int result = jFileChooser.showSaveDialog(MainFrame.this);
        		
        		if (result == JFileChooser.APPROVE_OPTION) {
        			File fileToSave = jFileChooser.getSelectedFile();
					String url = fileToSave.getAbsolutePath();
					SaveFileWorker worker = new SaveFileWorker(url, f.getImagePanel().getBufImg(), f.getImagePanel());
					worker.execute();
        		}
				
			}
		});
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        menuBar.add(createEditMenu());
        return menuBar;
    }

    public ImageInternalFrame createFrame(String title) {
    	saveAsMenuItem.setEnabled(true);
    	
    	openedImageNumber++;
        ImageInternalFrame fr = new ImageInternalFrame(title, openedImageNumber, this);
        jdpDesktop.add(fr);
        
        try {
            fr.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        	e.printStackTrace();
        }
        
        return fr;
    }
    
	public JLabel getImgStatusLabel() {
		return imgStatusLabel;
	}
	
	public void setImgStatusLabel(JLabel imgStatusLabel) {
		this.imgStatusLabel = imgStatusLabel;
	}
	
	public JMenuItem getSaveAsMenuItem() {
		return saveAsMenuItem;
	}
	
	public int getOpenedFrameNumber() {
		return jdpDesktop.getAllFrames().length;
	}

}
