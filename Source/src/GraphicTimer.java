import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Ryper on 2015. 12.
 * +..
 */
public class GraphicTimer extends JPanel{
    Timer _aktTimer = null;
    int seconds = 0;
    int _width = 0;

    BufferedImage _clock,_num0,_num1,_num2,_num3,_num4,_num5,_num6,_num7,_num8,_num9;

    final int BUTTON_WIDTH = 20;
    int MINES_HORIZONTALLY;
    int HORIZONTAL_ENDING;

    GraphicTimer(int width)
    {
        setOpaque(false);
        _width = width;
        MINES_HORIZONTALLY = _width;
        HORIZONTAL_ENDING = 15+BUTTON_WIDTH*MINES_HORIZONTALLY;

        int delay = 1000; //milliseconds
        _aktTimer = new Timer(delay, taskPerformer);

        loadPic();
    }
    void loadPic()
    {
        try
        {
            _clock = ImageIO.read(this.getClass().getResource("clock.png"));
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
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

            g.drawImage(_clock,HORIZONTAL_ENDING-54,22, null);

        /// Drawing numbers
        //00X
        int firstnum  =  seconds %  10;
        int secondnum = (seconds %  100)  / 10;
        int thirdnum  = (seconds %  1000) / 100;
        drawNumber(6,firstnum,g);
        drawNumber(5,secondnum,g);
        drawNumber(4,thirdnum,g);
//        int drawpos_x = 0;
//        final int drawpos_y = 22 + 2;
//        drawpos_x = HORIZONTAL_ENDING-54+2 + 17*2;
//
//        g.drawImage(_num0,drawpos_x,drawpos_y,null);
        //TODO Ide betolteni aknaszamlalot

    }

    //Pos determines which of the six positions do you want to draw a number(XXX XXX)
    void drawNumber(int pos, int number,Graphics g) //TODO Dobhatna hib�t ha nem 1<= pos <= 6
    {
        int drawpos_x = 0;
        final int drawpos_y = 22 + 2;
        switch(pos)
        {
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

    public void start()
    {
        seconds = 0;
        _aktTimer.start();
    }
    public void stop()
    {
        _aktTimer.stop();
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
