package matriz;

public class MatrizRotacionZ extends MatrizTransformacion {

	public MatrizRotacionZ(double o) {
		super(new double[][]{{Math.cos(o), - Math.sin(o), 0, 0},
				 			 {Math.sin(o),   Math.cos(o), 0, 0},
				 			 {     0     ,       0      , 1, 0},
				 			 {     0     ,       0      , 0, 1}});
	}

}
