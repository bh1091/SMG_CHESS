package org.vorasahil.hw3;

import org.vorasahil.hw6.client.*;

import org.vorasahil.hw7.client.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ChessEntryPoint implements EntryPoint {

	private LoginInfo loginInfo = null;

	public void onModuleLoad() {

		LoginServiceAsync loginService = GWT.create(LoginService.class);
		final RegisterServiceAsync registerService = GWT
				.create(RegisterService.class);

		loginService.login(Window.Location.getHref(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(final LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							registerService.registerPlayer(
									loginInfo.getEmailAddress(),true,
									new AsyncCallback<Register>() {

										@Override
										public void onSuccess(Register message) {
											// TODO Auto-generated method stub
											Graphics graphics = new Graphics(
													History.getToken(),
													message, result);
											RootPanel.get().add(graphics);
										}

										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Could not contact Server," +
													" please try again later");
										}
									});
						} else {
							loadLogin();
						}
					}
				});

	}

	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access this application.");
	private Anchor signInLink = new Anchor("Sign In");

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get().add(loginPanel);
	}
}
