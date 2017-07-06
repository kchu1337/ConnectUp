package com.kchu.repositorySQL;

import com.kchu.models.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by student on 7/6/17.
 */
public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    public Iterable<Notification> findAllByUserId(int id);
}
