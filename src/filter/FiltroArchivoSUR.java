package filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltroArchivoSUR extends FileFilter {

	@Override
	public boolean accept(File f) {
		return f.getName().endsWith("sur") || f.isDirectory();
	}

	@Override
	public String getDescription() {
		return "Archivo SUR (*.sur)";
	}

}
