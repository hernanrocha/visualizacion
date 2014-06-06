package graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import swing.Imagen3D;

public class Incidence {
	
	private int points[];
	private Punto3D luzPunto;

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
			luzPunto = imagen.getLuzPunto();
			Punto3D objetoPunto = getPuntoPromedio(puntos);
			
			Punto3D versorLuz = Punto3D.resta(objetoPunto, luzPunto).versor();
			
			Color ambiente = new Color(100, 0, 0);
			Color objeto = Color.RED;
			
			double intensidad = versorLuz.productoEscalar(normal.versor());
			
			if(intensidad < -1 || intensidad > 1){
//				System.out.println("Error");
			}
			
			if(normal.getZ() > 0 || ! imagen.isBackfaceCulling()){
				int red = (int) Math.max(0, intensidad * (255 - ambiente.getRed()) * 255 / objeto.getRed()) + ambiente.getRed();
				Color c = new Color(red, 0, 0);
				
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
	
	public Punto3D getPuntoPromedio(HashMap<Integer, Punto3D> puntos){
		int cant = points.length;
		
		double xAcum = 0;
		double yAcum = 0;
		double zAcum = 0;
		
		
		for(int i = 0; i < cant; i++){
			Punto3D punto = puntos.get(points[i]);
			xAcum += punto.getX();
			yAcum += punto.getY();
			zAcum += punto.getZ();
		}
		
		return new Punto3D(xAcum / cant, yAcum / cant, zAcum / cant);
	}
	
	public int[] getPoints(){
		return points;
	}

}
