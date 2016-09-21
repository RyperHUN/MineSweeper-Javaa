import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class FieldButton extends JButton implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon _num1,_num2,_num3,_num4,_num5,_num6,_num7,_num8;
	ImageIcon _hidden,_mine,_explodingMine,_empty,
			  _flag,_question, _pressedNormal,_before,_wrongMine;
	static int _mostiUnhide = 0,_mostjUnhide = 0;
	static int _mostiClicked = 0, _mostjClicked = 0;
	static boolean isPressing;
//	Icon _before;
	boolean _isQuestionMarked = false;
	boolean _isHidden;
	boolean _isFlagged = false;
	boolean _isPressed = false;
	static boolean  _isWin;
	public char _hiddenValue;
	int _i,_j;
	DrawerField _field;
	public FieldButton(char hiddenValue,boolean isHidden,int i,int j,DrawerField dfield)
	{
		_isWin = false;
		setSize(20,20);  // Ez most nem csinal semmit???????
		_i = i; 
		_j = j;
		_field = dfield;
		_hiddenValue = hiddenValue;
		_hidden = new ImageIcon(this.getClass().getResource("hidden.png"));
		_mine = new ImageIcon(this.getClass().getResource("mine.png"));
		_explodingMine = new ImageIcon(this.getClass().getResource("explodingMine.png"));
		_wrongMine = new ImageIcon(this.getClass().getResource("wrongMine.png"));
		_empty = new ImageIcon(this.getClass().getResource("empty.png"));
		_question = new ImageIcon(this.getClass().getResource("question.png"));
		_pressedNormal = new ImageIcon(this.getClass().getResource("pressedNormal.png"));
		_num1 = new ImageIcon(this.getClass().getResource("num1.png"));
		_num2 = new ImageIcon(this.getClass().getResource("num2.png"));
		_num3 = new ImageIcon(this.getClass().getResource("num3.png"));
		_num4 = new ImageIcon(this.getClass().getResource("num4.png"));
		_num5 = new ImageIcon(this.getClass().getResource("num5.png"));
		_num6 = new ImageIcon(this.getClass().getResource("num6.png"));
		_num7 = new ImageIcon(this.getClass().getResource("num7.png"));
		_num8 = new ImageIcon(this.getClass().getResource("num8.png"));
		_flag = new ImageIcon(this.getClass().getResource("flag.png"));
		_isHidden = isHidden;
		if(_isHidden)
			setIcon(_hidden);
		else
			setButtonIcon(false);
		this.addMouseListener(this);
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setVisible(true);
//		Border emptyBorder = BorderFactory.createEmptyBorder();
//		setBorder(emptyBorder);
	}
	//Megmutatja az adott gomb alatt "mi rejtozik"
	//Itt teszteli le a jatek azt is hogy vege van e a game nek
	public void showField()
	{
			_isFlagged = false;
			if(_isHidden)
			{
				_isHidden = false;
				_field.isShowed(_i,_j);
				setButtonIcon(true);
				_field.reveal(_i,_j);
			}
			if(!_isWin && _field.checkWin())
			{
				_isWin = true;   // Ha ez nincs benne akkor tobbszor is megnyitja a gameWin() fuggvenyt ami nem valami jó
				_field.showAllIfHidden();
				//TODO Szemuveges szmajli
				_field.gameWin();
			}
	}
	private void setButtonIcon(boolean isRecursion) {
		switch(_hiddenValue)
		{
			case 'X':
				setIcon(_explodingMine);
				_field.reveal(_i,_j);
				_field.gameOver();
				break;
			case '0':
				setIcon(_empty);
				repaint();
				if(isRecursion)
					_field.revealEmptyFields(_i, _j,true);
				break;
			case '1':
				setIcon(_num1);
				break;
			case '2':
				setIcon(_num2);
				break;
			case '3':
				setIcon(_num3);
				break;
			case '4':
				setIcon(_num4);
				break;
			case '5':
				setIcon(_num5);
				break;
			case '6':
				setIcon(_num6);
				break;
			case '7':
				setIcon(_num7);
				break;
			case '8':
				setIcon(_num8);
				break;
		}
	}
	//ezt a fuggvenyt hivja meg a Field osztaly ha veletlen rossz aknara megyunk
	/* Ez felderiti a mezoket  aszerint hogyha:
	 * 		-Zaszlo van rajta es jo helyen van meghagyja
	 *		-Ha rossz helyen van zalszo megjelenit egy X el athuzott aknat 
	 *		-ahol pedig akna lett volna azt megjeleniti
	 */
	void gameOverMine()
	{
		if(this.getIcon() == _flag)
		{
			setIcon(_wrongMine);
		}
		else if(_hiddenValue == 'X' && this.getIcon() != _explodingMine)
		{
			setIcon(_mine);
		}
	}
	// Rarak egy szimbolumot az adott mezore: ?,flag,sima
	void showSymbol()
	{
		if(_isHidden)
		{
			if(!_isFlagged && !_isQuestionMarked)
			{
				_field.setSymbol(_i,_j);
				setIcon(_flag);
				_isFlagged = true;

				//AZ AKNA SZAMLALOBOL LEVONUNK 1et
				_field.graphicMineCounter(-1);
			} 
			else if(_isFlagged && !_isQuestionMarked)
			{
				setIcon(_question);
				_field.setSymbol(_i,_j);
				_isFlagged = false;
				_isQuestionMarked = true;
			} 
			else if(!_isFlagged && _isQuestionMarked)
			{
				setIcon(_hidden);
				_field.setSymbol(_i,_j);
				_isQuestionMarked = false;
			}
			if(!_isWin && _field.checkWin())
			{
				_isWin = true;   // Ha ez nincs benne akkor tobbszor is megnyitja a gameWin() fuggvenyt ami nem valami jó
				_field.showAllIfHidden();
				//TODO Szemuveges szmajli
				_field.gameWin();
			}
		}
	}
	//Egy adott mezo korul megjeleniti amit meglehet "Eger Dupla klikk"
	void showSorroundings()
	{
		if(this.getIcon() != _flag)
		{
			// ha jobb gombal is klikkeltunk nem csak ballal
			if (_hiddenValue != '0') // Tehat ures mezore hivva nem fog egybõl mindent megjeleniteni korulotte
			{
				char mineCounted = _field.mineCounter(_i, _j, false); // false miatt a rendes mezot nezi
				if (_hiddenValue == mineCounted) {
					_field.revealSorroundings(_i, _j);
				}
//			else   //Debugra jo megmutatja hogy hogy all a jatek
//				_field.consoleShow();
			}
		}
	}
	//Duppla kattnal olyan mintha lenyomnad a gombot
	void showAsPressed()  
	{
		//Gombnyomasnal nem nyomja le azt ami mar levan nyomva, zaszlot se, es a kerdojelet se( ezek foloslegesek ugye)
		if(this.getIcon() != _pressedNormal && this.getIcon() != _flag && this.getIcon() != _question)
		{
			_before = (ImageIcon) this.getIcon(); // Elmenti hogy mi volt azelotte mielott lenyomna
			setIcon(_pressedNormal);
		}
	}
	//Dupla katt elengedesenel megmutatja a normalis mezot
	void showAsUnpressed()
	{
		if(this.getIcon() == _pressedNormal)
		{
			setIcon(_before);
			_before = null;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent mouse){  
//		if(_field.isMouseEventEnabled())
//		{
//			if(SwingUtilities.isLeftMouseButton(mouse) && !SwingUtilities.isRightMouseButton(mouse))
//				showField();
//			else if(SwingUtilities.isRightMouseButton(mouse) && !SwingUtilities.isLeftMouseButton(mouse))  // Csak jobb gomb
//				showSymbol();
//		}
	}
	@Override
	public void mouseEntered(MouseEvent mouse) {
		_mostiUnhide = _i; _mostjUnhide = _j;
		_mostiClicked = _i; _mostjClicked = _j;
		if(_field.isMouseEventEnabled())
		{
			if(isPressing)
			{
				if (!_isHidden) {
					if (SwingUtilities.isRightMouseButton(mouse) &&
							SwingUtilities.isLeftMouseButton(mouse))        //Ha bal es jobb gombot egyszerre nyomjuk
						_field.showSorroundingsPressed(_i, _j);//TODO ilyenkor csak "nyomja le1" a korulotte levo mezoket
				}
				else if (_isHidden)
				{
					if (SwingUtilities.isLeftMouseButton(mouse) && !SwingUtilities.isRightMouseButton(mouse))  // csak bal gomb
					{
						showAsPressed();
					}
				}
			}
		}
	}
	@Override
	public void mouseExited(MouseEvent mouse) {
		if(_field.isMouseEventEnabled())
		{
			if(SwingUtilities.isLeftMouseButton(mouse) && !SwingUtilities.isRightMouseButton(mouse))  // csak bal gomb
			{
				showAsUnpressed();
			}
			else if( SwingUtilities.isRightMouseButton(mouse) && SwingUtilities.isLeftMouseButton(mouse))
							_field.showSorroundingsUnpressed(_i, _j);// Ilyenkor nyomott bol visszamegy normalisba a gomb
		}
	}
	//TODO Megkene csinalni hogy minden dupla klikknel visszamennyenek a gombok amit egy valtozoval kene hogy pl isExited
	@Override
	public void mousePressed(MouseEvent mouse) {
		if(_field.isMouseEventEnabled())
		{
			isPressing = true;
			if(SwingUtilities.isLeftMouseButton(mouse) && !SwingUtilities.isRightMouseButton(mouse) && _isHidden)  // csak bal gomb
			{
				showAsPressed();
			}
			else if(SwingUtilities.isRightMouseButton(mouse) && !SwingUtilities.isLeftMouseButton(mouse))  // Csak jobb gomb
			{
				showSymbol(); // Egybol mutatja a szimbolumot
			}
			else if( SwingUtilities.isRightMouseButton(mouse) &&
				SwingUtilities.isLeftMouseButton(mouse))		//Ugyanaz mint mouse entered
			{
					_field.showSorroundingsPressed(_i,_j);      //TODO ilyenkor csak "nyomja le" a korulotte levo mezoket
					_mostiUnhide = _i;
					_mostjUnhide = _j;
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent mouse) {  // TODO Valamiert amikor nem ott engedem el hanem arrebb akkor lenyomva marad
		if(_field.isMouseEventEnabled())
		{
			isPressing = false;
			if(SwingUtilities.isLeftMouseButton(mouse) && !SwingUtilities.isRightMouseButton(mouse))
			{
				_field.showFieldButton(_mostiClicked,_mostjClicked);
			}
			if( SwingUtilities.isRightMouseButton(mouse) &&
				SwingUtilities.isLeftMouseButton(mouse)     )  //Ha bal es jobb gombot egyszerre nyomjuk
			{
				showSorroundings();
				_field.showSorroundingsUnpressed(_mostiUnhide, _mostjUnhide);
			}
		}
	}
	public void showIfHidden() {
		if(this.getIcon() == _hidden)
			this.showField();
	}

}
