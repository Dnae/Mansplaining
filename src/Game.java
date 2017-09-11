import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Game extends Observable implements Serializable{

	private static final long serialVersionUID = 429423423358L;
	
	int savenum;
	
	int[][] ground;

	Player[] players;
	int currentplayer;
	
	public Game(int id, int pl, int size){
		this.savenum = id;
		ground = new int[size][size];
		
		players = new Player[pl];
		
		startGame();
		drawMap();
	}
	
	private void startGame(){
		boolean canstart = true;
		
		for(int i = 0; i < players.length; i++){
			if(players[i] == null){
				canstart = false;
			}
		}
		
		if(canstart){
			currentplayer = 0;
		}
	}
	
	private void drawMap(){
		for(int i = 0; i < ground.length; i++){
			for(int j = 0; j < ground[0].length; j++){
				ground[i][j] = 1;
				if(Math.abs(i - j) < 3){
					ground[i][j] = 4;
				}
			}
		}
	}
	
	private void nextTurn(){
		currentplayer++;
		if(currentplayer > players.length){
			currentplayer = 0;
			//TODO NEW WEEK
		}
	}
	
	public Player getActivePlayer(){
		return players[currentplayer];
	}
	
	public int getGround(int x, int y){
		if(x >= 0 && x < ground.length && y >= 0 && y < ground[0].length){
			return ground[x][y];
		}
		else{
			return 0;
		}
	}
	
	public boolean setGround(int x, int y, int ind){
		if(x >= 0 && x < ground.length && y >= 0 && y < ground[0].length){
			ground[x][y] = ind;
			return true;
		}
		else{
			return false;
		}
	}
		
	public void save(){
		try {
			// Write to disk with FileOutputStream
			FileOutputStream f_out = new FileOutputStream(savenum + ".game");


			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

			obj_out.writeObject(this);

			obj_out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Game loadFile(String str){
		
		Game sf = null;

		FileInputStream f_in;

		File f = new File(str);

		if(f.exists()){
			try {
				f_in = new FileInputStream(str);


				ObjectInputStream obj_in = 	new ObjectInputStream(f_in);

				Object obj = null;
				try {
					obj = obj_in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				if (obj instanceof Game)
				{
					sf = (Game) obj;
				}
				
				obj_in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sf;
	}
	
}
