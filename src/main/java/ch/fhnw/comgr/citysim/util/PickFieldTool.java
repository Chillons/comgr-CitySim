package ch.fhnw.comgr.citysim.util;

import ch.fhnw.comgr.citysim.CityController;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;


public class PickFieldTool extends AbstractTool {

		public PickFieldTool(CityController controller) {
			super(controller);
		}

		@Override
		public void pointerPressed(IPointerEvent e) {
			int x = e.getX();
			int y = e.getY();	
			System.out.println("Clicking X: " + x + " und Y: " + y);
		}
	}


