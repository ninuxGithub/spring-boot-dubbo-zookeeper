package com.example.api.mode.builder;

/**
 * 使用多个简单的对象一步一步的构造成一个复杂的对象
 *
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class BuilderPatternDemo {
    public static void main(String[] args) {
        MealBuilder mealBuilder = new MealBuilder();
        Meal meal = mealBuilder.prepareNonVegMeal();
        meal.showItems();


        Meal vegMeal = mealBuilder.prepareVegMeal();
        vegMeal.showItems();
    }
}
