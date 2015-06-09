package ar.uba.fi.cim.filter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ar.uba.fi.cim.util.ImageUtils;

public class SobelFilter  extends Filter {
    String title = null; 
    int sobel_x[][] = {{-1,0,1},
                       {-2,0,2},
                       {-1,0,1}};
    int sobel_y[][] = {{-1,-2,-1},
                       {0,0,0},
                       {1,2,1}};

    int pixel_x;
    int pixel_y;
    
    Integer maximo = null;
    BufferedImage ip= null;


    public SobelFilter(BufferedImage bufferedImage) {
		this.ip = this.copyImage(bufferedImage);
	}

    public BufferedImage run() { 
    	
        try {
			ImageUtils.saveImage2File(ip, "tmp.jpg");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedImage img = null;
		File fileBin = new File("tmp.jpg");
		try {
			img = ImageIO.read(fileBin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ip = img;
		
        int w = ip.getWidth();
        int h = ip.getHeight();
        BufferedImage copy = copyImage(ip); 
        for (int x=1; x < w-2; x++) {
            for (int y=1; y < h-2; y++) {
                pixel_x = (sobel_x[0][0] * copy.getRGB(x-1,y-1)) + (sobel_x[0][1] * copy.getRGB(x,y-1)) + (sobel_x[0][2] * copy.getRGB(x+1,y-1)) +
                    (sobel_x[1][0] * copy.getRGB(x-1,y))   + (sobel_x[1][1] * copy.getRGB(x,y))   + (sobel_x[1][2] * copy.getRGB(x+1,y)) +
                    (sobel_x[2][0] * copy.getRGB(x-1,y+1)) + (sobel_x[2][1] * copy.getRGB(x,y+1)) + (sobel_x[2][2] * copy.getRGB(x+1,y+1));
                pixel_y = (sobel_y[0][0] * copy.getRGB(x-1,y-1)) + (sobel_y[0][1] * copy.getRGB(x,y-1)) + (sobel_y[0][2] * copy.getRGB(x+1,y-1)) +
                    (sobel_y[1][0] * copy.getRGB(x-1,y))   + (sobel_y[1][1] * copy.getRGB(x,y))   + (sobel_y[1][2] * copy.getRGB(x+1,y)) +
                    (sobel_y[2][0] * copy.getRGB(x-1,y+1)) + (sobel_y[2][1] * copy.getRGB(x,y+1)) + (sobel_x[2][2] * copy.getRGB(x+1,y+1));

                int val = (int)Math.sqrt((pixel_x * pixel_x) + (pixel_y * pixel_y));

                if(val < 0)
                {
                   val = 0;
                }

                if(val > 255)
                {
                   val = 16777215;
                }
                
                ip.setRGB(x,y,val);
            }
        }
        

		
        return ip;
    }

}