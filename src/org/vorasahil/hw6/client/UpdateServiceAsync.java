package org.vorasahil.hw6.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UpdateServiceAsync {

	void getUpdate(Update update, AsyncCallback<Boolean> callback);

}
