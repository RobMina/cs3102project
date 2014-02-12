/**
 * @author Robert Mina (ram2aq)
 * @author Jenny Xing (yx4qu)
 */

import java.util.*;

public class tttBoard {

	int size;
	int magicNum;
	String player0;
	String player1;
	HashMap<Integer, String> boardMap;
	HashMap<Integer, Integer> perfectSquare;
	TreeSet<Integer> player0Spots;
	TreeSet<Integer> player1Spots;

	public static void main(String args[]) {
		// needed main to run for testing purposes
	}

	public tttBoard(Integer size, String p0, String p1) {
		this.size = size;
		magicNum = size * (size * size + 1) / 2;
		player0 = p0;
		player1 = p1;
		boardMap = new HashMap<Integer, String>(size * size, (float) 1.0);
		perfectSquare = new HashMap<Integer, Integer>(size * size, (float) 1.0);
		loadPerfectSquare();
	}

	// load Integer values into the correct values in the perfectSquare map
	private void loadPerfectSquare() {

		if (size < 3)
			throw new RuntimeException(
					"magic squares of size less than 3 cannot be generated.");

		if (size % 2 == 1) {
			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					int num = size * ((row + col - 1 + size / 2) % size)
							+ ((row + 2 * col - 2) % size) + 1;
					perfectSquare.put(getKey(col - 1, row - 1), num);
					// System.out.println("(" + (row - 1) + ", " + (col - 1)
					// + "): " + num);
				}

			}
		}
		// TODO implement for even sizes
	}

	// params x(column) and y(row): coordinates within the square (0 to
	// size^2-1)
	// return int key corresponding to the key in the maps for those coords
	private int getKey(int x, int y) {
		return x + size * y;
	}

	// param player: 0 or 1, the player number
	// params x and y: coordinates within the square
	// return boolean true if move successful, false otherwise
	private boolean move(int player, int x, int y) {
		if (boardMap.containsKey(getKey(x, y))) { // already occupied
			return false;
		}
		if (player != 0 && player != 1) { // invalid player
			return false;
		}
		if (x >= size || y >= size) { // invalid coords
			return false;
		}
		// valid move
		boardMap.put(getKey(x, y), player == 1 ? player1 : player0);
		if (player == 1) {
			player1Spots.add(perfectSquare.get(getKey(x, y)));
		} else {
			player0Spots.add(perfectSquare.get(getKey(x, y)));
		}
		return true;
	}

	// check for win or draw conditions
	private void check() {
		if (player0Spots.size() < size && player1Spots.size() < size)
			return; // not possible that someone has won
		int[] nPair = new int[size];
		int[] nIndices = new int[size];
		ArrayList<Integer> player0SpotsList = new ArrayList<Integer>(
				player0Spots);
		for (int i = 0; i < player0SpotsList.size() - size; i++) {
			// loop over elements in the list, grabbing each possible
			// combination of size spots and seeing if they sum to the magic
			// number
			for (int j = 0; j < size - 1; j++) { // assign indices
				nIndices[j] = i + j;
				nPair[j] = player0SpotsList.get(nIndices[j]);
			} // indices default to first unchecked size elements
			while (true) {
				int sum = sum(nPair);
				if (sum == magicNum) {
					win(player0);
					return;
				} else if (sum > magicNum)
					break; // continue to next starting digit
				else { // re-assign indices and nPair
					if (nIndices[size - 1] == player0SpotsList.size())
						break; // no more nPairs with this first value
					for (int j = size - 1; j >= 0; j--) {
						nIndices[j]++;
						nPair[j] = player0SpotsList.get(nIndices[j]);
					}
				}
			}
		}
		ArrayList<Integer> player1SpotsList = new ArrayList<Integer>(
				player1Spots);
		for (int i = 0; i < player1SpotsList.size() - size; i++) {
			// loop over elements in the list, grabbing each possible
			// combination of size spots and seeing if they sum to the magic
			// number
			for (int j = 0; j < size - 1; j++) { // assign indices
				nIndices[j] = i + j;
				nPair[j] = player1SpotsList.get(nIndices[j]);
			} // indices default to first unchecked size elements
			while (true) {
				int sum = sum(nPair);
				if (sum == magicNum) {
					win(player1);
					return;
				} else if (sum > magicNum)
					break; // continue to next starting digit
				else { // re-assign indices and nPair
					if (nIndices[size - 1] == player1SpotsList.size())
						break; // no more nPairs with this first value
					for (int j = size - 1; j >= 0; j--) {
						nIndices[j]++;
						nPair[j] = player1SpotsList.get(nIndices[j]);
					}
				}
			}
		}

		// TODO: check for draw
		// if (boardMap.size() < )
	}

	private int sum(int[] nPair) {
		int retval = 0;
		for (int i = 0; i < size; i++) {
			retval += nPair[i];
		}
		return retval;
	}

	// perform the winning operation given the winner
	private void win(String winner) {

	}
}