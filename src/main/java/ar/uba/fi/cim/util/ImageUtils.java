package ar.uba.fi.cim.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImageUtils {

	private static Logger logger = Logger.getLogger(ImageUtils.class);
	/**
	 * Devuelve <code>true</code> si el archivo es una imagen o <code>false</code> en caso contrario.
	 * 
	 * @param file El archivo a analizar.
	 * @return
	 */
	public static boolean fileIsImage(File file) {
		try {
			Image image = ImageIO.read(file);
			if (image == null) {
				return false;
			}
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
	
	public static void saveImage2File(BufferedImage image, String fileName) throws IOException {
		logger.info("Save Image: " + fileName);
		File file = new File(fileName);
		try {
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			logger.error("No se pudo guardar la imagen: " + e.getMessage());
			throw e;
		}
	}

}
