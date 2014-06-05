package swing;

import javax.swing.JTable;

public class Tabla {

	public static final int SHEAR = 1;
	public static final int TWISTING = 2;
	public static final int TAPPERING = 3;
	public static final int BENDING = 4;
	public static final int PERSONALIZADA = 5;
	
	private static final String[][] MATRIZ_SHEAR = new String[][]{{  "1", "Sxy", "Sxz", "0"},
																  {"Syx",   "1", "Syz", "0"},
																  {"Szx", "Szy",   "1", "0"},
																  {  "0",   "0",   "0", "1"}};
	
	private static final String[][] MATRIZ_TWISTING = new String[][]{{  "1", "Sxy", "Sxz", "0"},
		  {"Syx",   "1", "Syz", "0"},
		  {"Szx", "Szy",   "1", "0"},
		  {  "0",   "0",   "0", "1"}};
	
	public static void cargar(JTable table, int transformacion){
		String[][] matriz = null;
		
		switch (transformacion) {
		case SHEAR:
			matriz = MATRIZ_SHEAR;
			break;
		case TWISTING:
			break;
		case TAPPERING:
			break;
		case BENDING:
		default:
			break;
		}
		
		for(int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				table.setValueAt(matriz[i][j], i, j);
			}
		}
	}
	
}
