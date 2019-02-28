package com.example.provider.repository;

import com.example.provider.entity.Department;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@Repository
public interface DepartmentRepository extends ElasticsearchRepository<Department, String> {

    public Department queryDepartmentById(String id);
}
