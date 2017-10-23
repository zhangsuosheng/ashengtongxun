package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class FindFriends extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JScrollPane scrollPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
					FindFriends frame = new FindFriends();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	Vector cols=new Vector();
	Vector rows=new Vector();
	
	public FindFriends() {
		setResizable(false);
		setTitle("\u67E5\u627E\u597D\u53CB");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel(" \u67E5 \u627E \u6635 \u79F0\uFF1A");
		label.setBounds(10, 13, 93, 15);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(120, 7, 196, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("\u67E5 \u627E");
		button.setBounds(326, 9, 93, 23);
		contentPane.add(button);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 60, 424, 179);
		contentPane.add(scrollPane);
		
		cols.add("Í«≥∆");
		cols.add(" «∑Ò‘⁄œﬂ");
		
		table = new JTable(rows,cols);
		scrollPane.setViewportView(table);
	}
}
