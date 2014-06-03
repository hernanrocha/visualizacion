package swing;

import graphic.Objeto3D;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Imagen3D extends JPanel {

	private static final long serialVersionUID = 1L;
	
	boolean cargado;
	Objeto3D objeto;
	
	 public Imagen3D() {
	        setBorder(BorderFactory.createLineBorder(Color.black));
	    }

//	    public Dimension getPreferredSize() {
//	        return new Dimension(600,550);
//	    }

	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);       

	        if (cargado){	        	
	        	// Dibujar triangulos
	        	g.setColor(Color.BLUE);
	        	objeto.dibujar(this, g);	        	
	        	
//	        	// Dibujar puntos
//	        	g.setColor(Color.RED);
//	        	for (Punto3D punto : puntos.values()){
//	        		g.drawLine(getXtoC(punto.getX()), getYtoF(punto.getY()), getXtoC(punto.getX()) + 3, getYtoF(punto.getY()) + 3);
//	        	}
		        
	        }
	    }
	    
	    public int getXtoC(double x){
	    	return (int) x;
	    }
	    
	    public int getYtoF(double y){
	    	return (int) (getHeight() - y);
	    }
	    
	    public double getCtoX(int c){
	    	return c;
	    }
	    
	    public double getFtoY(int f){
	    	return getHeight() - f;
	    }

		public void rotar(int f, int c, int deltaF, int deltaC) {
			
			// Mover a centro de coordenadas
			objeto.trasladar(- getCtoX(c), - getFtoY(f), 0);
			
			// Realizar rotacion
			objeto.rotacionX((double) (deltaF * Math.PI / 180));
			objeto.rotacionY((double) (deltaC * Math.PI / 180));
			
			// Restaurar posicion
			objeto.trasladar(getCtoX(c), getFtoY(f), 0);
			
		}
		
		public void trasladar(int deltaF, int deltaC){
			objeto.trasladar(deltaC, -deltaF, 0);
		}
		
		public void zoomIn(int f, int c){
			// Mover a centro de coordenadas
			objeto.trasladar(- getCtoX(c), - getFtoY(f), 0);
			
			// Realizar Zoom
			objeto.escalaIsotropica(2);
			
			// Restaurar posicion
			objeto.trasladar(getCtoX(c), getFtoY(f), 0);
		}
		
		public void zoomOut(int f, int c){
			// Mover a centro de coordenadas
			objeto.trasladar(- getCtoX(c), - getFtoY(f), 0);

			// Realizar Zoom
			objeto.escalaIsotropica(0.5);
			
			// Restaurar posicion
			objeto.trasladar(getCtoX(c), getFtoY(f), 0);
		}
}
