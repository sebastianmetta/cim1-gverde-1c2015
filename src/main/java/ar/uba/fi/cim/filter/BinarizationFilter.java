package ar.uba.fi.cim.filter;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 *
 * @author sami
 */
public class BinarizationFilter  extends Filter{

	public static int THRESHABOVE = 255;
	public static int THRESHBELOW = 0;
	private BufferedImage bufi;
	private Raster data;

	private   int width, height;
	private int threshold;

	private BufferedImage bufnew;
	/** Creates a new instance of Binarization */
	public BinarizationFilter(BufferedImage newimg) {
		bufi=this.copyImage(newimg);
	}

	public static int getColor(int b, int g, int r) {
		return (int)Math.pow(2,16)*r + 256*g + b;
	}
	public void setThreshold(int threshold){
		this.threshold=threshold;
	}

	public int getWidth(){
		return data.getWidth();
	}

	public int getHight(){
		return data.getHeight();
	}

	public void doBinirization(){


		width=bufi.getWidth();
		height=bufi.getHeight();
		BufferedImage bi = new BufferedImage(width,  height,bufi.getType());
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				int rgb =bufi.getRGB(i,j);

				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb >> 0) & 0xff;

				// Calculate the brightness
				if(r>threshold||g>threshold||b>threshold)
				{
					r=255;
					g=255;
					b=255;
				}
				else
				{
					r=0;
					g=0;
					b=0;
				}


				// Return the result
				rgb= (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);

				bi.setRGB(i, j,rgb);
			}

			bufnew=bi;

		}
	}

	public BufferedImage getImg(){

		return bufnew;
	}
}
