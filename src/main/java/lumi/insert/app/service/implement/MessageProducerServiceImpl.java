package lumi.insert.app.service.implement;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.activitycore.entity.ActivityLog;
import lumi.insert.app.service.MessageProducerService;

@Service
@Slf4j
public class MessageProducerServiceImpl implements MessageProducerService{

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void sendActivityLog(ActivityLog activityLog) { 
        rabbitTemplate.convertAndSend("main-exchange", "activity-routing", activityLog);
    }
    
}
