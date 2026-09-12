package com.wxprogrem.ElasticSearch.ElasticSearchIndex;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "dish")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishIndex {
    @Id
    private String id;
    @Field(type = FieldType.Text,analyzer = "standard")
    private String name;
    @Field(type = FieldType.Double)
    private Double price;
    @Field(type = FieldType.Text,analyzer = "standard")
    private String description;
    @Field(type = FieldType.Text,analyzer = "standard")
    private String image;
    @Field(type=FieldType.Integer)
    private Integer inventory;
}
