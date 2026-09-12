package com.wxprogrem.ElasticSearch.EsDataInit;

import com.wxprogrem.ElasticSearch.ElasticSearchIndex.DishIndex;
import com.wxprogrem.ElasticSearch.Repository.DishRepository;
import com.wxprogrem.mapper.DishMapper;
import com.wxprogrem.pojo.Dish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EsDataInitRunner implements CommandLineRunner {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishRepository dishRepository;

    @Override
    public void run(String... args) {
        List<Dish> dishList = dishMapper.getAllDish();
        if (dishList.isEmpty()) {
            log.warn("MySQL 中没有菜品数据，跳过同步");
            return;
        }

        List<DishIndex> indexList = dishList.stream().map(dish -> {
            DishIndex index = new DishIndex();
            index.setId(String.valueOf(dish.getId()));
            index.setName(dish.getName());
            index.setPrice(dish.getPrice());
            index.setDescription(dish.getDescription());
            index.setImage(dish.getImage());
            index.setInventory(dish.getInventory());
            return index;
        }).collect(Collectors.toList());

        dishRepository.saveAll(indexList);
        log.info("启动时自动同步菜品到 ES，共 {} 条", indexList.size());
    }
}
