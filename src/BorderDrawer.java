import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BorderDrawer extends JPanel{
	private int _width,_height;

	final int BUTTON_WIDTH = 20,BUTTON_HEIGHT = 20;
	int MINES_HORIZONTALLY;
	int MINES_VERTICALLY;

	//Clock
	int HORIZONTAL_ENDING;
	int HORIZONTAL_BEGINNING;

	Timer _aktTimer = null;
	int seconds = 0;

	BufferedImage _clock,_num0,_num1,_num2,_num3,_num4,_num5,_num6,_num7,_num8,_num9,_minus;

	//Mine counter
	int _mineNum;

	BorderDrawer(int width,int height,int mineNum)
	{
		setOpaque(false);
		_width = width;
		_height = height;
		_mineNum = mineNum;

		MINES_HORIZONTALLY = _width;
		MINES_VERTICALLY = _height;


		//Clock
		HORIZONTAL_ENDING = 15+BUTTON_WIDTH*MINES_HORIZONTALLY;
		HORIZONTAL_BEGINNING = 21;

		int delay = 1000; //milliseconds
		_aktTimer = new Timer(delay, taskPerformer);

		loadPic();
	}

	@Override
    protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		try{
	        BufferedImage topLeftCorner = ImageIO.read(this.getClass().getResource("topLeftCorner.png"));
	        BufferedImage topLine = ImageIO.read(this.getClass().getResource("topLine.png"));
	        BufferedImage topLeftLine = ImageIO.read(this.getClass().getResource("topLeftLine.png"));
	        g.drawImage(topLeftCorner, 0, 0, null); // see javadoc for more info on the parameters
	        int HORIZONTAL_ENDING = 15+BUTTON_WIDTH*MINES_HORIZONTALLY;
	        for(int i = 15; i <= HORIZONTAL_ENDING; i++)
	        {
	        	g.drawImage(topLine, i, 0, null);
	        }
	        BufferedImage topRightCorner = ImageIO.read(this.getClass().getResource("topRightCorner.png"));
	        int rightLineStartX = 15+BUTTON_WIDTH*MINES_HORIZONTALLY+1;
	        g.drawImage(topRightCorner, rightLineStartX, 0, null);
	        int MIDDLE_LINE_END = 55;
	        BufferedImage topRightLine = ImageIO.read(this.getClass().getResource("topRightLine.png"));
	        for(int i = 14; i <= MIDDLE_LINE_END;i++)
	        {
	        	g.drawImage(topRightLine, rightLineStartX, i, null);
	        }
	        BufferedImage middleRightCorner = ImageIO.read(this.getClass().getResourceAsStream("middleRightCorner.png"));
	        g.drawImage(middleRightCorner, rightLineStartX, MIDDLE_LINE_END+1, null);
	        for(int i = 14; i <= MIDDLE_LINE_END; i++)
	        {
	        	g.drawImage(topLeftLine, 0, i, null);
	        }
	        BufferedImage middleLeftCorner = ImageIO.read(this.getClass().getResource("middleLeftCorner.png"));
	        g.drawImage(middleLeftCorner, 0, 56, null);
	        BufferedImage bottomRightLine = ImageIO.read(this.getClass().getResource("bottomRightLine.png"));
	        int BOTTOM_LEFT_START = 70;
	        int BOTTOM_LEFT_ENDING = 70+BUTTON_HEIGHT*MINES_VERTICALLY;
	        for(int i = BOTTOM_LEFT_START; i < BOTTOM_LEFT_ENDING; i++)
	        {
	        	g.drawImage(bottomRightLine, rightLineStartX-1, i, null);
	        }
	        BufferedImage bottomRightCorner = ImageIO.read(this.getClass().getResource("bottomRightCorner.png"));
	        g.drawImage(bottomRightCorner,rightLineStartX-1,BOTTOM_LEFT_ENDING,null);

	        //
	        BufferedImage middleLine = ImageIO.read(this.getClass().getResource("middleLine.png"));
	        for(int i = 15; i <= HORIZONTAL_ENDING; i++)
	        {
	        	g.drawImage(middleLine, i, MIDDLE_LINE_END+1, null);
	        }
	        BufferedImage bottomLeftLine = ImageIO.read(this.getClass().getResource("bottomLeftLine.png"));

	        for(int i = BOTTOM_LEFT_START; i < BOTTOM_LEFT_ENDING; i++)
	        {
	        	g.drawImage(bottomLeftLine, 0, i, null);
	        }

	        BufferedImage bottomLeftCorner = ImageIO.read(this.getClass().getResource("bottomLeftCorner.png"));
	        g.drawImage(bottomLeftCorner,0,BOTTOM_LEFT_ENDING,null);
	        BufferedImage bottomLine = ImageIO.read(this.getClass().getResourceAsStream("bottomLine.png"));
	        for(int i = 15; i <= HORIZONTAL_ENDING; i++)
	        {
	        	g.drawImage(bottomLine, i, BOTTOM_LEFT_ENDING, null);
	        }

			//CLOCK

			g.drawImage(_clock,HORIZONTAL_ENDING-54,22, null);

			/// Drawing numbers
			//00X
			int number[] = new int[6];
			number[3]  =  seconds %  10;
			number[4]   = (seconds %  100)  / 10;
			number[5]  = (seconds %  1000) / 100;
			drawNumber(6,number[3],g);
			drawNumber(5, number[4], g);
			drawNumber(4, number[5], g);

			//Mine counter

			g.drawImage(_clock, HORIZONTAL_BEGINNING, 22, null);

			if(_mineNum >= 0)
			{
				number[0] = _mineNum % 10;
				number[1] = (_mineNum % 100) / 10;
				number[2] = (_mineNum % 1000) / 100;
			}
			else if(_mineNum < 0) //Ha tobb zaszlot raktunk mint akna van
			{
				number[0] = _mineNum % 10 * -1;
				number[1] = (_mineNum % 100) / 10 * -1;
				number[2] = -1;
			}
			drawNumber(3,number[0],g);
			drawNumber(2,number[1],g);
			drawNumber(1,number[2],g);
			//TODO 0 alatt rajta ki a minuszt mashogy szamoljon stb

        }
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
    }
	void loadPic()
	{
		try
		{
			_clock = ImageIO.read(this.getClass().getResource("clock.png"));
			_minus = ImageIO.read(this.getClass().getResource("cNum-.png"));
			_num0  = ImageIO.read(this.getClass().getResource("cNum0.png"));
			_num1  = ImageIO.read(this.getClass().getResource("cNum1.png"));
			_num2  = ImageIO.read(this.getClass().getResource("cNum2.png"));
			_num3  = ImageIO.read(this.getClass().getResource("cNum3.png"));
			_num4  = ImageIO.read(this.getClass().getResource("cNum4.png"));
			_num5  = ImageIO.read(this.getClass().getResource("cNum5.png"));
			_num6  = ImageIO.read(this.getClass().getResource("cNum6.png"));
			_num7  = ImageIO.read(this.getClass().getResource("cNum7.png"));
			_num8  = ImageIO.read(this.getClass().getResource("cNum8.png"));
			_num9  = ImageIO.read(this.getClass().getResource("cNum9.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	//Pos determines which of the six positions do you want to draw a number(XXX XXX)
	void drawNumber(int pos, int number,Graphics g) //TODO Dobhatna hib?t ha nem 1<= pos <= 6
	{
		int drawpos_x = 0;
		final int drawpos_y = 22 + 2;
		switch(pos)
		{
			case 1:
				drawpos_x = HORIZONTAL_BEGINNING +2;
				break;
			case 2:
				drawpos_x = HORIZONTAL_BEGINNING +2 + 17;
				break;
			case 3:
				drawpos_x = HORIZONTAL_BEGINNING +2 + 17*2;
				break;
			case 4:
				drawpos_x = HORIZONTAL_ENDING-54+2;
				break;
			case 5:
				drawpos_x = HORIZONTAL_ENDING-54+2 + 17;
				break;
			case 6:
				drawpos_x = HORIZONTAL_ENDING-54+2 + 17*2;
			default:
				break;
		}
		if(drawpos_x > 0)
		{
			switch (number)
			{
				case -1:
					g.drawImage(_minus,drawpos_x,drawpos_y,null);
					break;
				case 0:
					g.drawImage(_num0,drawpos_x,drawpos_y,null);
					break;
				case 1:
					g.drawImage(_num1,drawpos_x,drawpos_y,null);
					break;
				case 2:
					g.drawImage(_num2,drawpos_x,drawpos_y,null);
					break;
				case 3:
					g.drawImage(_num3,drawpos_x,drawpos_y,null);
					break;
				case 4:
					g.drawImage(_num4,drawpos_x,drawpos_y,null);
					break;
				case 5:
					g.drawImage(_num5,drawpos_x,drawpos_y,null);
					break;
				case 6:
					g.drawImage(_num6,drawpos_x,drawpos_y,null);
					break;
				case 7:
					g.drawImage(_num7,drawpos_x,drawpos_y,null);
					break;
				case 8:
					g.drawImage(_num8,drawpos_x,drawpos_y,null);
					break;
				case 9:
					g.drawImage(_num9,drawpos_x,drawpos_y,null);
					break;
			}
		}
	}

	public void startTimer()
	{
		seconds = 0;
		_aktTimer.start();
	}
	public void stopTimer()
	{
		_aktTimer.stop();
	}
	public void changeMineNum(int change)
	{
		_mineNum += change;
		repaint();
	}


	private ActionListener taskPerformer = new ActionListener()
	{
		public void actionPerformed(ActionEvent evt) // Itt tortenik az ora allitasa
		{
//            System.out.println("sec++");
			seconds++;
			repaint();
		}
	};
}
