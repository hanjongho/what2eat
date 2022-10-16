package com.ddd.what2eat.repository;

import com.ddd.what2eat.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	Optional<Attendance> findByMeetingIdAndUserId(Long id, Long userId);


	@Query("select at from Attendance at where at.user.id=:userId ")
	List<Attendance> findAllByUserId(Long userId);
}
