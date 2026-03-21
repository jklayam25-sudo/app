package lumi.insert.app.controller.memo;

import java.util.List;
import java.util.UUID;
 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import lumi.insert.app.controller.BaseControllerTest;
import lumi.insert.app.core.entity.nondatabase.EmployeeLogin;
import lumi.insert.app.core.entity.nondatabase.EmployeeRole;
import lumi.insert.app.dto.response.MemoResponse; 
 
public abstract class BaseMemoControllerTest extends BaseControllerTest{
     
    MemoResponse memoResponse = new MemoResponse(1L, "A Title", "A Body", List.of(), EmployeeRole.FINANCE, false);

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("OWNER");

    EmployeeLogin employeeLogin = EmployeeLogin.builder()
        .id(UUID.randomUUID())
        .username("lumi")
        .role(EmployeeRole.OWNER)
        .build();

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(employeeLogin, null, authorities);

}
