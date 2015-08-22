package hk.hku.cs.comp7502.util;

import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NativeUIUtils {
    public static void enableOSXFullscreen(Window window) {
        try {
            Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
            Class params[] = new Class[]{Window.class, Boolean.TYPE};
            Method method = util.getMethod("setWindowCanFullScreen", params);
            method.invoke(util, window, true);
        } catch (ClassNotFoundException | NoSuchMethodException |
            SecurityException | IllegalAccessException |
            IllegalArgumentException | InvocationTargetException exp) {
            exp.printStackTrace(System.err);
        }
    }
    
    public static void toggleOSXFullscreen(Window window) {
        try {
            Class application = Class.forName("com.apple.eawt.Application");
            Method getApplication = application.getMethod("getApplication");
            Object instance = getApplication.invoke(application);
            Method method = application.getMethod("requestToggleFullScreen", Window.class);
            method.invoke(instance, window);
        } catch (ClassNotFoundException | NoSuchMethodException |
            SecurityException | IllegalAccessException |
            IllegalArgumentException | InvocationTargetException exp) {
            exp.printStackTrace(System.err);
        }
    }

}
