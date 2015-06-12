package ar.uba.fi.cim.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import ar.uba.fi.cim.filter.BinarizationFilter;
import ar.uba.fi.cim.filter.Histograma;
import ar.uba.fi.cim.filter.SobelFilter;

public class Application {

	private static FileWriter fileWriter = null;

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	private static void procesarImagen(File file, boolean esChico) {
		try {
			BufferedImage image = ImageIO.read(file);

			// Aplico filtros binarizacion y sobel
			BinarizationFilter binFilter = new BinarizationFilter(image);
			binFilter.setThreshold(50); // TODO aca Omar dijo que se seteaba
										// inteligentemente
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
			int cantPixeles = (int) (cantPixelesBlancoBinarizacion + cantPixelesNegroBinarizacion);

			// Normalizacion de cantidad de pixeles
			cantPixelesBlancoBinarizacion /= cantPixeles;
			cantPixelesNegroBinarizacion /= cantPixeles;
			cantPixelesBlancoSobel /= cantPixeles;

			// Generacion csv
			fileWriter.append(String.format(Locale.US, "%.10f", cantPixelesBlancoBinarizacion));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.format(Locale.US, "%.10f", cantPixelesNegroBinarizacion));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.format(Locale.US, "%.10f", cantPixelesBlancoSobel));
			fileWriter.append(COMMA_DELIMITER);

			if (esChico) {
				fileWriter.append(String.valueOf(0));
			} else {
				fileWriter.append(String.valueOf(1));
			}

			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch (IOException e) {
			System.out.println("Image " + file.getName() + " failed");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		final File folderChico = new File(args[0]);
		final File folderGrande = new File(args[1]);
		try {
			fileWriter = new FileWriter(args[2]);
		} catch (IOException e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
			return;
		}

		for (final File file : folderChico.listFiles()) {
			procesarImagen(file, true);
		}

		for (final File file : folderGrande.listFiles()) {
			procesarImagen(file, false);
		}

		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
		System.out.println("TERMINO");
	}
}
