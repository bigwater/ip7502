package hk.hku.cs.comp7502.ui;

import hk.hku.cs.comp7502.command.OpenFileWorker;
import hk.hku.cs.comp7502.util.NativeUIUtils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {
	
	private int openedImageNumber = 0;
	
    private JDesktopPane jdpDesktop;
    
	private static final long serialVersionUID = 3204111789442234205L;
	List<ImageInternalFrame> imageDocumentList = new ArrayList<> ();
	
	public MainFrame() {
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
	

    
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem openImageMenuItem = new JMenuItem("Open Image from file...");
        JMenuItem openURLMenuItem = new JMenuItem("Open Image from URL...");
        
        openImageMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	createFrame();
            }
        });
        
        
        openURLMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImageInternalFrame imgFrame = createFrame();
				
				OpenFileWorker worker = new OpenFileWorker("http://pic2.zhimg.com/1dbe926e1_l.jpg", imgFrame);
				worker.execute();
				
				/*worker.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
			             if ("progress".equals(evt.getPropertyName())) {
			            	 System.out.println("fucking");
			            	 //imgFrame.jb.setText("gegege");
			            	 //((Graphics2D) imgFrame.getGraphics()).drawString("loading", 10, 10);
			             }
					}
				});*/
				
				
				//worker.cancel(true);
			}
		});
        
        fileMenu.add(openImageMenuItem);
        fileMenu.add(openURLMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    protected ImageInternalFrame createFrame() {
    	openedImageNumber++;
        ImageInternalFrame fr = new ImageInternalFrame("gege"+openedImageNumber, openedImageNumber);
        jdpDesktop.add(fr);
        
        try {
            fr.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        	e.printStackTrace();
        }
        
        return fr;
    }
    
}
