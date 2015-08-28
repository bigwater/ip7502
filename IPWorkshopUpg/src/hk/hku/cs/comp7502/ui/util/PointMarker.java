package hk.hku.cs.comp7502.ui.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class PointMarker {
	public static void markPoint(Graphics2D g2d, List<double[]> pointList, int baseX, int baseY) {
		if (pointList == null || pointList.isEmpty()) {
			return;
		}
		
		int maxX =  (int)  Math.round(pointList.stream().map(a -> a[0]).reduce(Double::max).get());
		int maxY = (int) Math.round( pointList.stream().map(a -> a[1]).reduce(Double::max).get() );
		
		BufferedImage img = new BufferedImage(maxY+1, maxX+1, BufferedImage.TYPE_4BYTE_ABGR);
		
		pointList.stream().forEach(a -> {
			img.setRGB((int)(Math.round(a[0])), (int)(Math.round(a[1])), Color.GREEN.getRGB());
		});
		
		g2d.drawImage(img, baseX, baseY, null);
		
	}
}
