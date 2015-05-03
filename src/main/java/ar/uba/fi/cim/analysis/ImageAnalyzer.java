package ar.uba.fi.cim.analysis;

import java.io.File;

/**
 * Esta es la interfaz entre el sistema que obtiene las imagenes y el analizador.
 * @author Sebastian
 *
 */
public interface ImageAnalyzer {

	/**
	 * Analiza la imagen pasada por parámetro y devuelve el resultado del análisis.
	 * @param file
	 * @return
	 */
	public AnalysisResult analyzeImage(File file);
	
}
