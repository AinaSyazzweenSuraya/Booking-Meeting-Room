package com.bookingmeetingroom.repository;

import com.bookingmeetingroom.entity.MeetingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoomEntity, Long> {
    List<MeetingRoomEntity> findAll();

}

