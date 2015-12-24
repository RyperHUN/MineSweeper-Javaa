import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
 
public class LeaderBoard extends JPanel{
    private String SERIALIZED_FILE = "leader.txt";
    //A jatekosnak a neve aki epp megnyerte és az ideje
    public LeaderBoard(String level,long time) {
        super(new GridLayout(1,0)); // 2 0
        boolean loaded = false;
        Object[][] oldData = new Object[3][3];
        try{
	        ObjectInputStream loader = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE));
			oldData = (Object[][])loader.readObject();
			loader.close();
			loaded = true;  // Sikeresen betoltotte
        }
        catch(Exception e)
        {
        	loaded = false; // Ha exception van akkor olyan mintha nem toltotte volna be a fajlt
        }
        String[] columnNames = {"Difficulty",
                                "Name",
                                "Time(ms)"};
        Object[][] data = {
        {"Beginner", "Bob", new Long(0)},
        {"Intermediate", "Alice", new Long(0)},
        {"Expert", "Black", new Long(0)}
        				  };
        if(!loaded)
        	oldData = data;
        updateTable(oldData,level,time);
        
        
        JTable table = new JTable(oldData, columnNames);
        table.setEnabled(false);  // This makes the table non editable
        table.setPreferredScrollableViewportSize(new Dimension(500, 60));
        table.setFillsViewportHeight(true);

 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        
        
        
        //TODO Megcsinalni hogy menure kattintva legyen Leaderboard vagyis ez (PopUp ba)
        
//        JButton myButton = new JButton("Open new window");
//        JFrame newFrame = new JFrame("New Window");  
//        //add this line of code
//        myButton.addActionListener(new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//        // open a new frame i.e window
//	        newFrame.pack();
//	        newFrame.setVisible(true);
//        }
//        });
        
        //Add the scroll pane to this panel.
        add(scrollPane);
//        add(myButton);
//        createAndShowGUI(this);
    }
    
    void updateTable(Object oldData[][],String level,long time)
    {
    	if(time != 0)
    	{
	    	for(int i = 0; i < 3 ;i++)
	    	{
	    		if(oldData[i][0].equals(level))
	    		{
	    			if( (Long)oldData[i][2] > time || (Long)oldData[i][2] == 0)
	    			{
	    				oldData[i][2] = time;
	    				oldData[i][1] = JOptionPane.showInputDialog("You have the fastest time for " + level + ", please enter your name");
	    				try {
	    					ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(SERIALIZED_FILE));
	    					saver.writeObject(oldData);
	    					saver.close();
	    				}
	    				catch (IOException e) 
	    				{
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    				break;
	    			}
	    		}
	    	}
    	}
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI(LeaderBoard leaderBoard) {
        //Create and set up the window.
        JFrame frame = new JFrame("RyperSweeper Leaderboard");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        leaderBoard.setOpaque(true); //content panes must be opaque
        frame.setContentPane(leaderBoard);
        frame.setResizable(false);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
}
