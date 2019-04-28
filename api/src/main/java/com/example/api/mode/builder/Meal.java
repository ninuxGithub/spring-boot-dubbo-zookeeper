package com.example.api.mode.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Meal {

    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public float getCost() {
        float cost = 0f;
        for (Item item : items) {
            cost += item.price();
        }
        return cost;
    }

    public void showItems(){
        StringBuffer sb = new StringBuffer();
        for (Item item : items){
            sb.setLength(0);
            sb.append("name :").append(item.name()).append( ", packing :").append(item.packing().pack()).append(",price:").append(item.price());
            System.out.println(sb.toString());
        }
    }


}
