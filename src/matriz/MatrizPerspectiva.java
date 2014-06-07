package matriz;

public class MatrizPerspectiva extends MatrizTransformacion {

	public MatrizPerspectiva(double d){
		super(new double[][]{{1/d, 0, 0, 0},
				             {0, 1/d, 0, 0},
				             {0, 0, 1, 0},
				             {0, 0, 0, 0}});
	}
	
}
