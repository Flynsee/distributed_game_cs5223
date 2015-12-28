package GUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import Game.Maze;

public class GamePanel extends JPanel {
	private Maze maze;
	
	public GamePanel(Maze maze) {
		this.maze=maze;
	}

	public void paintComponent(Graphics g){
		paintMaze(g);
	}
		
	public void paintMaze(Graphics g){
		if(maze.getHeight()==0 || maze.getWidth()==0)
			return;
	    int rH=getHeight()/maze.getHeight();
	    int rW=getWidth()/maze.getWidth();
	    int h=getHeight();
	    int w=getWidth();
	    int s=Math.min(rH,rW)*8/10;
	    int dH=(rH-s)/2;
	    int dW=(rW-s)/2;
	    for(int i=0; i<maze.getHeight(); i++){
	    	g.drawLine(0,i*rH,w,i*rH);
	    }
	    for(int i=0; i<maze.getWidth(); i++){
	    	g.drawLine(i*rW,0,i*rW,h);
	    }
	    for(int x=0; x<maze.getWidth(); x++){
		    for(int y=0; y<maze.getHeight(); y++){
		    	if(maze.isTreasure(x, maze.getHeight()-y-1)){
			    	if(maze.isFreeTreasure(x, maze.getHeight()-y-1))
			    		g.setColor(Color.GREEN);
			    	else
						g.setColor(Color.GRAY);
					g.fillRect(x*rW+dW,y*rH+dH, s,s);
		    	}
				if(maze.isPlayer(x, maze.getHeight()-y-1)){
					g.setColor(Color.PINK);
					g.fillOval(x*rW+dW,y*rH+dH, s,s);
				}
		    }
	    }
	}     
	
	private static final long serialVersionUID = 1L;

}
