package org.leozis.hw10;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.SimplePanel;

public class MyDropController extends SimpleDropController{
	private final SimplePanel dropZone;
	
	public MyDropController(SimplePanel dropZone) {
		
		super(dropZone);
		
		this.dropZone = dropZone;
	}
	
	@Override
	public void onDrop(DragContext context) {
		dropZone.setWidget(context.draggable);
		
		super.onDrop(context);
	}
	
	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		if (dropZone.getWidget() != null) {
			throw new VetoDragException();
		}
		super.onPreviewDrop(context);
	}

}
