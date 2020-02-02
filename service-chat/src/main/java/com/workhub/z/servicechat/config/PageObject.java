package com.workhub.z.servicechat.config;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 封装分页页面对象
 * 
 * @author hanxu
 */
public class PageObject {

	private int page; // 当前页码
	private int pageRow;// 每页数量
	private int total; // 总数
	private int totalPage; // 总页数
	private int priorPage; // 前一页
	private int nextPage; // 后一页
	private List<?> objectList; // 数据
	private int start; // 开始位置
	private int end; // 接收位置

	public int getPage() {
		if (StringUtils.isEmpty(page) || page < 1) {
			page = 1;
		}
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageRow() {
		if (StringUtils.isEmpty(pageRow) || pageRow < 1) {
			pageRow = 10;
		}
		return pageRow;
	}

	public void setPageRow(int pageRow) {
		this.pageRow = pageRow;
	}

	public int getPriorPage() {
		if (StringUtils.isEmpty(priorPage) || priorPage < 1) {
			priorPage = this.getPage() - 1;
			if (priorPage < 1) {
				priorPage = 1;
			}
		}
		return priorPage;
	}

	public void setPriorPage(int priorPage) {
		this.priorPage = priorPage;
	}

	public int getNextPage() {
		if (StringUtils.isEmpty(nextPage) || nextPage < 1) {
			nextPage = this.getPage() + 1;
			if (nextPage < 1) {
				nextPage = 1;
			}else if (nextPage > this.getTotalPage()){
				nextPage = this.getTotalPage();
			}
		}
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public List<?> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<?> objectList) {
		this.objectList = objectList;
	}

	public int getTotal() {
		if (StringUtils.isEmpty(total) || total < 1) {
			total = 0;
		}
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPage() {
		if (StringUtils.isEmpty(totalPage) || totalPage < 1) {
			if (this.getTotal() == 0) {
				totalPage = 1;
			} else {
				totalPage = this.getTotal() / this.getPageRow() + (this.getTotal() % this.getPageRow() > 0 ? 1 : 0);
			}
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStart() {
		if (StringUtils.isEmpty(start) || start < 1){
			start = (this.getPage() - 1) * this.getPageRow();
		}
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		if (StringUtils.isEmpty(end) || end < 1){
			end = this.getStart() + this.getPageRow();
		}
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
