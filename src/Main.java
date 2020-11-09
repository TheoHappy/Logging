import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel {

	private DefaultListModel	model;										// list model
	private JList				list, mirror;								// lists
	private JTextField			tName			= new JTextField(10);		// name field
	private JButton				bAdd			= new JButton("Add");		// add button
	private JButton				bRemove			= new JButton("Remove");	// remove button
	private JRadioButton		rList			= new JRadioButton("List");		// list radio button
	private JRadioButton		rMessage		= new JRadioButton("Message");	// list radio button
	private static Logger logger = Logger.getLogger(Main.class.getName());

	private Mediator med = new Mediator();
	
	public Main() {
		init();
	}
	
	public void init() {
		// initialize model
		logger.info("Initialize model");
		model = new DefaultListModel();
		model.addElement("IDP");
		model.addElement("POO");
		model.addElement("SO");
		
		tName.setName("tname");
		bAdd.setName("bAdd");
		bRemove.setName("bRemove");
		
		// initialize lists, based on the same model
		list = new JList(model);
		list.setName("list");
		mirror = new JList(new ReverseListModel(model));
		logger.info("Initialize lists");
		
		// init radios
		logger.info("Initialize radios");
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rList);
		radioGroup.add(rMessage);
		rList.setSelected(true);
		
		// main panel
		JPanel top = new JPanel(new FlowLayout());
		JPanel middle = new JPanel(new GridLayout(1, 0)); // 1 row, any number of columns
		middle.setName("middle");
		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setName("bottom");
		this.setLayout(new BorderLayout());
		this.add(top, BorderLayout.NORTH);
		this.add(middle, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		
		// top panel: two radios
		top.add(rList);
		top.add(rMessage);
		
		// middle panel: the two lists (scrollable)
		JScrollPane jsp = new JScrollPane(list);
		jsp.setName("scroll");
		
		JScrollPane jsp2 = new JScrollPane(mirror);
		jsp.setName("scroll2");
		
		middle.add(jsp);
		middle.add(jsp2);
		
		// bottom panel: name field, add button, remove button
		logger.info("Add remove and add button");
		bottom.add(tName);
		bottom.add(bAdd);
		bottom.add(bRemove);
		
		// mediator init
		logger.info("Intialize mediator");
		med.registerList(list);
		med.registerMirror(mirror);
		med.registerName(tName);
		med.registerAdd(bAdd);
		med.registerRemove(bRemove);

		logger.debug("Add listener for add button");
		bAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.add();
				/*
				String text = tName.getText();
				if (text.isEmpty()) {
					JOptionPane.showMessageDialog(
							null, "Name is empty!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				model.addElement(text);
				*/
			}
		});

		logger.debug("Add listener for remove button");
		bRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.remove();
				/*
				int index = list.getSelectedIndex();
				if (index == -1) {
					JOptionPane.showMessageDialog(
							null, "No item selected!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				model.remove(index);
				*/
			}
		});
		
		rList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.list();
			}
		});
		
		rMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				med.message();
			}
		});
		
	}
	
	public static void buildGUI() {
		logger.info("Enter in method buildGUI");
		JFrame frame = new JFrame("Swing stuff"); // title
		frame.setContentPane(new Main()); // content
		frame.setSize(300, 300); // width / height
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}


	public static void main(String[] args) {
		// run on EDT (event dispatch thread), not on main thread!
//		For Task#1
//		BasicConfigurator.configure();
//		For Task#2
		PropertyConfigurator.configure("log4j.properties");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI();
			}
		});
	}

}
