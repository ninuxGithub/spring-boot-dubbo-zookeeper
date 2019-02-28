package com.example.provider.repository;

import com.example.provider.entity.Department;
import com.example.provider.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, Long> {

    public Department queryDepartmentById(Long id);
}
