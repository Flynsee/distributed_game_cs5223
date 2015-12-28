package GUI;

import javax.swing.JFrame;

import Game.Maze;
import Signal.Listener;

public class GameWindow extends JFrame implements Listener{

	private static final long serialVersionUID = 1L;
	private GamePanel grid;
	
	public GameWindow(Maze maze){
		init(maze);
	}
	public GameWindow(Maze maze, String title){
		this.setTitle(title);
		init(maze);
	}
	public void init(Maze maze){
		maze.bind(this);
		grid=new GamePanel(maze);
		setContentPane(grid);
		setSize(500,500);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public void event() {
		this.repaint();
	}
}
