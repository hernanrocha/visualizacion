package matriz;

public class MatrizEscala extends MatrizTransformacion {

	public MatrizEscala(double sx, double sy, double sz) {
		super(new double[][]{{sx,  0,  0, 0},
				  			 { 0, sy,  0, 0},
				  			 { 0,  0, sz, 0},
				  			 { 0,  0,  0, 1}});
	}
	
	public MatrizEscala(double s){
		super(new double[][]{{s,  0,  0, 0},
	  			 			 { 0, s,  0, 0},
	  			 			 { 0,  0, s, 0},
	  			 			 { 0,  0,  0, 1}});
	}
	
}
