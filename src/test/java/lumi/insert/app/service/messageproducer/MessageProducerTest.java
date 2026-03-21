package lumi.insert.app.service.messageproducer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString; 
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import lumi.insert.app.activitycore.entity.ActivityLog; 
import lumi.insert.app.service.implement.MessageProducerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MessageProducerTest {
    
    @InjectMocks
    MessageProducerServiceImpl messageProducerService;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Test
    void sendActivityLog_shouldTriggerConvertAndSend(){
        ActivityLog activityLog = ActivityLog.builder()
        .actionMessage("a message")
        .build();

        messageProducerService.sendActivityLog(activityLog); 
        ArgumentCaptor<ActivityLog> argumentCaptor = ArgumentCaptor.forClass(ActivityLog.class);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), argumentCaptor.capture());
        ActivityLog value = argumentCaptor.getValue();
        assertEquals(activityLog.getActionMessage(), value.getActionMessage());
    }
}
