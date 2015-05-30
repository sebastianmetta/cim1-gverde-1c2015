package ar.uba.fi.cim.analysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.neuroph.core.NeuralNetwork;

import ar.uba.fi.cim.filter.Histograma;
import ar.uba.fi.cim.util.CommonProperties;

public class ImageAnalyzerImpl implements ImageAnalyzer {

	private static int NUM_RANGOS = 10;

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
			// BinarizationFilter binFilter = new BinarizationFilter(image);
			// binFilter.setThreshold(50);
			// binFilter.doBinirization();
			// Histograma histograma = new Histograma(binFilter.getImg());

			Histograma histograma = new Histograma(image);

			int[][] arrayHistograma = histograma.getHistograma();

			// Armo contadores de rango de cantidad de pixeles
			double[] rangos = new double[NUM_RANGOS];
			double intervalo = (double) 256 / NUM_RANGOS;
			double cotaRango = intervalo;
			int cantPixeles = 0, rango = 0;

			for (int j = 0; j < 256; j++) {
				if (j > cotaRango) {
					cotaRango += intervalo;
					rango++;
				}
				cantPixeles += arrayHistograma[0][j];
				rangos[rango] += arrayHistograma[0][j];
			}

			// Normalizacion de contadores
			for (int i = 0; i < NUM_RANGOS; i++) {
				rangos[i] /= cantPixeles;
			}

			// Genero el input para la red neuronal
			neuralNetwork.setInput(rangos[0], rangos[1], rangos[2], rangos[3],
					rangos[4], rangos[5], rangos[6], rangos[7], rangos[8],
					rangos[9]);

			// Calculo el resultado
			neuralNetwork.calculate();

			// Clasifico la imagen
			double[] networkOutput = neuralNetwork.getOutput();

			/** Para debug */
			System.out.println("OUTPUT_ANN: " + networkOutput[0]);
			/** FIN para debug */

			if (networkOutput[0] < 0.5) {
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
