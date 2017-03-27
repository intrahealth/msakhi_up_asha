package com.asha.mnewborncareasha;

import android.app.Application;

public class Global extends Application {
	private String globaluserid;
	private String globalpassword;
	// Hero Add
	private int Child_Entry_flag;

	public String getGlobaluserid() {
		return globaluserid;
	}

	public void setGlobaluserid(String globaluserid) {
		this.globaluserid = globaluserid;
	}

	public String getGlobalpassword() {
		return globalpassword;
	}

	public void setGlobalpassword(String globalpassword) {
		this.globalpassword = globalpassword;
	}

	public int getChild_Entry_flag() {
		return Child_Entry_flag;
	}

	public void setChild_Entry_flag(int child_Entry_flag) {
		Child_Entry_flag = child_Entry_flag;
	}

}
