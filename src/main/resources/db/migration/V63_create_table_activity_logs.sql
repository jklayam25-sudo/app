CREATE TABLE activity_logs (

    id UUID NOT NULL,
    entity_name VARCHAR(55) NOT NULL,
    entity_id VARCHAR(255) NOT NULL,
    action activity_action NOT NULL,
    action_message TEXT NOT NULL,
    ip_address VARCHAR(55) NOT NULL,

);