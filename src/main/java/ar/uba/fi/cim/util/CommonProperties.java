package ar.uba.fi.cim.util;

/**
 * Propiedades comunes al sistema.
 * 
 * @author Sebastian
 *
 */
public class CommonProperties {

	private String filesDirectoryInput;
	private String filesDirectoryOutputSano;
	private String filesDirectoryOutputRoto;
	private String filesDirectoryOutputTamanioGrande;
	private String filesDirectoryOutputTamanioChico;

	public String getFilesDirectoryInput() {
		return filesDirectoryInput;
	}

	public void setFilesDirectoryInput(String filesDirectoryInput) {
		this.filesDirectoryInput = filesDirectoryInput;
	}

	public String getFilesDirectoryOutputSano() {
		return filesDirectoryOutputSano;
	}

	public void setFilesDirectoryOutputSano(String filesDirectoryOutputSano) {
		this.filesDirectoryOutputSano = filesDirectoryOutputSano;
	}

	public String getFilesDirectoryOutputRoto() {
		return filesDirectoryOutputRoto;
	}

	public void setFilesDirectoryOutputRoto(String filesDirectoryOutputRoto) {
		this.filesDirectoryOutputRoto = filesDirectoryOutputRoto;
	}

	public String getFilesDirectoryOutputTamanioGrande() {
		return filesDirectoryOutputTamanioGrande;
	}

	public void setFilesDirectoryOutputTamanioGrande(
			String filesDirectoryOutputTamanioGrande) {
		this.filesDirectoryOutputTamanioGrande = filesDirectoryOutputTamanioGrande;
	}

	public String getFilesDirectoryOutputTamanioChico() {
		return filesDirectoryOutputTamanioChico;
	}

	public void setFilesDirectoryOutputTamanioChico(
			String filesDirectoryOutputTamanioChico) {
		this.filesDirectoryOutputTamanioChico = filesDirectoryOutputTamanioChico;
	}

}
