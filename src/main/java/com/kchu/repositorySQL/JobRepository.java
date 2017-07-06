package com.kchu.repositorySQL;

import com.kchu.models.Job;
import com.kchu.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by student on 7/6/17.
 */
public interface JobRepository extends CrudRepository<Job, Integer> {

    public Iterable<Job> findAllByTitleAndCompany(String title, String company);

    public Iterable<Job> findAllByTitle(String title);

    public Iterable<Job> findAllByCompany(String company);

    public Job findFirstById(int id);
}
