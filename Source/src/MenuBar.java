import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar implements ActionListener{
	HashMap<String,JMenuItem> menuItem;
	DrawerField _field;
	public MenuBar(DrawerField field)
	{
		super();
		menuItem = new HashMap<String, JMenuItem>();
		_field = field;
		JMenu menu = new JMenu("Game");
		menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
//		JMenuItem temp = new JMenuItem("New Game");
//		temp.addActionListener(this);
		menuItem.put("NewGame",new JMenuItem("New Game"));
		menuItem.get("NewGame").addActionListener(this);
//		//TODO Line separatort hozza kene adni
		menuItem.put("Save", new JMenuItem("Save"));
		menuItem.get("Save").addActionListener(this);
		menuItem.put("Load", new JMenuItem("Load"));
		menuItem.get("Load").addActionListener(this);
		menuItem.put("Beginner",new JMenuItem("Beginner"));
		menuItem.get("Beginner").addActionListener(this);
		menuItem.put("Intermediate",new JMenuItem("Intermediate"));
		menuItem.get("Intermediate").addActionListener(this);
		menuItem.put("Expert",new JMenuItem("Expert"));
		menuItem.get("Expert").addActionListener(this);
		menuItem.put("LeaderBoard", new JMenuItem("LeaderBoard"));
		menuItem.get("LeaderBoard").addActionListener(this);
		menuItem.put("Reset",new JMenuItem("Reset LeaderBoard"));
		menuItem.get("Reset").addActionListener(this);
		menu.add(menuItem.get("NewGame"));
		menu.add(menuItem.get("Save"));
		menu.add(menuItem.get("Load"));
		menu.addSeparator();
		menu.add(menuItem.get("Beginner"));
		menu.add(menuItem.get("Intermediate"));
		menu.add(menuItem.get("Expert"));
		


		menu.addSeparator();
		menu.add(menuItem.get("LeaderBoard"));
		menu.add(menuItem.get("Reset"));
		
		this.add(menu);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem temp = (JMenuItem)e.getSource();
		if(temp.equals(menuItem.get("NewGame")))
		{
			_field.restart();
		}
		else if(temp.equals(menuItem.get("Beginner")))
		{
			_field.setDifficulty("Beginner");
			_field.restartWNewSize(9, 9, 10);
		}
		else if(temp.equals(menuItem.get("Intermediate")))
		{
			_field.setDifficulty("Intermediate");
			_field.restartWNewSize(18,18,40);
		}
		else if(temp.equals(menuItem.get("Expert")))
		{
			_field.setDifficulty("Expert");
			_field.restartWNewSize(30, 16, 99);
		}
		else if(temp.equals(menuItem.get("Save")))
		{
			_field.save();
		}
		else if(temp.equals(menuItem.get("Load")))
		{
			_field.load();
		}
		else if(temp.equals(menuItem.get("LeaderBoard")))
		{
			LeaderBoard leaderBoard = new LeaderBoard(null,0);
			LeaderBoard.createAndShowGUI(leaderBoard);
		}
		else if(temp.equals(menuItem.get("Reset")))
		{
			File leaderBoard = new File("leader.txt");
			if(leaderBoard.exists())
				leaderBoard.delete();

		}
	}
}
