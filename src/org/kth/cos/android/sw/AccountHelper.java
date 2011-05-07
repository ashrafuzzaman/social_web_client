package org.kth.cos.android.sw;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

public class AccountHelper {
	public String getDefaultEmailName(Activity activity) {
		AccountManager accountMng = AccountManager.get(activity);
		Account[] accounts = accountMng.getAccountsByType(null);
		if (accounts.length > 0) {
			return accounts[0].name;
		}
		return "";
	}
}
