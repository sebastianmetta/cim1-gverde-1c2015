package ar.uba.fi.cim.files;

import java.io.File;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import ar.uba.fi.cim.analysis.AnalysisResult;
import ar.uba.fi.cim.analysis.ImageAnalyzer;
import ar.uba.fi.cim.util.CommonProperties;
import ar.uba.fi.cim.util.ImageUtils;

public final class ImageWatchQueueReader implements Runnable {

	private WatchService watcher;

	private ImageAnalyzer imageAnalyzer;

	private CommonProperties commonProperties;

	private ImageLocator imageLocator;

	private Logger logger = Logger.getLogger(ImageWatchQueueReader.class);

	private AtomicBoolean run = new AtomicBoolean();

	public ImageWatchQueueReader(WatchService watcher, ImageAnalyzer imageAnalyzer, CommonProperties commonProperties,
			ImageLocator imageLocator) {
		this.watcher = watcher;
		this.imageAnalyzer = imageAnalyzer;
		this.commonProperties = commonProperties;
		this.imageLocator = imageLocator;
	}

	@Override
	public void run() {
		try {
			WatchKey key = watcher.take();
			this.run.compareAndSet(false, true);
			logger.info("El proceso de inspeción de directorios ha sido iniciado.");
			
			while (key != null && this.run.get()) {
				for (WatchEvent<?> event : key.pollEvents()) {
					logger.info("Se detectó un nuevo archivo: " + event.context());
					File fileToAnalyze = new File(this.commonProperties.getFilesDirectoryInput(), event.context().toString());
					if (ImageUtils.fileIsImage(fileToAnalyze)) {
						AnalysisResult result = imageAnalyzer.analyzeImage(fileToAnalyze);
						imageLocator.locateImage(fileToAnalyze, result);
					} else {
						logger.info("Ignorando el archivo ya que el mismo no es una imagen.");
					}
				}
				key.reset();
				key = watcher.take();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ImageLocatorException e) {
			logger.error(e.getMessage());
		}
	}

	public synchronized void stopReader() {
		this.run.compareAndSet(true, false);
		logger.info("El proceso de inspeción de directorios ha sido detenido.");
	}

}
