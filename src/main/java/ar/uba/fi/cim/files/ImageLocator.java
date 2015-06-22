package ar.uba.fi.cim.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.log4j.Logger;

import ar.uba.fi.cim.analysis.AnalysisResult;
import ar.uba.fi.cim.util.CommonProperties;

/**
 * Esta clase tiene como responsabilidad la de mover una imagen del directorio de entrada al directorio correspondiente.
 * 
 * @author Sebastian
 *
 */
public class ImageLocator {

	private CommonProperties commonProperties;

	private Logger logger = Logger.getLogger(ImageLocator.class);

	public ImageLocator(CommonProperties commonProperties) {
		this.commonProperties = commonProperties;
	}

	/**
	 * Mueve la imagen al directorio de destino configurado en base al resultado del análisis.
	 * 
	 * @param imageToLocate
	 *            Imagen a mover.
	 * @param analysisResult
	 *            Resultado del análisis.
	 * @throws ImageLocatorException
	 *             si la imagen no pudo moverse.
	 */
	public void locateImage(File imageToLocate, AnalysisResult analysisResult) throws ImageLocatorException {
		try {
			if (analysisResult.equals(AnalysisResult.TAM_CHICO)) {
				this.moveFile(imageToLocate, this.commonProperties.getFilesDirectoryOutputTamanioChico());
			}
			if (analysisResult.equals(AnalysisResult.TAM_GRANDE)) {
				this.moveFile(imageToLocate, this.commonProperties.getFilesDirectoryOutputTamanioGrande());
			}
		} catch (IOException e) {
			throw new ImageLocatorException("La imagen " + imageToLocate.getName()
					+ " no ha podido ser movida al directorio de destino. El motivo del error es: " + e.getMessage());
		}
	}

	private void moveFile(File toMove, String destinationDirectoy) throws IOException {
		Path source = Paths.get(this.commonProperties.getFilesDirectoryInput(), toMove.getName());
		Path target = Paths.get(destinationDirectoy, toMove.getName());
		Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
		logger.info("El archivo " + toMove.getName() + " se movió al directorio " + destinationDirectoy + ".");
	}

}
