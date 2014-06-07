package matriz;

public class MatrizIdentidad extends MatrizTransformacion {

	public MatrizIdentidad(){
		super(new double[][]{{1, 0, 0, 0},
								{0, 1, 0, 0},
								{0, 0, 1, 0},
								{0, 0, 0, 1}
							});
	}
	
}
