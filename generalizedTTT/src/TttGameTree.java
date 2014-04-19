import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Robert Mina (ram2aq)
 * @author Jenny Xing (yx4qu)
 * 
 * A class that represents a tree of all possible TTT boards
 * descending from a particular board configuration.
 */
public class TttGameTree {

	private TttBoard root;
	private int toMove; //player # of who is about to move
	private HashMap<Integer,TttGameTree> children;
	private int treeCount = 0;
	
	/**
	 * 
	 * @param parent's board
	 * @param key of the spot that was previously taken
	 * @param player who just moved
	 */
	public TttGameTree(TttGameTree parent, int key, int player) {
		toMove = player==1 ? 0 : 1;
		root = new TttBoard(parent.root);
		root.moveSuppressWin(player, key);
		children = new HashMap<Integer,TttGameTree>();
	}
	
	public TttGameTree(TttBoard rootBoard, int toMove) {
		root = rootBoard;
		this.toMove = toMove;
		children = new HashMap<Integer,TttGameTree>();
		populateChildren();
	}
	
	public int populateChildren() {
		int count = 0;
		HashSet<Integer> openSpots = root.getOpenSpots();
		while (treeCount < 1000) {
			if (openSpots.size() < 1) break;
			for (int key : openSpots) {
				if (!children.containsKey(key)) {
					children.put(key, new TttGameTree(this, key, toMove));
					count++;
					treeCount++;
					if (treeCount > 1000) break;
				}
			}
			for (TttGameTree child : children.values()) {
				int temp = child.populateChildren(1000 - treeCount);
				count += temp;
				treeCount += temp;
				if (treeCount > 1000) break;
			}
		}
		return count;
	}
	
	private int populateChildren(int max) {
		int count = 0;
		HashSet<Integer> openSpots = root.getOpenSpots();
		while (count < max) {
			if (openSpots.size() < 1) break;
			for (int key : openSpots) {
				if (!children.containsKey(key)) {
					children.put(key, new TttGameTree(this, key, toMove));
					count++;
					treeCount++;
					if (count > max) break;
				}
			}
			for (TttGameTree child : children.values()) {
				int temp = child.populateChildren(max - count);
				count += temp;
				treeCount += temp;
				if (count > max) break;
			}
		}
		return count;
	}
	
	public TttGameTree selectChild(int key) {
		TttGameTree newRoot = children.get(key);
		newRoot.populateChildren();
		return newRoot;
	}
	
	public boolean isWin(int player) {
		HashSet<Integer> pairs = (player == 0) ? root.getPlayer0Pairs() 
					: root.getPlayer1Pairs();
		for (int i : pairs)
			if (i == root.magicNum)
				return true;
		return false;
	}
	
}
