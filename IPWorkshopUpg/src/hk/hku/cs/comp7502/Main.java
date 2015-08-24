package hk.hku.cs.comp7502;

import hk.hku.cs.comp7502.config.ConfigReader;
import hk.hku.cs.comp7502.config.WorkshopConfig;
import hk.hku.cs.comp7502.ui.MainFrame;

import java.awt.Window;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	public static void requestOSXFullscreen(Window window) {
		try {
			Class appClass = Class.forName("com.apple.eawt.Application");
			Class params[] = new Class[] {};

			Method getApplication = appClass.getMethod("getApplication", params);
			Object application = getApplication.invoke(appClass);
			Method requestToggleFulLScreen = application.getClass().getMethod("requestToggleFullScreen", Window.class);

			requestToggleFulLScreen.invoke(application, window);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace();
		}
	}

	private void enableOSXFullscreen(Window window) {
		try {
			Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
			Class params[] = new Class[] { Window.class, Boolean.TYPE };
			Method method = util.getMethod("setWindowCanFullScreen", params);
			method.invoke(util, window, true);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException exp) {
			exp.printStackTrace(System.err);
		}
	}

	public static void main(String[] args) {
		try {
			WorkshopConfig[] wConfig = ConfigReader.readWorkshopConfig();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
				    "Cannot find workshop config",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		try {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: " + e.getMessage());
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: " + e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame();
				// new JInternalFrameDemo();
			}
		});

	}
}
