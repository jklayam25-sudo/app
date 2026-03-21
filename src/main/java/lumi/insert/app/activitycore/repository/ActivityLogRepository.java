package lumi.insert.app.activitycore.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import lumi.insert.app.activitycore.entity.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID>{
    
}
