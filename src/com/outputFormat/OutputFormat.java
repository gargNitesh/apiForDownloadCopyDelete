package com.outputFormat;

import java.util.List;

public class OutputFormat {
	boolean status;
	List error;
	List data;

	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public List getData() {
		return data;
	}

	public List getError() {
		return error;
	}
	
	public void setError(List error) {
		this.error = error;
	}
	
	public void setData(List data) {
		this.data = data;
	}
	
}
