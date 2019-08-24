package community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO<T> {
    private List<T> data;//问题或通知
    private Integer page;//当前页
    private Boolean showFirstPage;//首页
    private Boolean showPrePage;//上一页
    private List<Integer> pages = new ArrayList<>();
    private Boolean showNextPage;//下一页
    private Boolean showEndPage;//尾页
    private Integer totalCount;//总问题数
    private Integer totalPage;//总页数

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        this.totalCount=totalCount;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //校验页面传过来的page的值
        if (page < 1) {
            page = 1;
        } else if (page > totalPage) {
            page = totalPage;
        }
        //设置本类page属性的值
        this.page = page;
        //添加页码
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i < totalPage + 1) {
                pages.add(page + i);
            }
        }

        //是否显示首页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        //是否显示上一页
        if (page != 1) {
            showPrePage = true;
        } else {
            showPrePage = false;
        }
        //showPrePage = (page == 1) ? false : true;
        //是否显示下一页
        if (page != totalPage) {
            showNextPage = true;
        } else {
            showNextPage = false;
        }

        //是否显示尾页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }

    }
}
