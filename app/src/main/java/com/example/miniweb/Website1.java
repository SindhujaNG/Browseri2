package com.example.miniweb;

public class Website1 {

	// private variables
	public int _id;
	public String _title;
	public String _url;
	public long _date;

	// Empty constructor
	public Website1() {

	}

	// constructor
	public Website1(String title, String url) {
		this._title = title;
		this._url = url;


	}

	// constructor
	public Website1(String title, String url, long date) {
		this._title = title;
		this._url = url;
		this._date = date;

	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int keyId) {
		this._id = keyId;
	}

	// getting name
	public String getTitle() {
		return this._title;
	}

	// setting name
	public void setTitle(String name) {
		this._title =_title;
	}

	// getting url
	public String getUrl() {
		return this._url;
	}

	// setting url
	public void setUrl(String url) {
		this._url = url;
	}

	// getting phone number

	public long getDate() {
		return _date;
	}
	public void setDate(long date) {
		this._date = date;
	}

}
