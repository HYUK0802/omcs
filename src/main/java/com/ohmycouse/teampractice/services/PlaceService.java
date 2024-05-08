package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.mappers.PlaceMapper;
import com.ohmycouse.teampractice.models.PagingModel;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    public static final int PAGE_COUNT = 12;
    private final PlaceMapper placeMapper;

    @Autowired
    public PlaceService(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    public PlaceEntity getPlace(int contentId) {
        return this.placeMapper.selectPlaceByContentId(contentId);
    }

    public PlaceEntity[] getPlaces(int areaCode){
        return this.placeMapper.selectPlaceByAreaCode(areaCode);
    }



    public Pair<PlaceEntity[], PagingModel> getPlacesPaged(int areaCode, int page){
        // 요청한 페이지 번호 : page
        // 한 페이지에 표시할 게시글 개수 : 12
        // 전체 게시글의 개수(지역번호가 같은거만) : totalCount
        final int totalCount = this.placeMapper.selectPlaceCountByAreaCode(areaCode);
        final PagingModel pagingModel = new PagingModel(12, totalCount, page);
        PlaceEntity[] places = this.placeMapper.selectPlaceByAreaCodePaged(areaCode, pagingModel);
        return new ImmutablePair<>(places, pagingModel);
    }

    public PlaceEntity[] getDetail(int contentId) {
        return this.placeMapper.selectDetailByContentId(contentId);
    }


//    public PlaceEntity getImageByTitle(String title) {
//        System.out.println("상세일정 타이틀  : " + title);
//        // place에서 firstImage를 가져와서 byte 배열로 변환하는 로직 추가
//        // 예시로 다음과 같이 구현할 수 있습니다. (실제로는 데이터베이스에 따라 다를 수 있음)
//        // byte[] imageBytes = someMethodToGetImageBytes(place.getFirstImage());
//        return this.placeMapper.selectPlaceByTitle(title);
//    }
    public String getImageUrlByTitle(String title) {
        PlaceEntity place = this.placeMapper.selectPlaceByTitle(title);
        if (place != null) {
            return place.getFirstImage();
        }
        return null; // 해당 title에 해당하는 이미지가 없을 경우 null을 반환
    }
}
