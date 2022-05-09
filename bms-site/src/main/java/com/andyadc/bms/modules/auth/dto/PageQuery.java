package com.andyadc.bms.modules.auth.dto;

public class PageQuery {

	private Integer page;
	private Integer limit;

	private int start;
	private int end;

	public void page() {
		if (page == null || limit == null) {
			return;
		}
		if (page < 1 || limit < 1) {
			return;
		}
		this.start = (this.page - 1) * this.limit;
		this.end = this.page * this.limit;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
