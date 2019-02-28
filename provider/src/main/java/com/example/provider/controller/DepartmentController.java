package com.example.provider.controller;

import com.example.provider.entity.Department;
import com.example.provider.entity.Employee;
import com.example.provider.repository.DepartmentRepository;
import com.example.provider.repository.EmployeeRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@RestController
@RequestMapping(value = "/es")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping("/addDepart")
    public String add() {
        Department department = new Department();
        department.setDepartName("研发部");
        department.setId(UUID.randomUUID().toString());
        Set<Employee> employees = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee();
            employee.setId(UUID.randomUUID().toString());
            employee.setFirstName("fn" + i);
            employee.setLastName("ln" + i);
            employee.setAge(26);
            employee.setAddress("上海 浦东新区");
            //employeeRepository.save(employee);
            employees.add(employee);
        }
        department.setEmployees(employees);
        departmentRepository.save(department);
        return "success";
    }


    @RequestMapping("/queryDepart/{page}/{size}/{q}")
    public List<Department> query(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String q) {
//        // 分页参数
//        Pageable pageable = PageRequest.of(page, size);
//
//        // 分数，并自动按分排序
//        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
//                .add(QueryBuilders.boolQuery()
//                        .should(QueryBuilders.matchQuery("departName", q)), ScoreFunctionBuilders.weightFactorFunction(1000)) // 权重：name 1000分
//                .add(QueryBuilders.boolQuery()
//                        .should(QueryBuilders.matchQuery("employees.firstName", q)),ScoreFunctionBuilders.weightFactorFunction(100)); // 权重：message 100分
//
//        // 分数、分页
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
//                .withQuery(functionScoreQueryBuilder).build();
//
//        Page<Department> searchPageResults = departmentRepository.search(searchQuery);
//        logger.info("\n searchCity(): searchContent [" + q + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//        return searchPageResults.getContent();
        return null;
    }


    @RequestMapping("/test")
    public List<Department> test(){
        Pageable pageable = PageRequest.of(1, 10);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withQuery(null).build();
        Page<Department> search = departmentRepository.search(searchQuery);
        return search.getContent();
    }


    private class FilterFunctionBuilder {
    }
}
