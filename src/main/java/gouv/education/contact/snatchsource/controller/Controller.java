package gouv.education.contact.snatchsource.controller;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import gouv.education.contact.snatchsource.model.service.SourceHandler;
import gouv.education.contact.snatchsource.view.MainScreen;

public class Controller {
	MainScreen mainFrame;
	
	private void createAndShowGui() {
		mainFrame = MainScreen.createAndShowGUI();
		mainFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		System.out.println("Debut");
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) { // If Nimbus is not available, you can set the
		                        // // GUI to another look and feel. }
		
		}
		
		// Schedule a job for event dispatch thread: // creating and showing
		// this application's GUI.
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Controller c = new Controller();
		        // c.createAndShowGui();
			}
		});
		new SourceHandler().getSource(args[0]);
	}
	
}
