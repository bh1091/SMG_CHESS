package org.jiangfengchen.hw3;

import org.shared.chess.State;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyLoadPanel extends PopupPanel{
	public MyLoadPanel(final Storage stockstore,final Presenter p,final int c){
		VerticalPanel vp=new VerticalPanel();
	    vp.setSpacing(8);
	    String msg="";
	    if(c==0) msg ="Do you want to Save the Game?";
	    else if (c==1) msg ="Load last Game?";
	    else if (c==2) msg = "Clear the saved Game?";
        Label l = new Label(msg);
        HorizontalPanel buttons = new HorizontalPanel();
        Button ok =new Button("Ok");
        Button cancel = new Button("Cancel");
        buttons.add(ok);
        buttons.add(cancel);
        ok.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(c==0){
					if (stockstore != null) {
						String k = Presenter.Seralize(p.getState());
						 int numStocks = stockstore.getLength();
						if(numStocks==0) stockstore.setItem("Stock."+numStocks, k);
						else{
							stockstore.clear();
							stockstore.setItem("Stock."+numStocks, k);
						}
				    }
					MyLoadPanel.this.hide();
				}else if(c==1){
					if (stockstore != null) {
						String key = stockstore.key(0);
						String k = stockstore.getItem(key);
						State st = Presenter.Deserialize(k);
						p.setState(st);
						MyLoadPanel.this.hide();
				
				    }else MyLoadPanel.this.hide();

				}else {
					stockstore.clear();
					MyLoadPanel.this.hide();
				}
				
			}});
        cancel.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				MyLoadPanel.this.hide();
				
			}
        	
        });
        vp.add(l);
        vp.add(buttons);
        this.setGlassEnabled(true);
	    this.setAnimationEnabled(true);
        this.setWidget(vp);
	}

}
