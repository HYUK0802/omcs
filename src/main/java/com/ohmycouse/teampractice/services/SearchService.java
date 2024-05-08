package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.mappers.PlaceMapper;
import com.ohmycouse.teampractice.models.PagingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final PlaceMapper placeMapper;
    public static final int PAGE_COUNT = 5;


    @Autowired
    public SearchService(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    public int getCount(String searchCriterion, String searchQuery){
        return this.placeMapper.selectCount(searchCriterion, searchQuery);
    }

    public PlaceEntity[] getByPage(PagingModel pagingModel, String searchCriterion,
                                   String searchQuery){
        PlaceEntity[] placeEntities = this.placeMapper.selectByPage(pagingModel,
                searchCriterion, searchQuery); //매퍼를 통해 페이지별로 조회하고 entity배열로 반환
        return placeEntities;
    }

}
