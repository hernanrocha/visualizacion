package swing;

import graphic.Objeto3D;
import graphic.Punto3D;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Imagen3D extends JPanel {

	private static final long serialVersionUID = 1L;

	boolean cargado;
	Objeto3D objeto;

	private int vista;
	private double distancia;
	private boolean backfaceCulling;
	private boolean perspectiva;

	private Punto3D [] luces = new Punto3D[]{new Punto3D(-1500, 0, 0),
											 new Punto3D(1500, 0, 0),
											 new Punto3D(0, 1500, 0),
											 new Punto3D(0, -1500, 0),
											 new Punto3D(0, 1500, 0),
											 new Punto3D(0, 0, 1500),
											 new Punto3D(0, 0, -1500)};

	private Punto3D luzPunto = luces[0];
	

	public Imagen3D() {
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);       

		// Dibujar triangulos
		if (cargado){
			objeto.dibujar(this, g);
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
		System.out.println(f + "-" + c);
		// Mover a centro de coordenadas
		double tx = getCtoX(c), ty = getFtoY(f);

		objeto.trasladar(-tx, -ty, 0);

		// Realizar Zoom
		objeto.escalaIsotropica(2);

		// Restaurar posicion
		objeto.trasladar(tx, ty, 0);

		// Actualizar pantalla
		updateUI();
	}

	public void zoomOut(int f, int c){
		// Mover a centro de coordenadas
		objeto.trasladar(- getCtoX(c), - getFtoY(f), 0);

		// Realizar Zoom
		objeto.escalaIsotropica(0.5);

		// Restaurar posicion
		objeto.trasladar(getCtoX(c), getFtoY(f), 0);

		// Actualizar pantalla
		updateUI();
	}

	public int getVista() {
		return vista;
	}
	
	public void setVista(int vista){
		this.vista = vista;
		
		updateUI();
	}
	
	public boolean isBackfaceCulling() {
		return backfaceCulling;
	}

	public void setBackfaceCulling(boolean backfaceCulling) {
		this.backfaceCulling = backfaceCulling;
		
		updateUI();
	}

	public void setPerspectiva(boolean perspectiva) {
		this.perspectiva = perspectiva;
		
	}

	public boolean isPerspectiva() {
		return perspectiva;
	}

	public Punto3D getLuzPunto() {
		return luzPunto;
	}
	
	public void setLuzPunto(int luzIndex){
		this.luzPunto = luces[luzIndex];
		updateUI();
	}

	public void setObjeto(Objeto3D objeto) {
		this.objeto = objeto;
		
	}

	public void setCargado(boolean cargado) {
		this.cargado = cargado;		
	}
}
