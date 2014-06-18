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
			xPoints[i] = imagen.mapToScreenX(punto.getX());
			yPoints[i] = imagen.mapToScreenY(punto.getY());
		}

		int vista = imagen.getVista();
		if (vista == Objeto3D.VISTA_PUNTOS){
			g.setColor(Color.RED);
			for(int i = 0; i < points.length; i++){
				g.drawLine(xPoints[i], yPoints[i], xPoints[i], yPoints[i]);
			}
		}else if (vista == Objeto3D.VISTA_TRIANGULOS){
			// Dibujar triangulacion
			g.setColor(Color.RED);
			g.drawPolygon(xPoints, yPoints, nPoints);
		}else if (vista == Objeto3D.VISTA_SOLIDO){			
			// Calcular vector normal
			Punto3D versorNormal = Punto3D.productoVectorial(
					Punto3D.resta(puntos.get(points[0]), puntos.get(points[1])), 
					Punto3D.resta(puntos.get(points[1]), puntos.get(points[2]))).versor();
			
			// Dibujar solido iluminado
			Punto3D luzPunto = imagen.getLuzPunto();
			Punto3D objetoPunto = getPuntoPromedio(puntos);
			
			Punto3D versorLuz = Punto3D.resta(objetoPunto, luzPunto).versor();
			
			Color ambiente = imagen.getAmbientColor();
			Color objeto = imagen.getObjectColor();
			
			double intensidad = versorLuz.productoEscalar(versorNormal);
			
			if(versorNormal.getZ() > 0 || !imagen.isBackfaceCulling()){
				int red = (int) (0.8 * Math.max(0, intensidad * objeto.getRed()) + 0.2 * ambiente.getRed());
				int green = (int) (0.8 * Math.max(0, intensidad * objeto.getGreen()) + 0.2 * ambiente.getGreen());
				int blue = (int) (0.8 * Math.max(0, intensidad * objeto.getBlue()) + 0.2 * ambiente.getBlue());
					
				g.setColor(new Color(red, green, blue));
				g.fillPolygon(xPoints, yPoints, nPoints);
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
		double cant = points.length;
		
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
