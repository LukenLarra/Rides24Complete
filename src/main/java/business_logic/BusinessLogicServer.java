/**
 * Package with the business logic of the application.
 */
package business_logic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.xml.ws.Endpoint;

import configuration.ConfigXML;

/**
 * It runs the business logic server as a separate process.
 */
public class BusinessLogicServer extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JTextArea textArea;
	BLFacade server;
	String service;

	Logger logger = Logger.getLogger(getClass().getName());

	public static void main(String[] args) {
		try {
			BusinessLogicServer dialog = new BusinessLogicServer();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		}
		private void initializeTextArea() {
		textArea = new JTextArea();
		contentPanel.add(textArea);
		}
	

	


	public BusinessLogicServer() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(1);
			}
		});
		setTitle("BusinessLogicServer: running the business logic");
		
		initializeTextArea();
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(e -> {
					textArea.append("\n\n\nClosing the server... ");
					
					System.exit(1);
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		ConfigXML c=ConfigXML.getInstance();

		if (c.isBusinessLogicLocal()) {
			textArea.append("\nERROR, the business logic is configured as local");
		}
		else {
		try {

			try{
				
				if (!c.isDatabaseLocal()) {
					logger.info("\nWARNING: Please be sure ObjectdbManagerServer is launched\n           in machine: "+c.getDatabaseNode()+" port: "+c.getDatabasePort()+"\n");	
				}
				
				service= "http://"+c.getBusinessLogicNode() +":"+ c.getBusinessLogicPort()+"/ws/"+c.getBusinessLogicName();
				
				Endpoint.publish(service, new BLFacadeImplementation());
				
				
			}
			catch (Exception e) {
				logger.info("Error in BusinessLogicServer: "+e.toString());
				textArea.append("\nYou should have not launched DBManagerServer...\n");
				textArea.append("\n\nOr maybe there is a BusinessLogicServer already launched...\n");
				throw e;
			}
			
			textArea.append("Running service at:\n\t" + service);
			textArea.append("\n\n\nPress button to exit this server... ");
			
		  } catch (Exception e) {
			textArea.append(e.toString());
		  }

	  }
	}
}





