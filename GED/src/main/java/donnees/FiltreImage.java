package donnees;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltreImage extends FileFilter{
	String[] suffixes;
	String desc;

	public FiltreImage(String[] suffixes, String desc) {
		this.suffixes = suffixes;
		this.desc = desc;
	}

	boolean appartient(String suffixe) {
		for (int i = 0; i < suffixes.length; ++i)
			if (suffixe.equals(suffixes[i]))
				return true;
		return false;
	}

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		String suffixe = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1)
			suffixe = s.substring(i + 1).toLowerCase();
		return suffixe != null && appartient(suffixe);
	}

	// la description du filtre
	@Override
	public String getDescription() {
		return desc;
	}
}
