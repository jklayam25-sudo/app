package lumi.insert.app.worker;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lumi.insert.app.activitycore.entity.ActivityLog;
import lumi.insert.app.activitycore.repository.ActivityLogRepository;

@ExtendWith(MockitoExtension.class)
public class MessageConsumerTest {
    
    @Mock
    private ActivityLogRepository repository;

    @InjectMocks
    private MessageConsumer consumer;

    @Test
    void activitLogsHandler_shouldSave(){
        ActivityLog activityLog = ActivityLog.builder()
        .actionMessage("a message")
        .build();

        consumer.activityLogsHandler(activityLog);

        verify(repository, times(1)).save(argThat(arg -> arg.getActionMessage().equals(activityLog.getActionMessage())));
    }
}
