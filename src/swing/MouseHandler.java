package swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseHandler extends MouseAdapter {
	private static final int REFRESH_PERIOD = 20;
	private int xPos;
	private int yPos;
	private Imagen3D panelImagen;
	private long oldTime = 0;
	
	private boolean mover, zoom;
	private boolean rotar;
	private int xPosPressed;
	private int yPosPressed;
	private Visualizador3D frame;
	private boolean zoomIn;
	private boolean zoomOut;
	
	private static final int MIN_PIXELS = 1;

	public MouseHandler(Visualizador3D frame, Imagen3D panelImagen) {
		this.frame = frame;
		this.panelImagen = panelImagen;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent evento){
		double sentido = evento.getPreciseWheelRotation();
		
		if(sentido < 0){
			panelImagen.zoomIn(evento.getY(), evento.getX());
		}else{
			panelImagen.zoomOut(evento.getY(), evento.getX());
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent evento){
		if(zoomIn){
			panelImagen.zoomIn(yPosPressed, xPosPressed);
		}else if(zoomOut){
			panelImagen.zoomOut(yPosPressed, xPosPressed);
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent evento){
		xPos = evento.getX();
		yPos = evento.getY();
		xPosPressed = xPos;
		yPosPressed = yPos;
		
		System.out.println("Presionado " + xPos + " - " + yPos);
	}
	
//	@Override
//	public void mouseReleased(MouseEvent evento){
//		if (zoom){
//			int xPosRelease = evento.getX();
//			int yPosRelease = evento.getY();
//			
//			if ( (Math.abs(xPos - xPosRelease) < MIN_PIXELS ) || (Math.abs(yPos - yPosRelease) < MIN_PIXELS) ){
//				return;
//			}
//			
//			System.out.println("Soltado " + xPos + " - " + yPos);
//			
//			if(xPosRelease > xPos){
//				if (yPosRelease > yPos){
//					frame.zoom(yPos, yPosRelease, xPos, xPosRelease);
//				}else{
//					frame.zoom(yPosRelease, yPos, xPos, xPosRelease);
//				}
//			}else{
//				if (yPosRelease > yPos){
//					frame.zoom(yPos, yPosRelease, xPosRelease, xPos);
//				}else{
//					frame.zoom(yPosRelease, yPos, xPosRelease, xPos);
//				}
//			}
//		}
//	}
	
	@Override
	public void mouseDragged(MouseEvent evento){
		
		long newTime = System.currentTimeMillis();
		
		if(newTime - oldTime  > REFRESH_PERIOD){
			
			oldTime = newTime;			
			int difX = evento.getX() - xPos;
			int difY = evento.getY() - yPos;
			
			// Procesar
			if(rotar){
				// Rotar
				System.out.println("Rotar: " + difX + " - " + difY);
				panelImagen.rotar(yPosPressed, xPosPressed, difY, difX);
			}else if(mover){
				// Mover
				System.out.println("Mover: " + difX + " - " + difY);
				panelImagen.trasladar(difY, difX);
			}
			
			// Actualizar pantalla
			panelImagen.updateUI();
			
			xPos = evento.getX();
			yPos = evento.getY();
		}
		
	}


	@Override
	public void mouseMoved(MouseEvent evento){
		frame.setPosicionActual(evento.getY(), evento.getX());
	}
	
	// Setters

	public void setMover(boolean mover) {
		System.out.println("Mover");
		this.mover = mover;
	}

	public void setRotar(boolean rotar) {
		this.rotar = rotar;
	}
	
	public void setZoomOut(boolean zoomOut) {
		this.zoomOut = zoomOut;
	}

	public void setZoomIn(boolean zoomIn) {
		this.zoomIn = zoomIn;
	}

//	public void setZoom(boolean zoom) {
//		System.out.println("Zoom");
//		this.zoom = zoom;
//	}
	
}

