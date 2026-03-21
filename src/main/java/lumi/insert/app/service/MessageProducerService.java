package lumi.insert.app.service;

import lumi.insert.app.activitycore.entity.ActivityLog;

public interface MessageProducerService {
    void sendActivityLog(ActivityLog activityLog);
}
