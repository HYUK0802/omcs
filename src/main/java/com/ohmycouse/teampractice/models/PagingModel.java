package com.ohmycouse.teampractice.models;


public class PagingModel {
    public final int pageCount;
    public final int totalCount;
    public final int requestPage; //요청한 페이지번호
    public final int minPage; //조회 가능한 최소 페이지 번호
    public final int maxPage; //조회 가능한 최대 페이지 번호
    public final int displayStartPage; //표시할 시작페이지
    public final int displayEndPage;// 표시할 끝페이지
    public final int offset; //DB용 OFFSET

    public PagingModel(int pageCount, int totalCount, int requestPage){
        this.pageCount = pageCount;
        this.totalCount = totalCount;
        this.requestPage = Math.max(requestPage, 1);
        this.minPage = 1;
        this.maxPage = Math.max(totalCount / pageCount + (totalCount % pageCount == 0 ? 0 : 1), 1);
        this.displayStartPage = ((requestPage - 1) / 10) * 10 + 1;
        this.displayEndPage = Math.min(this.maxPage,((requestPage - 1) / 10) * 10 + 10);
        this.offset = (requestPage - 1) * this.pageCount;
    }
}

