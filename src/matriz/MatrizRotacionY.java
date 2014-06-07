package matriz;

public class MatrizRotacionY extends MatrizTransformacion {

	public MatrizRotacionY(double o) {
		super(new double[][]{{  Math.cos(o),  0   , Math.sin(o)  , 0},
							 {     0       ,  1   ,       0      , 0},											 
							 {- Math.sin(o),  0   ,   Math.cos(o), 0},
							 {       0     ,  0   ,       0      , 1}});
	}

}
