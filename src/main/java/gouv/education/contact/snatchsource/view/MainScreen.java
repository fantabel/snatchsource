package gouv.education.contact.snatchsource.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MainScreen extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenuItem menuItemQuit;
	
	public MainScreen() {
		super("SnatchSource");
		init();
	}
	
	private void init() {
		this.getContentPane().setLayout(new BorderLayout());
		
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel h = new JLabel("Hello World!");
		menuBar = new JMenuBar();
		menuFile = new JMenu("Fichier");
		menuFile.setMnemonic(KeyEvent.VK_A);
		menuFile.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menuFile);
		
		menuItemQuit = new JMenuItem("Quitter", KeyEvent.VK_T);
		menuItemQuit.setAccelerator(
		        KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItemQuit.getAccessibleContext().setAccessibleDescription("Quitter");
		menuFile.add(menuItemQuit);
		
		this.setJMenuBar(menuBar);
		
		this.add(h, BorderLayout.CENTER);
		/*
		 * listPanel = new JPanel(new BorderLayout());
		 * 
		 * searchPanel = new SearchPanel(); filterPanel = new FilterPanel();
		 * tablePanel = new TablePanel(); comparePanel = new ComparePanel();
		 */
		
	}
	
	private void addComponentsToPane() {
		/*
		 * listPanel.add(filterPanel, BorderLayout.PAGE_START);
		 * listPanel.add(tablePanel, BorderLayout.PAGE_END);
		 * 
		 * this.getContentPane().add(searchPanel, BorderLayout.PAGE_START);
		 * this.getContentPane().add(listPanel, BorderLayout.CENTER);
		 * this.getContentPane().add(comparePanel, BorderLayout.PAGE_END);
		 */
		
	}
	
	public static MainScreen createAndShowGUI() {
		MainScreen frame = new MainScreen();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addComponentsToPane();
		
		// Display the window.
		frame.pack();
		return frame;
		
	}
}
