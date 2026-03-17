package lumi.insert.app;

import org.hibernate.envers.RevisionListener;

import lumi.insert.app.entity.RevisionAudit;
 
public class RevisionListenerImpl implements RevisionListener{
 
    private final AuditorAwareImpl auditorAwareImpl = new AuditorAwareImpl();

    @Override
    public void newRevision(Object entity) {
        if(entity instanceof RevisionAudit){
            RevisionAudit revisionAudit = (RevisionAudit) entity;
            revisionAudit.setUsername(auditorAwareImpl.getCurrentAuditor().orElse("SYSTEM"));
        }
    }
    
}
