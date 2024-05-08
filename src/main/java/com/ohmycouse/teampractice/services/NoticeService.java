package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.NoticeEntity;
import com.ohmycouse.teampractice.mappers.NoticeMapper;
import com.ohmycouse.teampractice.models.PagingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    public static final int PAGE_COUNT = 10;
    private final NoticeMapper noticeMapper;


    @Autowired
    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    public NoticeEntity[] getAll() {
        return this.noticeMapper.selectAll();
    }

    public int getCount(String searchCriterion, String searchQuery) {
        return this.noticeMapper.selectCount(searchCriterion, searchQuery);
    }

    public NoticeEntity[] getByPage(PagingModel pagingModel, String searchCriterion, String searchQuery) {
        NoticeEntity[] noticeEntities = this.noticeMapper.selectByPage(pagingModel, searchCriterion, searchQuery);
        return noticeEntities;
    }

    public boolean saveNotice(NoticeEntity noticeEntity,
                              String title,
                              String text) {
        System.out.println("서비스 공지사항 타이틀 : " + title);
        System.out.println("서비스 공지사항 내용 : " + text);

        noticeEntity.setTitle(title)
                .setText(text);
        return this.noticeMapper.insertNotice(noticeEntity) > 0;
    }

    public boolean deleteNotice(int index){
        return this.noticeMapper.deleteNotice(index) > 0;
    }

    public boolean updateByNotice(int index, String text){
        return this.noticeMapper.updateByNotice(index, text) > 0;
    }
}
