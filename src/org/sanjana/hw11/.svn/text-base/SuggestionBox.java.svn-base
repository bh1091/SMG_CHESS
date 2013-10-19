package org.sanjana.hw11;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SuggestionBox {

	MultiWordSuggestOracle oracle;
	SuggestBox suggestbox = new SuggestBox();

	public SuggestionBox(){
		this.oracle = new MultiWordSuggestOracle();
	}

	public void demo()
	{
		VerticalPanel panel = new VerticalPanel();
		panel.add(new Label("Enter Opponent Email"));
		panel.add(suggestbox);
		panel.addStyleName("demo-panel-padded");
		RootPanel.get().add(panel);
		add("coolbarbie2004@gmail.com");
		add("jassianand.786@gmail.com");
	}

	public void add(String str){
		oracle.add(str);
	}

	MultiWordSuggestOracle getAll(){
		return oracle;
	}
}
