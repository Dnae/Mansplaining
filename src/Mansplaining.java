import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Mansplaining {

	public static void main(String[] arr){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Mansplaining();
			}
		});
	}
	
	JFrame gamewindow = new JFrame(){
		{
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			setTitle("Mansplaining");
			setUndecorated(true);
		}
	};
	JPanel holdingpanel = new JPanel(new BorderLayout());
	
	Game g;
	
	public Mansplaining(){
		gamewindow.setVisible(true);
		gamewindow.add(holdingpanel);
		
		LoadingScreen loading = new LoadingScreen();
		holdingpanel.add(loading);
		
		new Thread(){
			public void run(){
				g = new Game(1, 20);
				holdingpanel.remove(loading);
				startGame(g);
				SwingUtilities.updateComponentTreeUI(holdingpanel);
			}
		}.start();		
	}
	
	private void startGame(Game g){		
		holdingpanel.add(new Field(g));
	}
}
