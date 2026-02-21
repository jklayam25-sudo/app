package lumi.insert.app.controller;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.AuthTokenCreateRequest;
import lumi.insert.app.dto.response.AuthTokenResponse;
import lumi.insert.app.service.AuthTokenService;

@RestController
public class AuthTokenController {
    
    @Autowired
    AuthTokenService authTokenService;

    @PostMapping(
        path = "/api/auth/login",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<WebResponse<AuthTokenResponse>> loginAuthAPI(@Valid AuthTokenCreateRequest request){
        AuthTokenResponse resultFromService = authTokenService.createAuthToken(request);

        WebResponse<AuthTokenResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", resultFromService.refreshToken())
        .maxAge(604800)
        .path("/")
        .build();

        return ResponseEntity.ok().header(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString()).body(wrappedResult);
    }

    @PostMapping(
        path = "/api/auth/refresh",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<AuthTokenResponse>> refreshAuthAPI(@CookieValue(name = "refreshToken", required = true) String refreshToken){
        AuthTokenResponse resultFromService = authTokenService.refreshAuthToken(refreshToken);

        WebResponse<AuthTokenResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null); 

        // ResponseCookie cookie = ResponseCookie.from("refreshToken", resultFromService.refreshToken())
        // .maxAge(604800)
        // .path("/")
        // .build();
        
        return ResponseEntity.ok(wrappedResult);
    }

    @DeleteMapping(
        path = "/api/auth/logout"
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteAuthAPI(@CookieValue(name = "refreshToken", required = true) String refreshToken, HttpServletResponse response){
        authTokenService.deleteRefreshToken(refreshToken);
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addHeader("X-ACCESS-TOKEN", null);
        response.addCookie(cookie);
    }
}
