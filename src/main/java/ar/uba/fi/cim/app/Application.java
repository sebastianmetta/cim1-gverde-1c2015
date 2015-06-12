package ar.uba.fi.cim.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import ar.uba.fi.cim.filter.BinarizationFilter;
import ar.uba.fi.cim.filter.Histograma;

public class Application {

	private static final int NUM_RANGOS = 10;
	private static FileWriter fileWriter = null;

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	private static void procesarImagen(File file, boolean esChico) {
		BufferedImage image;
		try {
			image = ImageIO.read(file);
			BinarizationFilter binFilter = new BinarizationFilter(image);
			binFilter.setThreshold(50);
			binFilter.doBinirization();
			Histograma histograma = new Histograma(binFilter.getImg());

			int[][] arrayHistograma = histograma.getHistograma();

			double cantPixBlancoBin = arrayHistograma[0][255];
			double cantPixNegroBin = arrayHistograma[0][0];
			int cantPix = (int) (cantPixBlancoBin + cantPixNegroBin);

			// Normalizacion de contadores
			cantPixBlancoBin /= cantPix;
			cantPixNegroBin /= cantPix;

			// Generacion csv
			fileWriter.append(String.format(Locale.US, "%.10f", cantPixBlancoBin));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.format(Locale.US, "%.10f", cantPixNegroBin));
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
