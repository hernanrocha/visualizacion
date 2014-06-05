package graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import swing.Imagen3D;

public class Incidence {
	
	private int points[];

	public Incidence(int[] points) {
		super();
		this.points = points;
	}

	public void dibujar(Imagen3D imagen, Graphics g, HashMap<Integer, Punto3D> puntos) {

		int nPoints = points.length;
		int xPoints[] = new int[nPoints];
		int yPoints[] = new int[nPoints];
		
		// Calcular vertices
		for(int i = 0; i < points.length; i++){
			Punto3D punto = puntos.get(points[i]);
			xPoints[i] = imagen.getXtoC(punto.getX());
			yPoints[i] = imagen.getYtoF(punto.getY());
		}

		int vista = imagen.getVista();
		if (vista == Objeto3D.VISTA_TRIANGULOS){
			// Dibujar triangulacion
			g.setColor(Color.RED);
			g.drawPolygon(xPoints, yPoints, nPoints);			
		}else if (vista == Objeto3D.VISTA_SOLIDO){
			// Calcular vector normal
			Punto3D normal = Punto3D.productoVectorial(
					Punto3D.resta(puntos.get(points[0]), puntos.get(points[1])), 
					Punto3D.resta(puntos.get(points[1]), puntos.get(points[2])));
			
			// Dibujar solido iluminado
			Punto3D luzVector = new Punto3D(0, 1, 0);
			
			Color luz = new Color(250, 200, 100);
			Color ambiente = new Color(100, 80, 40);
			Color objeto = Color.RED;
			
			double intensidad = luzVector.productoEscalar(normal.versor());
			
			if(intensidad < -1 || intensidad > 1){
				System.out.println("Error");
			}
			
			if(normal.getZ() > 0 || ! imagen.isBackfaceCulling()){
				int red = (int) Math.max(0, intensidad * (255 - ambiente.getRed()) * 255 / objeto.getRed()) + ambiente.getRed();
				int green = (int) Math.max(0, intensidad * (255 - ambiente.getGreen())) + ambiente.getGreen();
				int blue = (int) Math.max(0, intensidad * (255 - ambiente.getBlue())) + ambiente.getBlue();
				Color c = new Color(red, green, blue);
				
				g.setColor(c);
				g.fillPolygon(xPoints, yPoints, nPoints);
			}
		}else if (vista == Objeto3D.VISTA_PUNTOS){
			g.setColor(Color.RED);
			for(int i = 0; i < points.length; i++){
				g.drawLine(xPoints[i], yPoints[i], xPoints[i], yPoints[i]);
			}
		}
		
	}
	
	public Double getZPromedio(HashMap<Integer, Punto3D> puntos){
		int cant = points.length;
		
		double zAcum = 0;
		
		for(int i = 0; i < cant; i++){
			Punto3D punto = puntos.get(points[i]);
			zAcum += punto.getZ();
		}
		
		return zAcum / cant;
	}
	
	public int[] getPoints(){
		return points;
	}

}
