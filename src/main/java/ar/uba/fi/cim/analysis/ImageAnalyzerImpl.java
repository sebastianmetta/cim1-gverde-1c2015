package ar.uba.fi.cim.analysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;

import ar.uba.fi.cim.filter.BinarizationFilter;
import ar.uba.fi.cim.filter.Histograma;
import ar.uba.fi.cim.filter.SobelFilter;
import ar.uba.fi.cim.util.CommonProperties;

public class ImageAnalyzerImpl implements ImageAnalyzer {

	private Logger logger = Logger.getLogger(ImageAnalyzerImpl.class);

	@SuppressWarnings("rawtypes")
	private NeuralNetwork neuralNetwork;

	public ImageAnalyzerImpl(CommonProperties commonProperties) {
		/** SEBA: Esto tira excepcion NeurophException. La puedo atrapar aca,
		 * pero como aviso al runner que no tengo red neuronal? La deberia
		 * capturar el runner */
		neuralNetwork =
				NeuralNetwork.createFromFile(commonProperties
						.getFilesFilenameAnn());
	}

	@Override
	public AnalysisResult analyzeImage(File file) {
		logger.info("Analizando imagen: " + file.getAbsolutePath());
		try {
			BufferedImage image = ImageIO.read(file);

			Histograma histograma = new Histograma(image);
			// Aplico filtros binarizacion y sobel
			BinarizationFilter binFilter = new BinarizationFilter(image);
			binFilter.setOptimalThreshold(histograma);

			binFilter.doBinirization();

			SobelFilter sobelFilter = new SobelFilter(binFilter.getImg());
			BufferedImage imageSobel = sobelFilter.run();

			// Genero histograma con binarizacion
			Histograma histogramaBin = new Histograma(binFilter.getImg());
			int[][] arrayHistogramaBin = histogramaBin.getHistograma();

			// Genero histograma con sobel
			Histograma histogramaSobel = new Histograma(imageSobel);
			int[][] arrayHistogramaSobel = histogramaSobel.getHistograma();

			// Obtengo cantidad de pixeles en blanco y en negro con binarizacion
			// y blancos con sobel
			double cantPixelesBlancoBinarizacion = arrayHistogramaBin[0][255];
			double cantPixelesNegroBinarizacion = arrayHistogramaBin[0][0];
			double cantPixelesBlancoSobel = arrayHistogramaSobel[0][255];
			int cantPixeles =
					(int) (cantPixelesBlancoBinarizacion + cantPixelesNegroBinarizacion);

			// Normalizacion de cantidad de pixeles
			cantPixelesBlancoBinarizacion /= cantPixeles;
			cantPixelesBlancoSobel = cantPixelesBlancoSobel * 100 / cantPixeles;

			// Genero el input para la red neuronal
			neuralNetwork.setInput(cantPixelesBlancoBinarizacion,
					cantPixelesBlancoSobel);

			// Calculo el resultado
			neuralNetwork.calculate();

			// Clasifico la imagen
			double[] networkOutput = neuralNetwork.getOutput();

			if (networkOutput[0] < 0.7) {
				logger.info("Imagen analizada. El resultado es: "
						+ AnalysisResult.TAM_CHICO);
				return AnalysisResult.TAM_CHICO;
			}
			else {
				logger.info("Imagen analizada. El resultado es: "
						+ AnalysisResult.TAM_GRANDE);
				return AnalysisResult.TAM_GRANDE;
			}
		}
		catch (IOException e) {
			logger.info("Error al leer la imagen desde el archivo "
					+ file.getAbsolutePath());
			e.printStackTrace();
		}

		return AnalysisResult.INDETERMINADO;
	}
}
