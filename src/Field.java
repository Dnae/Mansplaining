import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Field extends JPanel{

	Image[] misc = new Image[5];
	Image[][] ground = new Image[6][4];
	Image[] unknown = new Image[10];

	Display display;
	int tilesize = 32;
	int animationcount = 0;
	
	Game game;
	
	int screen_x = 0;
	int screen_y = 0;
	int mouse_x = 1;
	int mouse_y = 1;
	
	public Field(Game g){
		super();
		setBackground(Color.GRAY);
		loadImages();
		
		display = new Display();
		this.game = g;
		
		(new Thread(display)).start();
		
		addMouseListener(new MouseAdapter(){

			@Override
			public void mouseReleased(MouseEvent e) {
				int x = (e.getX() * 1400 / getWidth() + screen_x % tilesize) / tilesize + screen_x / tilesize;
				int y = (e.getY() * 800 / getHeight() + screen_y % tilesize) / tilesize + screen_y / tilesize;
				
				game.setGround(x, y, 3);
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent e) {
				mouse_x = e.getX();
				mouse_y = e.getY();
			}

			public void mouseDragged(MouseEvent e) {
				//TODO
			} 
		});
		repaint();
	}
	
	private void loadImages(){
		misc[0] = ImageBank.getImage("misc/bg");
		
		//LOADGROUND
		for(int i = 0; i < 6; i++){
			if(i > 3){
				for(int j = 0; j < 4; j++){
				ground[i][j] = ImageBank.getImage("ground/" + i + j);
				}
			}
			else{
				ground[i][0] = ImageBank.getImage("ground/" + i);
			}
		}
		
		//LOAD UNKNOWN
		for(int i = 0; i < 10; i++){
			unknown[i] = ImageBank.getImage("ground/10" + i);
		}
		
	}
	
	private int getWindowWidth(){
		return getWidth();
	}
	
	private int getWindowHeight(){
		return getHeight();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(display, 0, 0, getWidth(), getHeight(), null);

	}
	
	private class Display extends BufferedImage implements Runnable{

		public Display(){
			super(1400, 800, BufferedImage.TYPE_INT_ARGB);
		}

		public void drawImage(){
			Graphics g = getGraphics(); 
			g.drawImage(misc[0], 0, 0, 1400, 800, null);
			
			int x0 = screen_x / tilesize;
		   	if(screen_x < 0 && screen_x % tilesize != 0){
		   		x0--;
		   	}
			int y0 = screen_y / tilesize;
			if(screen_y < 0 && screen_y % tilesize != 0){
		   		y0--;
		   	}
			
			int xs = screen_x % tilesize; 
			if(screen_x < 0 && xs != 0){
				xs = tilesize + xs;
			}
			int ys = screen_y % tilesize;
			if(screen_y < 0 && ys != 0){
				ys = tilesize + ys;
			}
			
			int xu = 0;
			int yu = 0;
			
			Player p = game.getActivePlayer();
			for(int i = 0; (i * tilesize - xs) < 1400; i++){
				for(int j = 0; (j * tilesize - ys) < 800; j++){
					int gr = game.getGround(i + x0, j + y0);
					int ph = 0;
					
					if(gr == 4){
						ph = animationcount / 8;
						if(game.getGround(i + x0 - 1, j + y0) == 4 && game.getGround(i + x0 + 1, j + y0) == 4 && game.getGround(i + x0, j + y0 - 1) == 4 && game.getGround(i + x0, j + y0 + 1) == 4){
							gr = 5;
						}
					}
					
					g.drawImage(ground[gr][ph], i * tilesize - xs, j * tilesize - ys, tilesize, tilesize, null);					
					xu = i*tilesize;
					yu = j*tilesize;
					
				}
			}

			g.drawImage(ground[2][0], - xs, - ys, tilesize, tilesize, null);
			g.drawImage(ground[2][0],xu - xs,yu - ys, tilesize, tilesize, null);
			
			repaint();
			
		}
		
		@Override
		public void run() {
			while(true){
				
				long x = System.currentTimeMillis();
				
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
//				System.out.println(System.currentTimeMillis() - x);
				
				if(mouse_x < 1){
					screen_x -= 8;
				}
				else if(mouse_x >= getWindowWidth() - 1){ 
					screen_x += 8;
				}
				if(mouse_y < 1){
					screen_y -= 8;
				}
				else if(mouse_y >= getWindowHeight() - 1){
					screen_y += 8;
				}
					
				animationcount++;
				if(animationcount > 31){
					animationcount = 0;
				}
				
				display.drawImage();
				
				
			}
		}
	}
}
