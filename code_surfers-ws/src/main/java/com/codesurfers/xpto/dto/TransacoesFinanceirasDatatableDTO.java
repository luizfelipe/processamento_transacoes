package com.codesurfers.xpto.dto;

import java.util.ArrayList;
import java.util.List;

public class TransacoesFinanceirasDatatableDTO {
	private int draw = 1;
	private int recordsTotal;
	private int recordsFiltered;
	
	private List<List<String>> data = new ArrayList<List<String>>();

	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	
	public List<List<String>> getData() {
		return data;
	}
	public void setData(List<List<String>> data) {
		this.data = data;
	}
}
