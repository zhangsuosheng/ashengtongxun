package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SelfInformation extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					javax.swing.UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
					SelfInformation frame = new SelfInformation();
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
	public SelfInformation() {
		setTitle("\u4E2A\u4EBA\u4FE1\u606F");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("\u5934\u50CF");
		label.setBounds(10, 10, 80, 80);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(100, 10, 235, 37);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(100, 53, 235, 37);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u4E2A\u4EBA\u8D44\u6599", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setToolTipText("");
		panel.setBounds(10, 111, 414, 290);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel label_1 = new JLabel(" \u59D3 \u540D \uFF1A");
		label_1.setBounds(32, 37, 54, 15);
		panel.add(label_1);
		
		textField_2 = new JTextField();
		textField_2.setBounds(96, 34, 126, 21);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel label_2 = new JLabel(" \u6027 \u522B \uFF1A");
		label_2.setBounds(32, 76, 54, 15);
		panel.add(label_2);
		
		JRadioButton radioButton = new JRadioButton("\u7537");
		radioButton.setName("");
		radioButton.setBounds(96, 72, 54, 23);
		panel.add(radioButton);
		
		JRadioButton radioButton_1 = new JRadioButton("\u5973");
		radioButton_1.setName("");
		radioButton_1.setBounds(154, 72, 54, 23);
		panel.add(radioButton_1);
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(radioButton_1);
		bg.add(radioButton);
		
		JLabel label_3 = new JLabel(" \u5E74 \u9F84 \uFF1A");
		label_3.setBounds(32, 114, 54, 15);
		panel.add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setBounds(100, 113, 66, 21);
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel label_4 = new JLabel(" \u5907 \u6CE8 \uFF1A");
		label_4.setBounds(32, 154, 54, 15);
		panel.add(label_4);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(96, 154, 271, 102);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
	}
}
