package ar.uba.fi.cim.analysis;

import java.io.File;

import org.apache.log4j.Logger;

public class ImageAnalyzerImpl implements ImageAnalyzer {

	private Logger logger = Logger.getLogger(ImageAnalyzerImpl.class);

	@Override
	public AnalysisResult analyzeImage(File file) {
		logger.info("Analizando imagen: " + file.getAbsolutePath());
		logger.info("Imagen analizada. El resultado es: " + AnalysisResult.SANO);
		return AnalysisResult.SANO;
	}

}
