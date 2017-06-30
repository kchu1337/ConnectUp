package com.kchu.repositorySQL;

import com.kchu.models.Education;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by student on 6/28/17.
 */
public interface EduRepository extends CrudRepository<Education, Integer> {

    @Query(value = "SELECT user_id FROM education where college = :college", nativeQuery=true)
    public Iterable<Integer> findIdByCollege(@Param("college")String college);


    public Iterable<Education> findAllByUserId(int id);
}
