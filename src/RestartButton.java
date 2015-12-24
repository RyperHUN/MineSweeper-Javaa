import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;


//Serializalas utan eleg furan mukodik a restart button
public class RestartButton extends JButton implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DrawerField _field;
	ImageIcon _idleSmile,_pressedSmile,_sadSmile;
	static boolean _mouseOn = false;
	static boolean _clickedOn = false;
	RestartButton(DrawerField field)
	{
		_field = field;
		this.addMouseListener(this);
		_idleSmile = new ImageIcon(this.getClass().getResource("idleSmile.png"));
		_pressedSmile = new ImageIcon(this.getClass().getResource("pressedSmile.png"));
		_sadSmile = new ImageIcon(this.getClass().getResource("sadSmile.png"));
		setIcon(_idleSmile);
		setSize(36,36);
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
	}
	void sadSmile()
	{
		setIcon(_sadSmile);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		_mouseOn = true;
		if(_clickedOn)
			setIcon(_pressedSmile);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setIcon(_idleSmile);
		_mouseOn = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		setIcon(_pressedSmile);
		_clickedOn = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(_mouseOn && _clickedOn)
		{
			_clickedOn = false;
			_field.restart();
		}
		setIcon(_idleSmile);
	}
}
