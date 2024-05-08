package com.ohmycouse.teampractice.mappers;

import com.ohmycouse.teampractice.entities.NoticeEntity;
import com.ohmycouse.teampractice.models.PagingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoticeMapper {
    NoticeEntity[] selectAll();
    int insert(NoticeEntity noticeEntity);

    int selectCount(@Param(value = "searchCriterion")String searchCriterion,
                    @Param(value = "searchQuery")String searchQuery);

    NoticeEntity[] selectByPage(@Param(value = "pagingModel")PagingModel pagingModel,
                                @Param(value = "searchCriterion")String searchCriterion,
                                @Param(value = "searchQuery")String searchQuery);

    int insertNotice(NoticeEntity noticeEntity);

    int deleteNotice(@Param(value = "index") int index);

    int updateByNotice(@Param(value = "index") int index,
                       @Param(value = "text") String text);
}
