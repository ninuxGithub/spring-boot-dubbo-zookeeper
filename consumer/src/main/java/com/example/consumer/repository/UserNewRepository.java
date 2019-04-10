package com.example.consumer.repository;

import com.example.consumer.bean.UserNew;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserNewRepository extends JpaRepository<UserNew, Long> {

    /**
     * 根据名称获取用户
     *
     * @param name
     * @return
     */
    public UserNew findUserNewByName(String name);

    @Cacheable(key = "#id + 'findById'")
    public UserNew findUserNewById(Long id);


    @Modifying
    @Query(value = "update UserNew  u set u.name=:name, u.age=:age where u.id=:id")
//    @CachePut(key = "#id+'findById'")
    @CacheEvict(key = "#id + 'findById'")
    public void updateUser(@Param("age") int age, @Param("name") String name, @Param("id") Long id);

    @Override
    @CacheEvict(allEntries = true)
    public void deleteAllInBatch();

}
