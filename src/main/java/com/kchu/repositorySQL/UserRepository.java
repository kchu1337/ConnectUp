package com.kchu.repositorySQL;

import com.kchu.models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by student on 6/28/17.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query(value = "SELECT id FROM user where name = :name", nativeQuery=true)
    public Iterable<Integer> findIdByName(@Param("name")String name);

    @Query(value = "SELECT id FROM user where email = :email", nativeQuery=true)
    public Integer findIdByLogin(@Param("email")String email);

    @Query(value = "SELECT id FROM user", nativeQuery=true)
    public Iterable<Integer> findId();


    public User findFirstById(int id);

}
