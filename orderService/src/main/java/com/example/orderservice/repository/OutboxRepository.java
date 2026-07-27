package com.example.orderservice.repository;

import com.example.orderservice.entity.Outbox;
import com.example.orderservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    List<Outbox> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);

    List<Outbox> findTop100ByStatusAndTryCntLessThanEqualOrderByCreatedAtAsc(OutboxStatus status,int retryCnt);

    @Modifying
    @Query("""
        update Outbox o
        set o.status = :status,
            o.tryCnt = o.tryCnt + 1
        where o.eventId = :eventId
    """)
    int updateStatusAndTryCntByEventId(@Param("eventId") UUID eventId, @Param("status") OutboxStatus status);

    @Modifying
    @Query("""
        update Outbox o
        set o.status = :status,
            o.tryCnt = o.tryCnt + 1,
            o.errorMsg = :errorMsg
        where o.eventId = :eventId
    """)
    int updateStatusAndTryCntAndErrorMsgByEventId(
            @Param("eventId") UUID eventId,
            @Param("status") OutboxStatus status,
            @Param("errorMsg") String errorMsg
    );
}
