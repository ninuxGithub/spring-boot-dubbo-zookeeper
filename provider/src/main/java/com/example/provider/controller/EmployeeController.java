package com.example.provider.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.provider.entity.Employee;
import com.example.provider.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@RestController
@RequestMapping(value = "/es")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping("/removeAll")
    public String remove() {
        employeeRepository.deleteAll();
        return "success";
    }

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping("/add")
    public String add(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAge(26);
        employee.setAddress("上海 浦东新区");
        employeeRepository.save(employee);
        return "success";
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    public String delete() {
        Employee employee = employeeRepository.queryEmployeeById("1");
        employeeRepository.delete(employee);
        return "success";
    }

    /**
     * 局部更新
     *
     * @return
     */
    @RequestMapping("/update")
    public String update() {
        Employee employee = employeeRepository.queryEmployeeById("1");
        employee.setFirstName("哈哈");
        employeeRepository.save(employee);
        return "success";
    }

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping("/query")
    public Employee query(@RequestParam("id") String id) {
        Employee employee = employeeRepository.queryEmployeeById(id);
        String json = JSONObject.toJSONString(employee);
        logger.info(json);
        return employee;
    }

}
