package lumi.insert.app.worker;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.activitycore.entity.ActivityLog;
import lumi.insert.app.activitycore.repository.ActivityLogRepository;

@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    ActivityLogRepository activityLogRepository;
    
    @RabbitListener(queues = "activity-logs")
    void activityLogsHandler(ActivityLog activityLog){ 
        activityLogRepository.save(activityLog);
    }
}
