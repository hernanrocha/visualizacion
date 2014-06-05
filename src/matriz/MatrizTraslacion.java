package matriz;

public class MatrizTraslacion extends MatrizTransformacion {

	public MatrizTraslacion(double tx, double ty, double tz) {
		super(new double[][]{{1, 0, 0, tx},
				  {0, 1, 0, ty},
				  {0, 0, 1, tz},
				  {0, 0, 0, 1 }});
	}	
	
}
