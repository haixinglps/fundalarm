package cn.exrick.manager.isearch.util;


/**
 * 分页 Bean
 * 
 * @author Administrator
 * 
 */
public class PageBean {

	private int totalItems; // 总数
	private int totalPage; // 总页数
	private int currentPage; // 当前页
	private int pageSize; // 每页显示数目
	private int startPageIndex; // 首页
	private int endPageIndex; // 尾页

	private boolean isFirstPage; // 是否首页
	private boolean isLastPage; // 是否尾页
	private boolean hasPrePage; // 是否有上一页
	private boolean hasNextPage; // 是否有下一页

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void init() {
		this.isFirstPage = isFirstPage();
		this.isLastPage = isLastPage();
		this.hasPrePage = isHasPrePage();
		this.hasNextPage = isHasNextPage();
	}

	private boolean isFirstPage() {
		isFirstPage = currentPage == 1;
		return isFirstPage;
	}

	private boolean isLastPage() {
		isLastPage = currentPage == totalPage;
		return isLastPage;
	}

	private boolean isHasPrePage() {
		hasPrePage = currentPage != 1;
		return hasPrePage;
	}

	private boolean isHasNextPage() {
		hasNextPage = currentPage != totalPage;
		return hasNextPage;
	}

	public static int countCurrentPage(int page) {
		final int curPage = (page == 0) ? 1 : page;
		return curPage;
	}

	public static int countTotalPage(int pageSize, final int allRow) {
		pageSize = (pageSize == 0) ? 20 : pageSize;
		int totalPage = (allRow % pageSize == 0) ? (allRow / pageSize)
				: (allRow / pageSize + 1);
		if (totalPage == 0)
			totalPage = 1;
		return totalPage;
	}

	public static int countOffset(final int pageSize, final int currentPage) {
		final int offset = pageSize * (currentPage - 1);
		return offset;
	}

	public int getStartPageIndex() {
		return startPageIndex;
	}

	public void setStartPageIndex(int startPageIndex) {
		this.startPageIndex = startPageIndex;
	}

	public int getEndPageIndex() {
		return endPageIndex;
	}

	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}

}
