package com.anestec.hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.anestec.hello.model.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long>,
        JpaSpecificationExecutor<Announcement> {

}
