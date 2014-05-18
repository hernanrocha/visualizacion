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
	private static final int MIN_PIXELS = 1;

	public MouseHandler(Imagen3D panelImagen) {
		this.panelImagen = panelImagen;
	}

	public void mouseWheelMoved(MouseWheelEvent evento){
		System.out.println(evento.getScrollAmount());
		System.out.println(evento.getPreciseWheelRotation());
		System.out.println(evento.getScrollType());
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
		
		if(mover && newTime - oldTime  > REFRESH_PERIOD){
			oldTime = newTime;
			
			int difX = evento.getX() - xPos;
			int difY = evento.getY() - yPos;
			
			if(rotar){
				// Rotar

				panelImagen.rotar(xPosPressed, yPosPressed, difX, difY);
//				panelImagen.objeto.trasladar(-xPosPressed, - ( panelImagen.getHeight() - yPosPressed ), 0);
//				panelImagen.objeto.rotacionX((double) (difY * Math.PI / 360));
//				panelImagen.objeto.rotacionY((double) (difX * Math.PI / 360));
//				panelImagen.objeto.trasladar(xPosPressed, panelImagen.getHeight() - yPosPressed, 0);
			}else{
				// Mover
				panelImagen.objeto.trasladar(difX, -difY, 0);
				System.out.println("Movido: " + difX + " - " + difY);
			}
			
			panelImagen.updateUI();
			
			xPos = evento.getX();
			yPos = evento.getY();
		}
		
	}
	
	public boolean isRotar() {
		return rotar;
	}

	public void setRotar(boolean rotar) {
		this.rotar = rotar;
	}

	@Override
	public void mouseMoved(MouseEvent evento){
//		frame.setPosicionActual(evento.getY(), evento.getX());
	}

//	public boolean isMover() {
//		return mover;
//	}

	public void setMover(boolean mover) {
//		System.out.println("Set mover " + mover);
		this.mover = mover;
	}

//	public boolean isZoom() {
//		return zoom;
//	}

	public void setZoom(boolean zoom) {
		System.out.println("Set zoom " + zoom);
		this.zoom = zoom;
	}
	
}

