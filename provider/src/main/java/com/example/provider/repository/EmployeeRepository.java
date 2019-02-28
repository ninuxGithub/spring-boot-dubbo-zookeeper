package com.example.provider.repository;

import com.example.provider.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@Repository
public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {

    /**
     * @author shenzm
     * @param id
     * @return
     */
    public Employee queryEmployeeById(String id);
}
