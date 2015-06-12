package ar.uba.fi.cim.analysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;

import ar.uba.fi.cim.filter.BinarizationFilter;
import ar.uba.fi.cim.filter.Histograma;
import ar.uba.fi.cim.util.CommonProperties;

public class ImageAnalyzerImpl implements ImageAnalyzer {

	private Logger logger = Logger.getLogger(ImageAnalyzerImpl.class);

	@SuppressWarnings("rawtypes")
	private NeuralNetwork neuralNetwork;

	public ImageAnalyzerImpl(CommonProperties commonProperties) {
		/**
		 * SEBA: Esto tira excepcion NeurophException. La puedo atrapar aca,
		 * pero como aviso al runner que no tengo red neuronal? La deberia
		 * capturar el runner
		 */
		neuralNetwork = NeuralNetwork.createFromFile(commonProperties.getFilesFilenameAnn());
	}

	@Override
	public AnalysisResult analyzeImage(File file) {
		logger.info("Analizando imagen: " + file.getAbsolutePath());
		try {
			BufferedImage image = ImageIO.read(file);
			BinarizationFilter binFilter = new BinarizationFilter(image);
			binFilter.setThreshold(50); // TODO aca Omar dijo que se seteaba
										// inteligentemente
			binFilter.doBinirization();
			Histograma histograma = new Histograma(binFilter.getImg());

			int[][] arrayHistograma = histograma.getHistograma();

			double cantPixelesBlancoBinarizacion = arrayHistograma[0][255];
			double cantPixelesNegroBinarizacion = arrayHistograma[0][0];
			int cantPixelesBinarizacion = (int) (cantPixelesBlancoBinarizacion + cantPixelesNegroBinarizacion);
			
			/** Para debug */
			System.out.println("PIXELES_BLANCO_BIN: " + cantPixelesBlancoBinarizacion);
			System.out.println("PIXELES_NEGRO_BIN: " + cantPixelesNegroBinarizacion);
			/** FIN para debug */

			// Normalizacion de contadores
			cantPixelesBlancoBinarizacion /= cantPixelesBinarizacion;
			cantPixelesNegroBinarizacion /= cantPixelesBinarizacion;

			// Genero el input para la red neuronal
			neuralNetwork.setInput(cantPixelesBlancoBinarizacion, cantPixelesNegroBinarizacion);

			// Calculo el resultado
			neuralNetwork.calculate();

			// Clasifico la imagen
			double[] networkOutput = neuralNetwork.getOutput();

			/** Para debug */
			System.out.println("OUTPUT_ANN: " + networkOutput[0]);
			/** FIN para debug */

			if (networkOutput[0] < 0.5) {
				logger.info("Imagen analizada. El resultado es: " + AnalysisResult.TAM_CHICO);
				return AnalysisResult.TAM_CHICO;
			} else {
				logger.info("Imagen analizada. El resultado es: " + AnalysisResult.TAM_GRANDE);
				return AnalysisResult.TAM_GRANDE;
			}
		} catch (IOException e) {
			logger.info("Error al leer la imagen desde el archivo " + file.getAbsolutePath());
			e.printStackTrace();
		}

		return AnalysisResult.INDETERMINADO;
	}
}
