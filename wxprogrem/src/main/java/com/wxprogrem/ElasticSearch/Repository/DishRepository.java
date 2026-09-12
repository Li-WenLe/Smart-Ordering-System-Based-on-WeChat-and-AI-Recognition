package com.wxprogrem.ElasticSearch.Repository;

import com.wxprogrem.ElasticSearch.ElasticSearchIndex.DishIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DishRepository extends ElasticsearchRepository<DishIndex, String> {
    //根据商品名查询商品
    List<DishIndex> findByName(String name);
}
