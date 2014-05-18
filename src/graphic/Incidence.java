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

    	// Dibujar triangulacion
		int nPoints = points.length;
		int xPoints[] = new int[nPoints];
		int yPoints[] = new int[nPoints];
		
		for(int i = 0; i < points.length; i++){
			Punto3D punto = puntos.get(points[i]);
			xPoints[i] = imagen.getXtoC(punto.getX());
			yPoints[i] = imagen.getYtoF(punto.getY());
		}
		
//		g.drawPolygon(xPoints, yPoints, nPoints);
		
		
		// Dibujar iluminado
		
//		g.fillPolygon(xPoints, yPoints, nPoints);
		Punto3D p = Punto3D.productoVectorial(
				Punto3D.resta(puntos.get(points[0]), puntos.get(points[1])), 
				Punto3D.resta(puntos.get(points[1]), puntos.get(points[2])));
		
		if(p.getZ() > 0){
//			System.out.println("Visible :" + p.getZ());
			Color c = new Color(0, 0, (int) Math.min((double) p.getZ(), 255));
			g.setColor(c);
			g.fillPolygon(xPoints, yPoints, nPoints);
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

}
