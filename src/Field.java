import java.io.Serializable;
import java.util.Random;
import javax.swing.JFrame;


public class Field implements Serializable {
	protected char _field[][];       //Sima palya, ezt latja a jatekos
	protected char _hidden[][];		 //Ebbe vannak elrejtve az aknák
	protected boolean _isShowed[][];  //TODO ebbe lesznek hogy megvan e nyitva valahol a mezo
	protected int  _width,_height;   //Megadja a magasságát és a szélességét a mezõknek
	protected int  _mineNum = 0;	 //Aknák száma
	
	char[][] getField() { return _field; }
	char[][] getHidden() { return _hidden; }
	boolean[][] getIsShowed() { return _isShowed; }
	int getWidth() { return _width; }
	int getHeight() { return _height; }
	Field()
	{	
	}
	void initConsole(int height,int width,int mineNum)
	{
		_field  = new char[height][width];   //Mezok letrehozasa
		_hidden = new char[height][width];
		_isShowed = new boolean[height][width];
		_width  = width;
		_height = height;
		for(int i = 0; i< _height; i++)
			for(int j = 0; j < _width; j++)
			{
				_field[i][j] = '#';			//Mezo feltoltese "rejtett" karakterrel
				_isShowed[i][j] = false;
			}
		_mineNum = mineNum;
		addingMines();
	}
	public void isShowed(int _i, int _j) {
		_isShowed[_i][_j] = true;	
	}
	void addingMines()
	{
		if(_mineNum >= (_height*_width/2))          //Ha a palya felenel tobb aknat akarunk megadni akkor leszabalyozza
			_mineNum = _height*_width/2;
		int mine_num = _mineNum;
		Random random = new Random();  				//Letrehoz egy random osztalyt amivel lehet random szamokat generálni
		while(mine_num > 0)
		{
			int random1 = random.nextInt(_height);  //Kreal egy random szamot 0-_height kozott
			int random2 = random.nextInt(_width);   //Kreal egyrandom szamot 0-_width kozott
			if(_hidden[random1][random2] != 'X')    //Ha a krealt random szamon nincs akna akkor rak oda aknát
			{ 
				_hidden[random1][random2] = 'X';
				mine_num--;
			}
		}
		fillHiddenWithNumbers();					//Feltolti a rejtett mezot szamokkal, attol fuggoen hogy egy adott mezohoz kepest
	}												// mennyi akna van
	
	//Megadja hogy adott iteracioval merre lehet tovabb haladni
	Directions getIterDirection(int i, int j)
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
	//Az aknamezot feltolti szamokkal aszerint hogy
	//Merre lehet menni
	void fillHiddenWithNumbers()
	{
		for(int i = 0; i < _height ; i++)
		{
			for(int j = 0; j < _width; j++)
			{
				if(_hidden[i][j] != 'X') // Ha nem akna
				{
					_hidden[i][j] = mineCounter(i,j,true);	   //Az akna szamlalo korbenezi az elozo alapjan hogy mennyi akna van korulotte
				}
			}
		}
	}
	//Megy egy kort az adott mezo kozul es megnezi mennyi akna van
	// @ KARAKTERBE ADJA VISSZA AZ EREDMENYT!!!!! 
	char mineCounter(int i, int j,boolean hidden)
	{
		Directions iterDirection = getIterDirection(i,j);  //Megnezzuk hogy merre haladhatnk tovabb
		char osszeg = '0';  		//Eddig osszesen 0db akna veszi korul, azert van char ként declarálva mert így megjeleníthetõ egybõl
		int ix = i;					//Masolat az eredetirol
		int jx = j;
		if(iterDirection.fel)		//Ha mehetunk felfele, akkor a sorok iteralasat egyel elorebb kezdhetjuk(feljebb)
			ix--; 
		if(iterDirection.bal)		//Ha mehetunk balra a sorok iteralasat egyel balrol kezdhetjuk
			jx--;
		int ihatar = i + 1;			//mivel i < ihatar van ezert alapból 1et hozza kell adni kulonben az adott sort se veszi figyelembe
		int jhatar = j + 1;
		if(iterDirection.le)		//Ha mehetunk le noveljuk a hatart
			ihatar++;
		if(iterDirection.jobb)
			jhatar++;
		if(hidden)
		{
			for(i = ix;i < ihatar; i++)
			{
				for(j = jx;j < jhatar; j++)
				{
					if(_hidden[i][j] == 'X')
					{
						osszeg++;				//Megszamolja hany akna a relytett palyan
					}
				}
			}
		}
		else
		{
			for(i = ix;i < ihatar; i++)
			{
				for(j = jx;j < jhatar; j++)
				{
					if(_field[i][j] == 'Z') //Z mint Zaszlo
					{
						osszeg++;				//Megszamolja hany akna van a rendes palyan
					}
				}
			}
		}
		return osszeg;
	}
	//Az aknamezot mutatja meg, tehat ami az igazi mezo "alatt" van
	//Felfedi az ures mezoket ha a jatekos epp rabokott egyre
	void consoleRevealEmptyFields(int i,int j)
	{
		if(_hidden[i][j] == '0' && _field[i][j] != '0')	//Ha az adott mezo rejtve ures de meg nincs atvive a rendes mezore
		{
			_field[i][j] = '0'; //Atviszi a sima mezore a rejtett mezo tartalmat
			Directions iterDirection = getIterDirection(i,j);  //Megadja melyik iranyba mehetunk tovabb
			if(iterDirection.fel)
			{
				consoleRevealEmptyFields(i-1,j);
			}
			if(iterDirection.bal)
			{
				consoleRevealEmptyFields(i,j-1);
			}
			if(iterDirection.le)
			{
				consoleRevealEmptyFields(i+1,j);
			}
			if(iterDirection.jobb)
			{
				consoleRevealEmptyFields(i,j+1);
			}
		}
		return;
	}
	/*
	 * Annak fuggvenyeben hogy hany rejtett '#' mezo es hany
	 * akna van, megmondja hogy nyert e a jatekos
	 */
	boolean checkWin()   
	{
		int countHidden = 0;
		for(int i = 0; i < _height && countHidden <= _mineNum; i++)
		{
			for(int j = 0; j < _width; j++)
			{
				if(_field[i][j] == 'Z' && _hidden[i][j] == 'X')     // || _field[i][j] == '#'
					countHidden++;
			}
		}
		if(countHidden == _mineNum)   		//Ha egyezik az aknak es a rejtett mezok szama
			return true;
		return false;
	}
	void reveal(int i, int j)
	{
		_field[i][j] = _hidden[i][j];
	}
	void setSymbol(int i, int j)
	{
		if(_field[i][j] == '#')
		{
			_field[i][j] = 'Z';
		}
		else if(_field[i][j] == 'Z')
		{
			_field[i][j] = '?';
		}
		else if(_field[i][j] == '?')
		{
			_field[i][j] = '#';
		}
	}
	// Csak megjeleniti a rejtett palyareszt
	void consoleShowHidden()
	{
		for(int i = 0; i< _height; i++)
		{
			for(int j = 0; j < _width; j++)
			{
				System.out.print(_hidden[i][j]);
			}
			System.out.print('\n');
		}
	}
	void consoleShow()	 //Megjeleniti a sima mezot
	{
		System.out.println("");
		for(int i = 0; i< _height; i++)
		{
			for(int j = 0; j < _width; j++)
			{
				System.out.print(_field[i][j]);
			}
			System.out.print('\n');
		}
	}
}
