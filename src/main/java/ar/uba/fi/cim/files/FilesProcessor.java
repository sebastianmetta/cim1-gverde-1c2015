package ar.uba.fi.cim.files;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

import org.apache.log4j.Logger;

import ar.uba.fi.cim.analysis.ImageAnalyzer;
import ar.uba.fi.cim.util.CommonProperties;

/**
 * Responsable de leer las imágenes del directorio correspondiente y utilizar al analizador para categorizarlas.
 * 
 * @author Sebastian
 *
 */
public class FilesProcessor {

	private ImageAnalyzer imageAnalyzer;

	private CommonProperties commonProperties;

	private ImageLocator imageLocator;

	private Logger logger = Logger.getLogger(FilesProcessor.class);

	public FilesProcessor(ImageAnalyzer imageAnalyzer, CommonProperties commonProperties, ImageLocator imageLocator) {
		this.imageAnalyzer = imageAnalyzer;
		this.commonProperties = commonProperties;
		this.imageLocator = imageLocator;
	}

	public ImageAnalyzer getImageAnalyzer() {
		return this.imageAnalyzer;
	}

	public CommonProperties getCommonProperties() {
		return this.commonProperties;
	}

	/**
	 * Comienza el hilo que se encarga de detectar nuevas imágenes en el directorio configurado.
	 */
	public void startProcessing() {
		ImageWatchQueueReader fileWatcher = null;
		try {
			Path toWatch = Paths.get(this.commonProperties.getFilesDirectoryInput());
			if (toWatch == null) {
				throw new UnsupportedOperationException(
						"El directorio no existe o no puede ser accedido por la aplicación.");
			}

			WatchService imageWatcher = toWatch.getFileSystem().newWatchService();
			fileWatcher = new ImageWatchQueueReader(imageWatcher, this.imageAnalyzer, this.commonProperties,
					this.imageLocator);
			Thread th = new Thread(fileWatcher, "ImagesWatcher");
			th.start();
			logger.info("Sistema iniciado, esperando imagenes en el directorio: " + this.commonProperties.getFilesDirectoryInput());
			toWatch.register(imageWatcher, StandardWatchEventKinds.ENTRY_CREATE);
			th.join();
		} catch (NoSuchFileException e) {
			logger.error("El directorio de entrada de imágenes no existe: " + e.getMessage());
		} catch (IOException | InterruptedException e) {
			logger.error("La aplicación detectó un error: " + e.toString());
		} finally {
			if (fileWatcher != null) {
				fileWatcher.stopReader();
			}
		}
	}

}
