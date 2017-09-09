import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class LoadingScreen extends JPanel{

	public LoadingScreen(){
		super();
		add(new JPanel(){
			
			@Override 
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.fillOval(0, 0, 100, 100);
			}
		});
	}
	

}
