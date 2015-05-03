package ar.uba.fi.cim.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

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

}
