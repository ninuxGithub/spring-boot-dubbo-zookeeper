package com.example.provider.repository;

import com.example.provider.entity.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@Repository
public interface PersonRepository extends ElasticsearchRepository<Person, Long> {

    public Person queryById(Long id);
}
