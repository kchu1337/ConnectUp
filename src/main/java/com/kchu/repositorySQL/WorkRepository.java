package com.kchu.repositorySQL;

import com.kchu.models.Work;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by student on 6/28/17.
 */
public interface WorkRepository extends CrudRepository<Work, Integer> {

    @Query(value = "SELECT user_id FROM work where company = :company", nativeQuery=true)
    public Iterable<Integer> findIdByCompany(@Param("company")String company);

    public Iterable<Work> findAllByUserId(int id);



}
