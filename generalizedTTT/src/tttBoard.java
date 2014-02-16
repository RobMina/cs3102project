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
	HashMap<Integer, Integer> magicSquare;
	TreeSet<Integer> player0Spots;
	TreeSet<Integer> player1Spots;
	final int MAX_SPACES = 1000000;

	public static void main(String args[]) {
		// just testin stuff
		tttBoard testBoard = new tttBoard(10, "x", "o");
		// testBoard.printMagicSquare();
		String input = "8430 8481 8532 8583 8634 8685 8736 8787 8838 8889 8940 8991 9042 9093 9144 9195 9246 9297 9348 9399 9450 9501 9552 9603 1    52   103  154  205  256  307  358  409  460  511  562  613  664  715  766  817  868  919  970  1021 1072 1123 1174 1225 6029 6080 6131 6182 6233 6284 6335 6386 6437 6488 6539 6590 6641 6692 6743 6794 6845 6896 6947 6998 7049 7100 7151 7202 4803 4854 2504 2555 2606 2657 2708 2759 2810 2861 2912 2963 3014 3065 3116 3167 3218 3269 3320 3371 3422 3473 3524 3575 3626 ";
		String[] vals = input.split("\\s+");
		int total = 0;
		for (String x : vals) {
			total += Integer.parseInt(x);
		}
		// System.out.println(total + " should equal " + testBoard.magicNum);

	}

	public tttBoard(Integer size, String p0, String p1) {
		this.size = size;
		magicNum = size * (size * size + 1) / 2;
		player0 = p0;
		player1 = p1;
		boardMap = new HashMap<Integer, String>(size * size, (float) 1.0);
		magicSquare = new HashMap<Integer, Integer>(size * size, (float) 1.0);
		loadMagicSquare();
	}

	// load Integer values into the correct values in the magicSquare map
	private void loadMagicSquare() {

		if (size < 3)
			throw new RuntimeException("invalid size for tic-tac-toe board.");

		// generation for odd sizes
		if (size % 2 == 1) {
			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					// formula given by wikipedia
					int value = size * ((row + col - 1 + size / 2) % size)
							+ ((row + 2 * col - 2) % size) + 1;
					magicSquare.put(getKey(row - 1, col - 1), value);
				}
			}
			// printMagicSquare();
		}

		// generation for doubly even (divisible by 4) sizes.
		// http://www.1728.org/magicsq2.htm
		if (size % 4 == 0) {
			// fills the "diagonals"
			int count = 1;
			final int smaller_size = size / 4;
			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					int key = getKey(row - 1, col - 1);
					// fills in the corner squares that are size/4 x size/4
					if ((col <= smaller_size && row <= smaller_size)
							|| (col > size - smaller_size && row > size
									- smaller_size)
							|| (col <= smaller_size && row > size
									- smaller_size)
							|| (col > size - smaller_size && row <= smaller_size)) {
						magicSquare.put(key, count);
					}

					// fills the inner square that is size/2 x size/2
					if (col > smaller_size && col <= size - smaller_size
							&& row > smaller_size && row <= size - smaller_size) {
						magicSquare.put(key, count);
					}

					count++;
				}
			}

			// fills in the rest from the bottom right to top left
			count = 1;
			for (int row = size - 1; row >= 0; row--) {
				for (int col = size - 1; col >= 0; col--) {
					int key = getKey(row, col);
					if (!magicSquare.containsKey(key)) {
						magicSquare.put(key, count);
					}
					count++;
				}
			}
			// printMagicSquare();
		}

		// generation for singly even (divisible by 2 but not by 4) sizes
		// http://www.1728.org/magicsq3.htm
		if (size % 2 == 0 && !(size % 4 == 0)) {

			final int smaller_size = size / 2;

			// fills in the board with smaller odd size magic squares
			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					// formula given by wikipedia
					int value = smaller_size
							* ((row + col - 1 + smaller_size / 2) % smaller_size)
							+ ((row + 2 * col - 2) % smaller_size) + 1;
					magicSquare.put(getKey(row - 1, col - 1), value);
				}
			}

			// increments the quadrants so that they match the ACDB filling
			// pattern
			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					int key = getKey(row - 1, col - 1);
					int value = magicSquare.get(key);
					int s2 = smaller_size * smaller_size;

					// quadrant A stays the same
					// quadrant B
					if (row > smaller_size && col > smaller_size)
						magicSquare.put(key, value + s2);

					// quadrant C
					if (row <= smaller_size && col > smaller_size)
						magicSquare.put(key, value + 2 * s2);

					// quadrant D
					if (row > smaller_size && col <= smaller_size)
						magicSquare.put(key, value + 3 * s2);

				}
			}

			// swap (almost) ALL the values!

			// swaps right side values
			int rightWidth = (smaller_size - 3) / 2;

			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					if (col > size - rightWidth && row <= size / 2) {
						// unnecessary variables are unnecessary
						int key = getKey(row - 1, col - 1);
						int swapkey = key + (size * size) / 2;
						swapValues(key, swapkey);
					}
				}
			}

			// swaps left side values
			int leftWidth = rightWidth + 1;

			for (int row = 1; row <= size; row++) {
				for (int col = 1; col <= size; col++) {
					if (col <= leftWidth && row <= size / 2) {
						// unnecessary variables are unnecessary
						int key = getKey(row - 1, col - 1);
						int swapkey = key + (size * size) / 2;
						swapValues(key, swapkey);
					}
				}
			}
			// accounts for indent on left side values
			swapValues(getKey(leftWidth, 0), getKey(leftWidth, 0)
					+ (size * size) / 2);
			swapValues(getKey(leftWidth, 0) + leftWidth, getKey(leftWidth, 0)
					+ leftWidth + (size * size) / 2);
		}

	}

	// params row and col: coordinates within the square (0 to
	// size^2-1)
	// return int key corresponding to the key in the maps for those coords
	private int getKey(int row, int col) {
		return size * row + col;
	}

	// param player: 0 or 1, the player number
	// params row and col: coordinates within the square
	// return boolean true if move successful, false otherwise
	private boolean move(int player, int row, int col) {
		if (boardMap.containsKey(getKey(row, col))) { // already occupied
			return false;
		}
		if (player != 0 && player != 1) { // invalid player
			return false;
		}
		if (row >= size || col >= size) { // invalid coords
			return false;
		}
		// valid move
		boardMap.put(getKey(row, col), player == 1 ? player1 : player0);
		if (player == 1) {
			player1Spots.add(magicSquare.get(getKey(row, col)));
		} else {
			player0Spots.add(magicSquare.get(getKey(row, col)));
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

	// prints magic square in row-major oder
	private void printMagicSquare() {
		System.out.println("start");
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				// System.out.println("(" + row + ", " + col + "): "
				// + magicSquare.get(getKey(row, col)));
				if (magicSquare.containsKey(getKey(row, col))) {
					int value = magicSquare.get(getKey(row, col));
					System.out.print(value);
					if (value < 10000)
						System.out.print(" ");
					if (value < 1000)
						System.out.print(" ");
					if (value < 100)
						System.out.print(" ");
					if (value < 10)
						System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println("end");
	}

	// x and y are the keys
	private void swapValues(int x, int y) {
		int xval = magicSquare.get(x);
		int yval = magicSquare.get(y);
		magicSquare.put(x, yval);
		magicSquare.put(y, xval);
	}
}