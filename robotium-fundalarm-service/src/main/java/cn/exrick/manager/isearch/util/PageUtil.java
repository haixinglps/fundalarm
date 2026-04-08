package cn.exrick.manager.isearch.util;

public class PageUtil {

	/**
	 * 分页查询
	 * 
	 * @param totalItems
	 *            总数
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            每页显示长度
	 */
	public static PageBean query(int totalItems, int page, int pageSize) {
		return query(totalItems, page, pageSize, 10);
	}

	/**
	 * 分页查询
	 * 
	 * @param totalItems
	 *            总数
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            每页显示长度
	 * @param viewPageCount
	 *            浏览页数
	 */
	public static PageBean query(int totalItems, int page, int pageSize,
			int viewPageCount) {
		int totalPage = PageBean.countTotalPage(pageSize, totalItems);
		final int currentPage = PageBean.countCurrentPage(page);
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(currentPage);
		pageBean.setTotalItems(totalItems);
		pageBean.setTotalPage(totalPage);
		int startPageIndex = 1;
		int endPageIndex = totalPage;
		if (totalPage > viewPageCount) {
			if (viewPageCount % 2 == 0) {
				startPageIndex = currentPage - viewPageCount / 2 + 1;
				endPageIndex = currentPage + viewPageCount / 2;
			} else {
				startPageIndex = currentPage - viewPageCount / 2;
				endPageIndex = currentPage + viewPageCount / 2;
			}
			if (startPageIndex < 1) {
				startPageIndex = 1;
				endPageIndex = viewPageCount;
			}
			if (endPageIndex > totalPage) {
				endPageIndex = totalPage;
				startPageIndex = totalPage - viewPageCount + 1;
				if (startPageIndex < 1) {
					startPageIndex = 1;
				}
			}
		}
		pageBean.setStartPageIndex(startPageIndex);
		pageBean.setEndPageIndex(endPageIndex);
		return pageBean;
	}

}
