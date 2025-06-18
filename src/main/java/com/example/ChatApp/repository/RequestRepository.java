package com.example.ChatApp.repository;

import com.example.ChatApp.model.Request;
import com.example.ChatApp.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsBySenderIdAndReceiverId(Integer senderId, Integer receiverId);

    @Query("SELECT COUNT(r) > 0 FROM Request r WHERE r.sender.id = :senderId AND r.receiver.id = :receiverId AND r.status = 'PENDING'")
    boolean pendingRequestExists(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    @Query("SELECT r FROM Request r WHERE r.sender.id = :senderId AND r.receiver.id = :receiverId AND r.status = :status")
    Optional<Request> findRequestBySenderIdAndReceiverIdAndStatus(
            @Param("senderId") Integer senderId,
            @Param("receiverId") Integer receiverId,
            @Param("status") RequestStatus status);

    @Query("SELECT r FROM Request r WHERE r.receiver.id = :receiverId AND r.status = 'PENDING'")
    List<Request> findPendingRequestsByReceiverId(@Param("receiverId") Integer receiverId);

}
