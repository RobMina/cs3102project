import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TttGUI extends JPanel {

	static JTextField textfield = new JTextField(20);
	static JTextArea textarea = new JTextArea(5, 7);

	public static void main(String[] args) {
		JFrame frame = new JFrame("Generalized Tic-tac-toe");
		frame.setSize(600, 400);
		frame.setLocation(40, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setContentPane(new /*Change THIS--->*/ BeerPong());
		frame.setVisible(true);

		JPanel panel = null;
		panel = new JPanel(new BoxLayout(panel, 5));

		JLabel label = new JLabel("T");

		panel.add(label);

		panel.add(textarea);

		JButton button = new JButton("Search");

		panel.add(button);

		panel.add(textfield);

		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.setVisible(true);

	}

	static class Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String text = textfield.getText();
			System.out.println(text);
			textarea.append(text);
			textfield.selectAll();
		}
	}

}
