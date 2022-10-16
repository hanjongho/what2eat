package com.ddd.what2eat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddd.what2eat.model.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
