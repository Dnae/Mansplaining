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
	Image[] ground = new Image[5];

	Display display;

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
				int x = (e.getX() * 700 / getWidth() + screen_x % 32) / 32 + screen_x / 32;
				int y = (e.getY() * 400 / getHeight() + screen_y % 32) / 32 + screen_y / 32;
				
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
		ground[0] = ImageBank.getImage("ground/0");
		ground[1] = ImageBank.getImage("ground/1");
		ground[2] = ImageBank.getImage("ground/2");
		ground[3] = ImageBank.getImage("ground/3");
		ground[4] = ImageBank.getImage("ground/4");
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
		
		int in = 0;
		
		public Display(){
			super(700, 400, BufferedImage.TYPE_INT_ARGB);
		}

		public void drawImage(){
			Graphics g = getGraphics(); 
			g.drawImage(misc[0], 0, 0, 700, 400, null);
			
			int x0 = screen_x / 32;
		   	if(screen_x < 0 && screen_x % 32 != 0){
		   		x0--;
		   	}
			int y0 = screen_y / 32;
			if(screen_y < 0 && screen_y % 32 != 0){
		   		y0--;
		   	}
			
			int xs = screen_x % 32; 
			if(screen_x < 0 && xs != 0){
				xs = 32 + xs;
			}
			int ys = screen_y % 32;
			if(screen_y < 0 && ys != 0){
				ys = 32 + ys;
			}
			
			int xu = 0;
			int yu = 0;
			
			for(int i = 0; (i * 32 - xs) < 700; i++){
				for(int j = 0; (j * 32 - ys) < 400; j++){
					g.drawImage(ground[game.getGround(i + x0, j + y0)], i * 32 - xs, j * 32 - ys, 32, 32, null);					
					xu = i*32;
					yu = j*32;
					
				}
			}

			g.drawImage(ground[2], - xs, - ys, 32, 32, null);
			g.drawImage(ground[2],xu - xs,yu - ys, 32, 32, null);
			
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
					screen_x -= 4;
				}
				else if(mouse_x >= getWindowWidth() - 1){ 
					screen_x += 4;
				}
				if(mouse_y < 1){
					screen_y -= 4;
				}
				else if(mouse_y >= getWindowHeight() - 1){
					screen_y += 4;
				}
					
				display.drawImage();
				
				
			}
		}
	}
}
