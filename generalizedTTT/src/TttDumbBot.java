/**
 * @author Robert Mina (ram2aq)
 * @author Jenny Xing (yx4qu)
 * 
 * A class that implements an AI to play tic-tac-toe with 2 players.
 * A dumb bot that only: checks for a winning move, checks for a win block,
 * and then picks a random spot.
 */

import java.util.*;

public class TttDumbBot implements TttBot {

	private TttBoard board;
	private int id; // the player # of this object

	public TttDumbBot(TttBoard board, int id) {
		this.board = board;
		this.id = id;
	}

	/**
	 * @return int representing key of spot to be taken
	 */
	public int move() {
		HashSet<Integer> openSpots = board.getOpenSpots();
		HashSet<Integer> myPairs = board.getPlayerPairs(id);
		HashSet<Integer> oppPairs = board.getPlayerPairs(id == 0 ? 1 : 0);

		if (openSpots.size() == 0)
			return -1; // no open spots

		int key;
		if ((key = findWin(openSpots, myPairs)) != -1)
			return key;
		if ((key = findWinBlock(openSpots, oppPairs)) != -1)
			return key;
		return randomMove(openSpots);
	}

	/**
	 * Find a move that will win on this turn
	 * 
	 * @param openSpots
	 * @param myPairs
	 * @return int representing move to make, or -1 if no winning move found
	 */
	private int findWin(HashSet<Integer> openSpots, HashSet<Integer> myPairs) {
		int winningMove;
		for (int i : myPairs) {
			winningMove = board.magicNum - i;
			if (winningMove < 0)
				continue;
			if (openSpots.contains(winningMove))
				return winningMove;
		}
		return -1;
	}

	/**
	 * Find a move that will block an opponent win on next turn
	 * 
	 * @param openSpots
	 * @param oppPairs
	 * @return int representing move to make, or -1 if not blocking move found
	 */
	private int findWinBlock(HashSet<Integer> openSpots,
			HashSet<Integer> oppPairs) {
		int blockingMove;
		for (int i : oppPairs) {
			blockingMove = board.magicNum - i;
			if (blockingMove < 0)
				continue;
			if (openSpots.contains(blockingMove))
				return blockingMove;
		}
		return -1;
	}

	/**
	 * Because we are using HashSet, this will be an expensive O(n) operation.
	 * 
	 * @param openSpots
	 * @return int representing random move
	 */
	private int randomMove(HashSet<Integer> openSpots) {
		Random myRand = new Random();
		while (true) {
			int next = myRand.nextInt(TttBoard.pow(board.order, board.dimension));
			if (openSpots.contains(next))
				return next;
		}
	}

	public int randomMove() {
		HashSet<Integer> openSpots = board.getOpenSpots();
		return randomMove(openSpots);
	}

	public int firstMove() {
		HashSet<Integer> openSpots = board.getOpenSpots();
		if (board.order%2 == 1) // there is only one center spot
		{
			int center = TttBoard.pow(board.order, board.dimension)/2;
			if (openSpots.contains(center))
				return center;
		}
		else { //there are multiple "center" spots
			int dimCoord = board.order/2;
			int[] coords = new int[board.dimension];
			for (int i=0; i<board.dimension; i++)
				coords[i] = dimCoord;
			int key = board.getKey(coords);
			//note the following could be much more elegant
			//also, it only works for 3d
			if (openSpots.contains(key))
				return key;
			key++;
			coords[0]++;
			if (openSpots.contains(key))
				return key;
			coords[0]--;
			coords[1]++;
			key += board.dimension-1;
			if (openSpots.contains(key))
				return key;
			coords[0]++;
			key++;
			if (openSpots.contains(key))
				return key;
			if (board.dimension > 2) { //ie. 3d
				coords[2]++;
				coords[0]--;
				coords[1]--;
				key = board.getKey(coords);
				if (openSpots.contains(key))
					return key;
				key++;
				coords[0]++;
				if (openSpots.contains(key))
					return key;
				coords[0]--;
				coords[1]++;
				key += board.dimension-1;
				if (openSpots.contains(key))
					return key;
				coords[0]++;
				key++;
				if (openSpots.contains(key))
					return key;
			}
		}
		return randomMove(openSpots);
	}

	@Override
	public void registerMove(int key) {
		return; //this is the dumb bot, don't do anything
	}
}
