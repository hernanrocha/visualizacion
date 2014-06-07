package matriz;

public class MatrizRotacionX extends MatrizTransformacion {

	public MatrizRotacionX(double o) {
		super(new double[][]{{     1     ,      0     ,       0      , 0},
				 		     {     0     , Math.cos(o), - Math.sin(o), 0},
				 		     {     0     , Math.sin(o),   Math.cos(o), 0},
				 		     {     0     ,      0     ,       0      , 1}});
	}

}
