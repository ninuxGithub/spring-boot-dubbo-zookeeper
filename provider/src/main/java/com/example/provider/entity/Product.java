package com.example.provider.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@Document(indexName = "products", type = "product", shards = 1, replicas = 0, refreshInterval = "-1")
public class Product implements Serializable {

    @Id
    private Long id;

    @Field
    private String productName;

    @Field
    private float price;

    @Field
    private Date date;

    @Field
    private int amount;

    public Product() {
    }

    public Product(Long id, String productName, float price, Date date, int amount) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.date = date;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
