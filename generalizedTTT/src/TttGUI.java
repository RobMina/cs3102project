import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TttGUI extends JFrame {

	private static final int WIDTH = 900;
	private static final int HEIGHT = 700;

	private Container content;
	private JLabel result;
	private JButton[] cells;
	private JButton exitButton;
	private JButton initButton;
	private CellButtonHandler[] cellHandlers;
	private ExitButtonHandler exitHandler;
	private InitButtonHandler initHandler;

	private int order, dimension, numSpaces;
	private boolean player1;
	private boolean gameOver;

	public TttGUI(int o, int d) {
		order = o;
		dimension = d;
		numSpaces = o * o;
		setTitle("Tic-Tac-Toe Board");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		content = getContentPane();
		content.setBackground(Color.blue.darker());

		GridLayout gl = new GridLayout(order, order);
		content.setLayout(gl);

		// Create cells and handlers
		cells = new JButton[numSpaces];
		cellHandlers = new CellButtonHandler[numSpaces];
		for (int i = 0; i < numSpaces; i++) {
			cells[i] = new JButton(Integer.toString(i));
			cellHandlers[i] = new CellButtonHandler();
			cells[i].addActionListener(cellHandlers[i]);
		}

		// Create init and exit buttons and handlers
		exitButton = new JButton("EXIT");
		exitHandler = new ExitButtonHandler();
		exitButton.addActionListener(exitHandler);
		initButton = new JButton("CLEAR");
		initHandler = new InitButtonHandler();
		initButton.addActionListener(initHandler);

		// Create result label
		result = new JLabel("player1", SwingConstants.CENTER);
		result.setForeground(Color.white);

		// Add elements to the grid content pane
		for (int i = 0; i < numSpaces; i++) {
			content.add(cells[i]);
		}
		// gl.addLayoutComponent("", initButton);
		// gl.addLayoutComponent("", result);
		// gl.addLayoutComponent("", exitButton);
		// content.add(initButton);
		// content.add(result);
		// content.add(exitButton);

		// Initialize
		init();
	}

	public void init() {
		// Initialize booleans
		player1 = true;
		gameOver = false;

		// Initialize text in buttons
		for (int i = 0; i < numSpaces; i++) {
			cells[i].setText(Integer.toString(i));
		}

		// Initialize result label
		result.setText("player1");

		setVisible(true);
	}

	public boolean checkWinner() {
		if (cells[0].getText().equals(cells[1].getText())
				&& cells[1].getText().equals(cells[2].getText())) {
			return true;
		} else if (cells[3].getText().equals(cells[4].getText())
				&& cells[4].getText().equals(cells[5].getText())) {
			return true;
		} else if (cells[6].getText().equals(cells[7].getText())
				&& cells[7].getText().equals(cells[8].getText())) {
			return true;
		} else if (cells[0].getText().equals(cells[3].getText())
				&& cells[3].getText().equals(cells[6].getText())) {
			return true;
		} else if (cells[1].getText().equals(cells[4].getText())
				&& cells[4].getText().equals(cells[7].getText())) {
			return true;
		} else if (cells[2].getText().equals(cells[5].getText())
				&& cells[5].getText().equals(cells[8].getText())) {
			return true;
		} else if (cells[0].getText().equals(cells[4].getText())
				&& cells[4].getText().equals(cells[8].getText())) {
			return true;
		} else if (cells[2].getText().equals(cells[4].getText())
				&& cells[4].getText().equals(cells[6].getText())) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		int[] od = promptOrderAndDimension();
		TttGUI gui = new TttGUI(od[0], od[1]);

		JFrame player0moves = new JFrame();
		player0moves.setTitle("Player 0 (X) Moves");
		player0moves.setSize(450, 350);
		player0moves.setLocation(900, 0);
		JFrame player1moves = new JFrame();
		player1moves.setTitle("Player 1 (O) Moves");
		player1moves.setSize(450, 350);
		player1moves.setLocation(900, 350);

		JTextArea p0m = new JTextArea();
		p0m.setEditable(false);
		p0m.setLineWrap(true);
		JTextArea p1m = new JTextArea();
		p1m.setEditable(false);
		p1m.setLineWrap(true);

		player0moves.add(p0m);
		player0moves.setVisible(true);
		player1moves.setVisible(true);

	}

	private class CellButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// If game over, ignore
			if (gameOver) {
				return;
			}

			// Get button pressed
			JButton pressed = (JButton) (e.getSource());

			// Get text of button
			String text = pressed.getText();

			// If player1 or player2, ignore
			if (text.equals("O") || text.equals("X")) {
				return;
			}

			// Add nought or player2
			if (player1) {
				pressed.setText("X");
			} else {
				pressed.setText("O");
			}

			// Check winner
			if (checkWinner()) {
				// End of game
				gameOver = true;

				// Display winner message
				if (player1) {
					result.setText("player1 wins!");
				} else {
					result.setText("player2 wins!");
				}
			} else {
				// Change player
				player1 = !player1;

				// Display player message
				if (player1) {
					result.setText("player1's turn");
				} else {
					result.setText("player2's turn");
				}
			}
		}
	}

	public static int[] promptOrderAndDimension() {
		// get order and dimension from the user
		int o = 0, d = 0;
		JTextField order = new JTextField(5);
		JTextField dimension = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Order:"));
		myPanel.add(order);
		myPanel.add(Box.createHorizontalStrut(10)); // a spacer
		myPanel.add(new JLabel("Dimension:"));
		myPanel.add(dimension);

		do {
			int result = JOptionPane.showConfirmDialog(null, myPanel,
					"Please Enter Order and Dimension",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_CANCEL_OPTION
					|| result == JOptionPane.CLOSED_OPTION) {
				System.exit(0);
			}
			if (result == JOptionPane.OK_OPTION) {
				try {
					o = Integer.parseInt(order.getText());
					d = Integer.parseInt(dimension.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,
							"Please only enter integers.");
				}
				if (d != 2) {
					JOptionPane
							.showMessageDialog(null,
									"Error in dimension! Only dimension of 2 is allowed (for now).");
				}
				if (o < 3 || o > 100) {
					JOptionPane
							.showMessageDialog(null,
									"Error in order! Order must be between 3 and 100. ");
				}
			}

		} while (o < 3 || o > 100 || d != 2);

		int[] od = { o, d };
		return od;
	}

	private class ExitButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	private class InitButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			init();
		}
	}
}
