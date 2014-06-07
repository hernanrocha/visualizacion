package graphic;

import java.text.NumberFormat;

public class Punto3D {
	
	private double x, y, z;
	
	// Resta de dos puntos
	public static Punto3D resta(Punto3D p1, Punto3D p2){
		return new Punto3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
	}
	
	// Producto Vectorial de dos puntos
	public static Punto3D productoVectorial(Punto3D p1, Punto3D p2){
		double Ux = p1.getX(), Uy = p1.getY(), Uz = p1.getZ();
		double Vx = p2.getX(), Vy = p2.getY(), Vz = p2.getZ();
		
		return new Punto3D(Uy * Vz - Uz * Vy, Uz * Vx - Ux * Vz, Ux * Vy - Uy * Vx);
		
	}	

	// Constructor
	public Punto3D(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	// Aplicar matriz de transformacion (4x4)
	public Punto3D aplicarTransformacion(double[][] M) {
		double[] res = new double[4];
		double[] p = new double[]{x, y, z, 1};
		
		for(int i = 0; i < 4; i++){
			res[i] = 0;
            for(int j = 0; j < 4; j++){
            	res[i] += M[i][j] * p[j];
            }
        }
		
		Punto3D pNuevo = new Punto3D(res[0], res[1], res[2]);
		
		return pNuevo;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public String toString(){
		return "(" + 
				NumberFormat.getNumberInstance().format(x) + ", " +
				NumberFormat.getNumberInstance().format(y) + ", " + 
				NumberFormat.getNumberInstance().format(z) + ")";
	}

	public double getModulo() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double productoEscalar(Punto3D normal) {
		// TODO Auto-generated method stub
		return x * normal.x + y * normal.y + z * normal.z;
	}

	public Punto3D versor() {
		double modulo = getModulo();
		return new Punto3D(x / modulo, y / modulo, z / modulo);
	}

}
