package com.kchu.repositorySQL;

import com.kchu.models.Skill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by student on 6/28/17.
 */
public interface SkillRepository extends CrudRepository<Skill, Integer> {

    @Query(value = "SELECT user_id FROM skill where name = :name", nativeQuery=true)
    public Iterable<Integer> findIdByName(@Param("name")String name);




    public Iterable<Skill> findAllByUserId(int id);
}
