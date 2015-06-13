package ar.uba.fi.cim.test.analysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;

import ar.uba.fi.cim.analysis.AnalysisResult;
import ar.uba.fi.cim.analysis.ImageAnalyzer;
import ar.uba.fi.cim.filter.BinarizationFilter;
import ar.uba.fi.cim.filter.Histograma;
import ar.uba.fi.cim.filter.SobelFilter;
import ar.uba.fi.cim.util.CommonProperties;
import ar.uba.fi.cim.util.ImageUtils;

public class ImagePruebaSobelImpl implements ImageAnalyzer {

	private Logger logger = Logger.getLogger(ImagePruebaSobelImpl.class);

	public ImagePruebaSobelImpl(CommonProperties commonProperties) {

	}

	@Override
	public AnalysisResult analyzeImage(File file) {
		logger.info("Analizando imagen: " + file.getAbsolutePath());
		try {
			BufferedImage image, img;
			
				image = ImageIO.read(file);
			Histograma histograma = new Histograma(image);
			
			BinarizationFilter binFilter = new BinarizationFilter(image);
			binFilter.setOptimalThreshold(histograma);
			binFilter.doBinirization();

			ImageUtils.saveImage2File(binFilter.getImg(),
			file.getParent()+"\\..\\SALIDA\\B_"+file.getName());

			
			SobelFilter sobelFilter = new SobelFilter(binFilter.getImg());
			image = sobelFilter.run();
			
			ImageUtils.saveImage2File(image,
			file.getParent()+"\\..\\SALIDA\\S_"+file.getName());
			
			/** Fin codigo para quitar (OMAR) */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return AnalysisResult.INDETERMINADO;
	}
}
