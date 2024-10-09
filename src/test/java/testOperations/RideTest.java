package testOperations;

import org.mockito.Mockito;

import business_logic.BLFacade;
import gui.MainGUI;

public class RideTest {

	static BLFacade appFacadeInterface = Mockito.mock(BLFacade.class);
	
	public static void main(String[] args) {
		MainGUI a = new MainGUI();
		MainGUI.setBussinessLogic(appFacadeInterface);
		a.setVisible(true);
		
		Mockito.doReturn(true).when(appFacadeInterface).isRegistered("a","a");
		Mockito.doReturn("Driver").when(appFacadeInterface).getMotaByUsername(Mockito.anyString());
		
		
	}
}
