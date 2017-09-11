import java.io.Serializable;

public class Player implements Serializable{

	private static final long serialVersionUID = 192837465L;

	int id;
	int[][] seen;
	
	public Player(int id, int size){
		this.id = id;
		seen = new int[size][size];
		
		System.out.println(seen[0][0]);
	}
	
	public int[][] getSeen(){
		return seen;
	}
	
}
