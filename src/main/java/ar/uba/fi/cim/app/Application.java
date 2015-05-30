package ar.uba.fi.cim.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

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

			// Generacion csv
			for (double valor : rangos) {
				fileWriter.append(String.format(Locale.US, "%.10f", valor));
				fileWriter.append(COMMA_DELIMITER);
			}

			if (esChico) {
				fileWriter.append(String.valueOf(0));
			}
			else {
				fileWriter.append(String.valueOf(1));
			}

			fileWriter.append(NEW_LINE_SEPARATOR);
		}
		catch (IOException e) {
			System.out.println("Image " + file.getName() + " failed");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		final File folderChico = new File(args[0]);
		final File folderGrande = new File(args[1]);
		try {
			fileWriter = new FileWriter(args[2]);
		}
		catch (IOException e) {
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
		}
		catch (IOException e) {
			System.out.println("Error while flushing/closing fileWriter !!!");
			e.printStackTrace();
		}
		System.out.println("TERMINO");
	}
}
