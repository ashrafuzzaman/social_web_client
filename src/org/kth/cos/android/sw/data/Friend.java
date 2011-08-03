package org.kth.cos.android.sw.data;

public class Friend {
	private String email;
	private String dataStore;
	private String sharedKey;

	public Friend() {
	}

	public Friend(String email, String dataStore, String sharedKey) {
		super();
		this.email = email;
		this.dataStore = dataStore;
		this.sharedKey = sharedKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDataStore() {
		return dataStore;
	}

	public void setDataStore(String dataStore) {
		this.dataStore = dataStore;
	}

	public String getSharedKey() {
		return sharedKey;
	}

	public void setSharedKey(String sharedKey) {
		this.sharedKey = sharedKey;
	}

}
