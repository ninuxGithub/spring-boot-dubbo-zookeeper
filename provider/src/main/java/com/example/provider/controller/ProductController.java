package com.example.provider.controller;

import com.example.provider.entity.Product;
import com.example.provider.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@RestController
@RequestMapping(value = "/es")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/saveProduct")
    public String saveProduct() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        productRepository.save(new Product(1L,"黑人牙膏",2.45f, dateFormat.parse("2018-12-18"), 99 ));
        productRepository.save(new Product(2L,"酸奶",19.85f, dateFormat.parse("2018-11-18"), 89 ));
        productRepository.save(new Product(3L,"百事可乐",3.40f, dateFormat.parse("2018-10-18"), 120 ));
        productRepository.save(new Product(4L,"旺仔小馒头",12.00f, dateFormat.parse("2018-07-28"), 39 ));
        return "success";
    }
}
