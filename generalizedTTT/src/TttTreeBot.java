/**
 * @author Robert Mina (ram2aq)
 * @author Jenny Xing (yx4qu)
 * 
 * A class that implements an AI to play tic-tac-toe with 2 players.
 * A clever bot that searches in the game space for up to 1000 possibilities
 * before deciding where to move.
 * 
 * If the entire game space is < 1000, this bot will play perfectly.
 *
 */
public class TttTreeBot implements TttBot {

	private TttBoard board;
	private TttGameTree tree;
	private int id; // the player # of this object
	private int prev;
	
	public TttTreeBot(TttBoard board, int id) {
		
	}
	
	@Override
	public int move() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int randomMove() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int firstMove() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registerMove(int key) {
		prev = key;
		updateTree();
	}
	
	//Update the game tree using the value of prev
	//to specify the last move that was made.
	private void updateTree() {
		tree = tree.selectChild(prev);
	}

}
