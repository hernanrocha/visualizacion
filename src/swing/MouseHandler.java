package swing;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseHandler extends MouseAdapter {
	private static final int REFRESH_PERIOD = 20;
	private int xPos;
	private int yPos;
	private Imagen3D panelImagen;
	private long oldTime = 0;
	
	private int xPosPressed;
	private int yPosPressed;
	private Visualizador3D frame;
	
	public MouseHandler(Visualizador3D frame, Imagen3D panelImagen) {
		this.frame = frame;
		this.panelImagen = panelImagen;		

		// Add Mouse Handler
		panelImagen.addMouseListener(this);
		panelImagen.addMouseMotionListener(this);
		panelImagen.addMouseWheelListener(this);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent evento){
		if(!panelImagen.isCargado())
			return;
		
		double sentido = evento.getPreciseWheelRotation();
		
		if(sentido < 0){
			panelImagen.zoomIn(evento.getY(), evento.getX());
		}else{
			panelImagen.zoomOut(evento.getY(), evento.getX());
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent evento){
		xPos = evento.getX();
		yPos = evento.getY();
		xPosPressed = xPos;
		yPosPressed = yPos;
		
	}
	
	@Override
	public void mouseDragged(MouseEvent evento){
		long newTime = System.currentTimeMillis();
		
		if(panelImagen.isCargado() && newTime - oldTime  > REFRESH_PERIOD){
			
			oldTime = newTime;			
			int difX = evento.getX() - xPos;
			int difY = evento.getY() - yPos;
			
			// Procesar
			if(isRightClick(evento)){
				// Rotar
				panelImagen.rotar(yPosPressed, xPosPressed, difY, difX);
			}else if(isLeftClick(evento)){
				// Mover
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
	
	// Getters
	
	public boolean isLeftClick(MouseEvent evento){
		return (evento.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK;
	}
	
	public boolean isWheelClick(MouseEvent evento){
		return (evento.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK;
	}
	
	public boolean isRightClick(MouseEvent evento){
		return (evento.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK;
	}
	
}

