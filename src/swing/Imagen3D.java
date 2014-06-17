package swing;

import graphic.Incidence;
import graphic.Objeto3D;
import graphic.Punto3D;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import matriz.MatrizEscala;
import matriz.MatrizRotacionX;
import matriz.MatrizRotacionY;
import matriz.MatrizTraslacion;

public class Imagen3D extends JPanel {

	private static final long serialVersionUID = 1L;

	private boolean cargado;
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

	private Color objectColor;

	private Color ambientColor;
	
	public Imagen3D() {
		setBorder(BorderFactory.createLineBorder(Color.black));
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

	public void rotar(int deltaF, int deltaC) {
		Punto3D centro = objeto.getCentro();
		double cx = centro.getX();
		double cy = centro.getY();
		double cz = centro.getZ();				
		
		// Mover a centro de coordenadas
		objeto.aplicarTransformacion(new MatrizTraslacion(-cx, -cy, -cz));

		// Realizar rotacion
		objeto.aplicarTransformacion(new MatrizRotacionX((double) (deltaF * Math.PI / 180)));
		objeto.aplicarTransformacion(new MatrizRotacionY((double) (deltaC * Math.PI / 180)));

		// Restaurar posicion
		objeto.aplicarTransformacion(new MatrizTraslacion(cx, cy, cz));

	}

	public void trasladar(int deltaF, int deltaC){		
		objeto.aplicarTransformacion(new MatrizTraslacion(deltaC, -deltaF, 0));
	}

	public void zoomIn(int f, int c){
		zoom(mapToObjectX(c), mapToObjectY(f), 2);
	}

	public void zoomOut(int f, int c){
		zoom(mapToObjectX(c), mapToObjectY(f), 0.5);
	}
	
	public void zoom(double tx, double ty, double s){
		// Mover a centro de coordenadas
		objeto.aplicarTransformacion(new MatrizTraslacion(-tx, -ty, 0));

		// Realizar Zoom
		objeto.aplicarTransformacion(new MatrizEscala(s));

		// Restaurar posicion
		objeto.aplicarTransformacion(new MatrizTraslacion(tx, ty, 0));

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
		
		setCargado(true);
		updateUI();
	}

	public void setCargado(boolean cargado) {
		this.cargado = cargado;		
	}
	
	public boolean isCargado(){
		return cargado;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);       

		if (cargado){
			Vector<Incidence> elementos = objeto.getElementos();
			HashMap<Integer, Punto3D> puntos = objeto.getPuntos();
			
			// Dibujar triangulos
			for (Incidence elemento : elementos){
				elemento.dibujar(this, g, puntos);
			}
		}
	}

	public int mapToScreenX(double x) {
		return (int) (getWidth() / 2 + x);
	}

	public int mapToScreenY(double y) {
		return (int) (getHeight() / 2 - y);
	}
	
	private double mapToObjectY(int y) {
		return getHeight() / 2 - y;
	}

	private double mapToObjectX(int x) {
		return x - getWidth() / 2;
	}
	
	public void setObjectColor(Color objectColor){
		this.objectColor = objectColor;
		
		updateUI();
	}
	
	public Color getObjectColor(){
		return objectColor;
	}

	public void setAmbientColor(Color ambientColor) {
		this.ambientColor = ambientColor;
		
		updateUI();
	}

	public Color getAmbientColor() {
		return ambientColor;
	}
	
}
