package matriz;

public class MatrizTransformacion {
	
	private double[][] matriz = new double[4][4];
	
	public MatrizTransformacion(){}
	
	public MatrizTransformacion(double[][] matriz){
		this.matriz = matriz;
	}
	
	public double[][] getMatriz(){
		return matriz;
	}

	public MatrizTransformacion producto(MatrizTransformacion t) {
		MatrizTransformacion tNueva = new MatrizTransformacion();
		
		for ( int k = 0; k < 4; k++){
			for ( int j = 0; j < 4; j++){
				for ( int i = 0; i < 4; i++ ){
					tNueva.matriz[k][j] += this.matriz[k][i] * t.matriz[i][j];
				}
			}
		}
		
		return tNueva;
	}
	
	public void imprimir(){
		for(int i = 0; i < 4; i++){
			System.out.println(matriz[i][0] + " " + matriz[i][1] + " " + matriz[i][2] + " " + matriz[i][3]);
		}
	}
	
}
