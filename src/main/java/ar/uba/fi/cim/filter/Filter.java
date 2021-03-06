package ar.uba.fi.cim.filter;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public abstract class Filter {
	
	static final int MAX_POSITIONS_HISTOGRAM = 255;
	
	public BufferedImage copyImage(BufferedImage bufferedImage){
		
    	BufferedImage copyOfImage = 
 			   new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
		Graphics g = copyOfImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, null);
 		return copyOfImage;
	}

}
