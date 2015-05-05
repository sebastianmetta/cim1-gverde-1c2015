package ar.uba.fi.cim.analysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import ar.uba.fi.cim.filter.BinarizationFilter;
import ar.uba.fi.cim.filter.Histograma;
import ar.uba.fi.cim.util.ImageUtils;

public class ImageAnalyzerImpl implements ImageAnalyzer {

	private Logger logger = Logger.getLogger(ImageAnalyzerImpl.class);

	@Override
	public AnalysisResult analyzeImage(File file) {
		logger.info("Analizando imagen: " + file.getAbsolutePath());
		try {
			BufferedImage image = ImageIO.read(file);
			BinarizationFilter binFilter = new BinarizationFilter(image);
			binFilter.setThreshold(50);
			binFilter.doBinirization();
			
			Histograma histograma = new Histograma(binFilter.getImg());
			
			/**
			 * Este codigo es solo para poder analizar los histogramas luego no es necesario generarlos
			 */
			
			histograma.mostrarHistogramaNoCero();
			
			//BufferedImage histogramImage = histograma.crearImagenHistograma();
			
			//ImageUtils.saveImage2File(histogramImage, file.getParent()+"\\..\\HISTOGRAMA\\histo_"+file.getName());
			
			/**
			 * Fin codigo para quitar
			 */
			
			
			
			int[][] arrayHistograma = histograma.getHistograma();
			
			// Apartir de aca hay que segmentar por rangos el array de histograma, seguramente 
			// normalizar esos datos (dejarlos entre 1-0) y luego alimentar a la red nueronal
			
		} catch (IOException e) {
			logger.info("Error al leer la imagen desde el archivo " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		
		
		logger.info("Imagen analizada. El resultado es: " + AnalysisResult.SANO);
		return AnalysisResult.SANO;
	}

}
