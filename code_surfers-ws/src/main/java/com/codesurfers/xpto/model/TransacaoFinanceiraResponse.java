package com.codesurfers.xpto.model;

import java.util.Collection;
import java.util.List;

public class TransacaoFinanceiraResponse {
	private String draw;
	private Long recordsTotal;
	private Integer recordsFiltered;
	private Collection<TransacaoFinanceira> data;
	
	
	public TransacaoFinanceiraResponse(String draw, Long recordsTotal, int recordsFiltered,
			Collection<TransacaoFinanceira> data) {
		super();
		this.draw = draw;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
		this.data = data;
	}
	
	
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public Collection<TransacaoFinanceira> getData() {
		return data;
	}
	public void setData(Collection<TransacaoFinanceira> data) {
		this.data = data;
	}
	
	
}
