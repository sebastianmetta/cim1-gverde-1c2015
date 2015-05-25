package ar.uba.fi.cim.filter;
import java.awt.Color;
import java.awt.image.BufferedImage;


public class Histograma extends Filter{
	
	static final int KEY_RED = 0;
	static final int KEY_GREEN = 1;
	static final int KEY_BLUE = 2;
	
	static final int RED = 16711680;
	static final int GREEN = 65280;
	static final int BLUE = 255;
	
	BufferedImage bufferImage;

	public Histograma(BufferedImage bufferImage) {
		this.bufferImage = bufferImage;
	}
	
	public BufferedImage crearImagenHistograma(){
		int[][] histograma = this.getHistograma();
		BufferedImage bi = new BufferedImage(256,600,BufferedImage.TYPE_INT_RGB);
        fillHistogram(histograma, bi, KEY_RED,200);
        fillHistogram(histograma, bi, KEY_GREEN,400);
        fillHistogram(histograma, bi, KEY_BLUE,600);
		return bi;
	}
	
	private void fillHistogram(int[][] h, BufferedImage bi, int color,int alto) {
		int maximo = this.getMaximo(h,color);
		int cantPixeles = bi.getHeight()*bi.getWidth();
		int porcentajeMaximo = 20;
		int pixelesPorcentaje = (cantPixeles /100) * porcentajeMaximo;
		maximo = (maximo > pixelesPorcentaje) ? pixelesPorcentaje : maximo;
		int yTransform=0;
		for( int x = 0; x < 256; x++ ){ //Recorremos los pixels de la imagen
			yTransform = (h[0][x]*199)/maximo;
            for( int y = 0; y < 200; y++ ){
            	
            	if(yTransform<y){
            		bi.setRGB(x, alto-1-y, getColorKey(color));
            	}
            	else{
            		bi.setRGB(x, alto-1-y, 0);
            	}
            }
        }
	}
	
	private int getColorKey(int color){
		switch (color){
			case KEY_RED : color = RED;
			break;
			case KEY_GREEN : color = GREEN;
			break;
			case KEY_BLUE : color = BLUE;
			break;
		}
		return color;
	}
	
	private int getMaximo(int[][] h, int color){
		int maximo=0;
		for(int x=0; x<256;x++){
			if (h[color][x]> maximo){
				maximo = h[color][x];
			}
		}
		return maximo;
	}
	
	public void mostrarHistograma(){
		int[][] h = this.getHistograma();
        for( int i = 0; i < 3; i++ ){ //Recorremos los pixels de la imagen
        	System.out.println("-------"+i+"------");
            for( int j = 0; j < 256; j++ ){
                //Obtenemos color del pixel actual
            	System.out.println(j+":"+h[i][j]);
            }
        }
	
		
	}
	
	public void mostrarHistogramaNoCero(){
		int[][] h = this.getHistograma();
        for( int i = 0; i < 3; i++ ){ //Recorremos los pixels de la imagen
        	System.out.println("-------"+i+"------");
            for( int j = 0; j < 256; j++ ){
                //Obtenemos color del pixel actual
            	if (h[i][j] > 0)
            		System.out.println(j+":"+h[i][j]);
            }
        }
	
		
	}
	
	public int[][] getHistograma(){
        Color colorAuxiliar;
        
        int histogramaReturn[][]=new int[3][256]; /*Creamos la variable que contenga el histograma*/
        
        for( int i = 0; i < this.getBufferImage().getWidth(); i++ ){ //Recorremos los pixels de la imagen
            for( int j = 0; j < this.getBufferImage().getHeight(); j++ ){
                //Obtenemos color del pixel actual
                colorAuxiliar=new Color(this.getBufferImage().getRGB(i, j));
                histogramaReturn[0][colorAuxiliar.getRed()]+=1;
                histogramaReturn[1][colorAuxiliar.getGreen()]+=1;
                histogramaReturn[2][colorAuxiliar.getBlue()]+=1;
            }
        }
        return histogramaReturn;
    }

	public BufferedImage getBufferImage() {
		return bufferImage;
	}


	public void setBufferImage(BufferedImage bufferImage) {
		this.bufferImage = bufferImage;
	}
	

}
