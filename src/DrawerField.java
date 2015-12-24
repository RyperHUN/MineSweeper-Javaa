import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
//TODO SZERIALIZALASNAL FLAGAKET NEM TOLTI VISSZA
//TODO SZERIALIZALASNAL 0 R�L INDUL AZ IDO LEHET CSALNI

public class DrawerField extends JFrame{
	private JPanel fieldPanel;
	private JPanel smilePanel;
	private BorderDrawer borderDrawer;
	private RestartButton restartButton;
	private FieldButton _buttons[][];
	private boolean _isMouseEventEnabled;
	private boolean _isRecursiveSlowMo = false;
	
	Field _FIELD;
	protected char _field[][];       //Sima palya, ezt latja a jatekos
	protected char _hidden[][];		 //Ebbe vannak elrejtve az akn�k
	protected boolean _isShowed[][]; // Eltarolja hogy mi volt eddig felfedve
	protected int _height;
	protected int _width;
	protected int _mineNum;
	protected String difficulty = "Beginner";
//	Timer timer; // Ez egy ido mero lesz
	long startTime,endTime;

	public DrawerField() // TODO megcsinalni hogy lehessen allitani a meretet a palyanak
	{
		super("MineSweeper");
		_FIELD = new Field();
		_height = 9;
		_width = 9;
		_mineNum = 10;

		constructorInit();
	}
	public void constructorInit()
	{
//		File serializedFile = new File("saveDefault.ryp");
//		boolean isSerialized = serializedFile.exists();
		startTime = System.currentTimeMillis();

		_FIELD.initConsole(_height,_width,_mineNum);
		loadField();
		
		_buttons = new FieldButton[_height][_width];  // hidden meretut hoz letre
		_isMouseEventEnabled = true;
		fieldPanel = new JPanel();
		smilePanel = new JPanel();
		//INITS
		int fieldSizeWidth = (_width)*20;
		int fieldSizeHeight = (_height)*20; // Magic size
		fieldPanel.setSize(fieldSizeWidth,fieldSizeHeight); // 20x20 os meretu a kep
		fieldPanel.setLocation(15, 70);
		fieldPanel.setLayout(new GridLayout(_height,_width)); // ide majd megfelelo meret
		int fullWindowWidth = fieldSizeWidth+36;
		int fullWindowHeight = fieldSizeHeight+142;
		setSize(fullWindowWidth, fullWindowHeight);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		restartButton = new RestartButton(this);
		smilePanel.setSize(34,34);					// Ha ezt nem irom ide akkor megse jelenik a kep
		smilePanel.add(restartButton);
		smilePanel.setLayout(new GridLayout(1,1));  // Ha ezt nem irom ide akkor egy magikus keretet hoz letre a gomb korul WTF?
		smilePanel.setLocation((int)fullWindowWidth/2-(34/2)-1,20);
		///INITIALS
				
		for(int i = 0; i < _height; i++)
		{
			for(int j = 0; j < _width ; j++)
			{
				_buttons[i][j] = new FieldButton(_hidden[i][j],true,i,j,this);
				fieldPanel.add(_buttons[i][j]);
			}
		}

		add(smilePanel);
		add(fieldPanel);

		borderDrawer = new BorderDrawer(_width,_height,_mineNum);
		borderDrawer.startTimer();
		add(borderDrawer);

		MenuBar menuBar = new MenuBar(this);
		setJMenuBar(menuBar);
		setVisible(true);
	}
	// Ha ures mezore lepsz felfedi azokat
	// TODO Lehet felfedi aztis amin zaszlo van
	// TODO Van mikor a sarokba nem fed fel 1-2 aknat a "sarokba"
	void revealEmptyFields(int i,int j,boolean last)
	{
		if(_hidden[i][j] == '0' && _field[i][j] != '0')	//Ha az adott mezo rejtve ures de meg nincs atvive a rendes mezore
		{
			_field[i][j] = '0'; //Atviszi a sima mezore a rejtett mezo tartalmat
			_buttons[i][j].showField();

//			if(_isRecursiveSlowMo) //TODO Egybe fedi fel az egeszet, valahogy megkene csinalni hogy egyenkent jelenitse meg a gombokat
//			{
//				try {
//					revalidate();
//					_buttons[i][j].revalidate();
//					_buttons[i][j].repaint();
//					SwingUtilities.updateComponentTreeUI(_buttons[i][j]); // ezzel se jo
//					repaint();
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			
			Directions iterDirection = getIterDirection(i,j);  //Megadja melyik iranyba mehetunk tovabb
			if(iterDirection.fel)
			{
				revealEmptyFields(i-1,j,last);
			}
			if(iterDirection.bal)
			{
				revealEmptyFields(i,j-1,last);
			}
			if(iterDirection.le)
			{
				revealEmptyFields(i+1,j,last);
			}
			if(iterDirection.jobb)
			{
				revealEmptyFields(i,j+1,last);
			}
			if(iterDirection.fel && iterDirection.jobb)
			{
				revealEmptyFields(i-1,j+1,last);
			}
			if(iterDirection.fel && iterDirection.bal)
			{
				revealEmptyFields(i-1,j-1,last);
			}
			if(iterDirection.le && iterDirection.bal)
			{
				revealEmptyFields(i+1,j-1,last);
			}
			if(iterDirection.le && iterDirection.jobb)
			{
				revealEmptyFields(i+1,j+1,last);
			}
		}
		else if(last)   // Ha utoljara meg volt felfedve mezo akkor meg 1 et lehet felfedni de utana stopTimer
		{
			_field[i][j] = _hidden[i][j]; //Atviszi a sima mezore a rejtett mezo tartalmat
			_buttons[i][j].showField();
			return;
		}
		return;
	}
	void revealSorroundings(int i,int j)
	{
		Directions iterDirection = getIterDirection(i,j);  //Megnezzuk hogy merre haladhatnk tovabb
		int ix = i;					//Masolat az eredetirol
		int jx = j;
		if(iterDirection.fel)		//Ha mehetunk felfele, akkor a sorok iteralasat egyel elorebb kezdhetjuk(feljebb)
			ix--; 
		if(iterDirection.bal)		//Ha mehetunk balra a sorok iteralasat egyel balrol kezdhetjuk
			jx--;
		int ihatar = i + 1;			//mivel i < ihatar van ezert alapb�l 1et hozza kell adni kulonben az adott sort se veszi figyelembe
		int jhatar = j + 1;
		if(iterDirection.le)		//Ha mehetunk le noveljuk a hatart
			ihatar++;
		if(iterDirection.jobb)
			jhatar++;
			for(i = ix;i < ihatar; i++)
			{
				for(j = jx;j < jhatar; j++)
				{
					if(_field[i][j] != 'Z') 
					{
						_buttons[i][j].showField();
					}
				}
			}
	}
	void showSorroundingsUnpressed(int i,int j)
	{
		Directions iterDirection = getIterDirection(i,j);  //Megnezzuk hogy merre haladhatnk tovabb
		int ix = i;					//Masolat az eredetirol
		int jx = j;
		if(iterDirection.fel)		//Ha mehetunk felfele, akkor a sorok iteralasat egyel elorebb kezdhetjuk(feljebb)
			ix--; 
		if(iterDirection.bal)		//Ha mehetunk balra a sorok iteralasat egyel balrol kezdhetjuk
			jx--;
		int ihatar = i + 1;			//mivel i < ihatar van ezert alapb�l 1et hozza kell adni kulonben az adott sort se veszi figyelembe
		int jhatar = j + 1;
		if(iterDirection.le)		//Ha mehetunk le noveljuk a hatart
			ihatar++;
		if(iterDirection.jobb)
			jhatar++;
			for(i = ix;i < ihatar; i++)
			{
				for(j = jx;j < jhatar; j++)
				{
					if(_field[i][j] == '#')  //TODO Egyellore csak rejtett mezot nyom le a gomb
					{
						_buttons[i][j].showAsUnpressed();
					}
				}
			}
	}
	void showSorroundingsPressed(int i, int j) // TODO FOlytatni
	{
		Directions iterDirection = getIterDirection(i,j);  //Megnezzuk hogy merre haladhatnk tovabb
		int ix = i;					//Masolat az eredetirol
		int jx = j;
		if(iterDirection.fel)		//Ha mehetunk felfele, akkor a sorok iteralasat egyel elorebb kezdhetjuk(feljebb)
			ix--; 
		if(iterDirection.bal)		//Ha mehetunk balra a sorok iteralasat egyel balrol kezdhetjuk
			jx--;
		int ihatar = i + 1;			//mivel i < ihatar van ezert alapb�l 1et hozza kell adni kulonben az adott sort se veszi figyelembe
		int jhatar = j + 1;
		if(iterDirection.le)		//Ha mehetunk le noveljuk a hatart
			ihatar++;
		if(iterDirection.jobb)
			jhatar++;
			for(i = ix;i < ihatar; i++)
			{
				for(j = jx;j < jhatar; j++)
				{
					if(_field[i][j] == '#')  //TODO Egyellore csak rejtett mezot nyom le a gomb
					{
						_buttons[i][j].showAsPressed();
					}
				}
			}
	}
	long getTime()
	{
		endTime = System.currentTimeMillis();
		return endTime-startTime;
	}
	public void gameWin() {
		borderDrawer.stopTimer();
		_isMouseEventEnabled = false;
//		System.out.println("Idod:" + getTime() + "ms");
		new LeaderBoard(difficulty,getTime());
	}
	void gameOver()
	{
		borderDrawer.stopTimer();
//		Ido kiir
		System.out.println("currentTime:" + getTime() + "ms");
		_isMouseEventEnabled = false;
		restartButton.sadSmile();
		for(int i = 0; i < _field.length; i++)
		{
			for(int j = 0; j < _field[0].length; j++)
			{
				if(_field[i][j] == 'Z' && _hidden[i][j] != 'X' || _field[i][j] != 'Z' && _hidden[i][j] == 'X' )
				{
					_buttons[i][j].gameOverMine();
				}
			}
		}
	}
	boolean isMouseEventEnabled()
	{
		return _isMouseEventEnabled;
	}


