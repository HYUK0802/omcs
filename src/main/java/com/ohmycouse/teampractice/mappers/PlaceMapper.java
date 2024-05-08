package com.ohmycouse.teampractice.mappers;

import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.models.PagingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlaceMapper {

//    PlaceEntity selectPlaceByAreaCode(int areaCode);
//    인터페이스에서 @Param값은 xml의 where #{} 값

    PlaceEntity selectPlaceByContentId(@Param(value = "contentId") int contentId);

    PlaceEntity[] selectPlaceByAreaCode(@Param(value = "areaCode") int areaCode);

    PlaceEntity[] selectPlaceByAreaCodePaged(@Param(value = "areaCode") int areaCode,
                                             @Param(value = "pagingModel")PagingModel pagingModel);

    int selectPlaceCountByAreaCode(@Param(value = "areaCode") int areaCode);
    int selectCount(@Param(value="searchCriterion") String searchCriterion,
                    @Param(value="searchQuery")String searchQuery);

    PlaceEntity[] selectDetailByContentId(@Param(value = "contentId") int contentId);

    PlaceEntity[] selectByPage(@Param(value = "pagingModel") PagingModel pagingModel,
                               @Param(value = "searchCriterion") String searchCriterion,
                               @Param(value = "searchQuery") String searchQuery);


    PlaceEntity selectPlaceByTitle(@Param("title") String title);
}
