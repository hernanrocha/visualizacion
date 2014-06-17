package matriz;

public class MatrizPerspectiva extends MatrizTransformacion {

	public MatrizPerspectiva(double d){
//		super(new double[][]{{-1000/(d-1900), 0, 0, 0},
//				             {0, -1000/(d-1900), 0, 0},
//				             {0, 0, 1, 0},
//				             {0, 0, 0, 0}});
		super(new double[][]{{1, 0, 0, 0},
	             {0, 1, 0, 0},
	             {0, 0, 1, 0},
	             {0, 0, 0, 1}});
	}
	
}