	void restart()  //Folytatni
	{
		remove(fieldPanel);
		remove(smilePanel);
		remove(borderDrawer);
		constructorInit();

		repaint();
		setVisible(true);
	}
	private void loadField() {
		_field = _FIELD.getField();
		_hidden = _FIELD.getHidden();
		_isShowed = _FIELD.getIsShowed();	
		_height = _FIELD.getHeight();
		_width = _FIELD.getWidth();
	}
	void restartWNewSize(int width,int height,int mineNum)
	{
		_height = height;
		_width = width;
		_mineNum = mineNum;

		remove(fieldPanel);
		remove(smilePanel);
		remove(borderDrawer);

		constructorInit();
		
		setVisible(true);
		repaint();
	}
	public boolean checkWin()
	{
		return _FIELD.checkWin();
	}
	public void setDifficulty(String diff)
	{
		difficulty = diff;
	}

	public void recursiveSlowMo() {
		_isRecursiveSlowMo = !_isRecursiveSlowMo;		
	}
	//TODO Megoldani hogy barmilyen neven elmentse es betoltse barmilyen neven
	public void save()
	{
		try {
			ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream("saveDefault.txt"));
			saver.writeObject(_FIELD);
			saver.close();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void load()
	{ //TODO bes�r�teni egy f�ggv�nybe az egesz folyamatot, igy tul hosszu de mukodik
		try{
			ObjectInputStream loader = new ObjectInputStream(new FileInputStream("saveDefault.txt"));
			_FIELD =  (Field)loader.readObject();
			loader.close();
			loadField(); // Betolti _FIELD bol az adatokat
			startTime = System.currentTimeMillis(); // Timer 0 rol indul ami nem jo
//			this.removeAll();
			_isMouseEventEnabled = true;
			remove(fieldPanel);
			remove(smilePanel);
			remove(borderDrawer);
			
			_buttons = new FieldButton[_height][_width];  // hidden meretut hoz letre
			fieldPanel = new JPanel();
			smilePanel = new JPanel();
			//INITS
			int fieldSizeWidth = (_width)*20;
			int fieldSizeHeight = (_height)*20; // Magic size
			fieldPanel.setSize(fieldSizeWidth,fieldSizeHeight); // 20x20 os meretu a kep
			fieldPanel.setLocation(15, 70);
			fieldPanel.setLayout(new GridLayout(_width,_height)); // ide majd megfelelo meret
			int fullWindowWidth = fieldSizeWidth+36;
			int fullWindowHeight = fieldSizeHeight+142;
			setSize(fullWindowWidth,fullWindowHeight);
//			setResizable(false);
			setDefaultCloseOperation(EXIT_ON_CLOSE);

			restartButton = new RestartButton(this);
			smilePanel.setSize(34,34);					// Ha ezt nem irom ide akkor megse jelenik a kep
			smilePanel.add(restartButton);
			smilePanel.setLayout(new GridLayout(1,1));  // Ha ezt nem irom ide akkor egy magikus keretet hoz letre a gomb korul WTF?
			smilePanel.setLocation((int)fullWindowWidth/2-(34/2),20);
			///INITIALS
					
			for(int i = 0; i < _height; i++)
			{
				for(int j = 0; j < _width ; j++)
				{
						_buttons[i][j] = new FieldButton(_hidden[i][j],!_isShowed[i][j],i,j,this);
						fieldPanel.add(_buttons[i][j]);
				}
			}
//			TODO Hogy rajzoljam ki tobbszor a timert
//			GraphicTimer graphicTimer = new GraphicTimer();
//			timer = new Timer();
//			timer.schedule(new TimerLogic(graphicTimer),0, 1);  //ennek atkell majd adni valami osztalyt ami megvalositja a TimerTask osztalyt es van run metodusa amit futtat a timer
			
			add(fieldPanel);
			add(smilePanel);
			borderDrawer = new BorderDrawer(_width,_height,_mineNum);

			borderDrawer.startTimer();
			add(borderDrawer);
			MenuBar menuBar = new MenuBar(this);
			setJMenuBar(menuBar);
			
			repaint();
			setVisible(true);
		} 
		catch( IOException e)
		{
			e.printStackTrace();
			System.out.println("nemsikerult beolvasni");
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("nemsikerult beolvasni");
		}
	}
	public void graphicMineCounter(int change)
	{
		borderDrawer.changeMineNum(change);
	}
	private Directions getIterDirection(int i, int j)
	{
		Directions iterDirection = new Directions();
		if(j >= 1)
			iterDirection.bal = true;  // Ha nem az elso oszlopban vagyunk
		else
			iterDirection.bal = false;
		if(j < _width-1)
			iterDirection.jobb = true; // Ha nem az utolso oszlopban vagyunk
		else
			iterDirection.jobb = false;
		if(i >= 1)
			iterDirection.fel = true; // Ha nem a legtetejen vagyunk
		else
			iterDirection.fel = false;
		if(i < _height-1)
			iterDirection.le = true;	//Ha nem a legalso sorban vagyunk
		else
			iterDirection.le = false;
		return iterDirection;
	}
	public void isShowed(int i, int j) {
		_FIELD.isShowed(i, j);
	}
	public char mineCounter(int i, int j, boolean b) {
		return _FIELD.mineCounter(i,j,b);
	}
	public void reveal(int i, int j) {
		_FIELD.reveal(i, j);
	}
	public void setSymbol(int i, int j) {
		_FIELD.setSymbol(i, j);
	}
	public void showFieldButton(int i,int j)
	{
		_buttons[i][j].showField();
	}
	public void showAllIfHidden() {
		for(int i = 0; i < _height; i++)
		{
			for(int j = 0; j < _width; j++)
				_buttons[i][j].showIfHidden();
		}
	}
}
