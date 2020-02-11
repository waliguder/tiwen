package com.example.demo.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//页面数据对象封装
@Data
public class PageDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious = true;
    private boolean showFirstPage = true;
    private boolean showNext = true;
    private boolean showLastPage = true;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        //计算页码数
        double num = Math.ceil(totalCount * 1.0 /size);
        Integer pageNum =(int) num;
        this.totalPage = pageNum;
        //page 容错
        if(page<1){
            page=1;
        }else if(page > pageNum){
            page = pageNum;
        }
        //将页面的page参数赋值给对象的page
        this.page = page;

        pages.add(page);
        for(int i=1;i<=3;i++){
            if(page-i >0){
                pages.add(page-i);
            }
            if(page+i<=pageNum){
                pages.add(page+i);
            }
        }
        Collections.sort(pages);
        //判断前一页按钮的出现情况
        if(page == 1){
            showPrevious = false;
            showLastPage = true;
        }else{
            showPrevious = true;
        }
        if (page == pageNum){
            showNext = false;
            showFirstPage = true;
        }else{
            showNext = true;
        }
        //判断第一页和最后一页按钮出现
        if(pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
        if(pages.contains(pageNum)){
            showLastPage = false;
        }else{
            showLastPage = true;
        }
    }
}
