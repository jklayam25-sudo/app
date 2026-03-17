package lumi.insert.app.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware; 
import org.springframework.security.core.context.SecurityContextHolder;

import lumi.insert.app.core.entity.nondatabase.EmployeeLogin;

public class AuditorAwareImpl implements AuditorAware<String>{

    @Override
    public Optional<String> getCurrentAuditor() {
       return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(auth -> {
            if (auth.getPrincipal() instanceof EmployeeLogin){
                return ((EmployeeLogin) auth.getPrincipal()).getUsername();
            }
            return auth.getPrincipal().toString();
        });
        
    }
    
}
