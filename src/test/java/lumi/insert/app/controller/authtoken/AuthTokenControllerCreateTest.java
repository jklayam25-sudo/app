package lumi.insert.app.controller.authtoken;

import static org.mockito.ArgumentMatchers.any; 
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.servlet.http.Cookie;
import lumi.insert.app.dto.request.AuthTokenCreateRequest;
import lumi.insert.app.exception.AuthenticationTokenException;
import lumi.insert.app.exception.NotFoundEntityException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

public class AuthTokenControllerCreateTest extends BaseAuthTokenControllerTest{
   
    @Test
    @DisplayName("should return Auth DTO when credentials valid")
    public void loginAuthAPI_validCredential_shouldReturnAuthDTO() throws Exception{
        when(authTokenService.createAuthToken(any(AuthTokenCreateRequest.class))).thenReturn(authTokenResponse);

        mockMvc.perform(
            post("/api/auth/login")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", setupEmployee.getUsername()) 
            .param("password", "secret$") 
        )
        .andDo(print())
        .andExpect(status().isOk()) 
        .andExpect(cookie().exists("refreshToken"))
        .andExpect(cookie().maxAge("refreshToken", 604800))
        .andExpect(jsonPath("$.data.refreshToken").value(authTokenResponse.refreshToken()))
        .andExpect(jsonPath("$.errors").isEmpty())
        .andExpect(jsonPath("$.data.accessToken").value(authTokenResponse.accessToken()));
    }

    @Test
    @DisplayName("should return badrequest error of invalid param  when param doesnt meet pattern requirement")
    public void loginAuthAPI_invalidParam_shouldReturBadReq() throws Exception{ 

        mockMvc.perform(
            post("/api/auth/login")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", setupEmployee.getUsername()) 
            .param("password", "secret") 
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("password has to be 5-50 length and has atleast 1 unique char"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return badrequest error of blank param  when param is blank/null empty")
    public void loginAuthAPI_blankParam_shouldReturnBadReq() throws Exception{ 

        mockMvc.perform(
            post("/api/auth/login")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED) 
            .param("password", "secret$") 
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("username cannot be empty"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return notfound error when username is not valid")
    public void loginAuthAPI_invalidUsername_shouldReturnBadReq() throws Exception{ 
        when(authTokenService.createAuthToken(any())).thenThrow(new NotFoundEntityException("Employee with username " + setupEmployee.getUsername() + " is not found"));

        mockMvc.perform(
            post("/api/auth/login")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED) 
            .param("username", setupEmployee.getUsername()) 
            .param("password", "secret$") 
        )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors").value("Employee with username " + setupEmployee.getUsername() + " is not found"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return AccountExpiredException when user is not active")
    public void loginAuthAPI_inactiveUser_shouldReturnBadReq() throws Exception{ 
        when(authTokenService.createAuthToken(any())).thenThrow(new AccountExpiredException("Employee with username " + setupEmployee.getUsername() + " is not active"));

        mockMvc.perform(
            post("/api/auth/login")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED) 
            .param("username", setupEmployee.getUsername()) 
            .param("password", "secret$") 
        )
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errors").value("Employee with username " + setupEmployee.getUsername() + " is not active"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return BadCredentialsException when password wrong")
    public void loginAuthAPI_wrongCredentials_shouldReturnBadReq() throws Exception{ 
        when(authTokenService.createAuthToken(any())).thenThrow(new BadCredentialsException("Bad credentials, wrong password!"));

        mockMvc.perform(
            post("/api/auth/login")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED) 
            .param("username", setupEmployee.getUsername()) 
            .param("password", "secret$") 
        )
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errors").value("Bad credentials, wrong password!"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("should return Auth DTO when credentials valid")
    public void refreshAuthAPI_validCredential_shouldReturnAuthDTO() throws Exception{
        Cookie cookie = new Cookie("refreshToken", "someRefreshToken");
        when(authTokenService.refreshAuthToken(cookie.getValue())).thenReturn(authTokenResponse);

        mockMvc.perform(
            post("/api/auth/refresh")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)  
            .cookie(cookie)  
        )
        .andDo(print())
        .andExpect(status().isOk())  
        .andExpect(jsonPath("$.data.refreshToken").value(authTokenResponse.refreshToken()))
        .andExpect(jsonPath("$.errors").isEmpty())
        .andExpect(jsonPath("$.data.accessToken").value(authTokenResponse.accessToken()));
    }

    @Test
    @DisplayName("should return Bad request when cookie token is empty")
    public void refreshAuthAPI_emptyCookie_shouldReturnBadReq() throws Exception{ 

        mockMvc.perform(
            post("/api/auth/refresh")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)    
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").value("refreshToken at cookie is missing, try to login first")); 
    }

    @Test
    @DisplayName("should return Auth DTO when credentials valid")
    public void refreshAuthAPI_AuthenticationTokenException_shouldReturnAuthDTO() throws Exception{
        Cookie cookie = new Cookie("refreshToken", "someRefreshToken");
        when(authTokenService.refreshAuthToken(cookie.getValue())).thenThrow(new AuthenticationTokenException("Credentials token is not valid"));

        mockMvc.perform(
            post("/api/auth/refresh")
            .with(csrf())
            .accept(MediaType.APPLICATION_JSON_VALUE)  
            .cookie(cookie)  
        )
        .andDo(print())
        .andExpect(status().isUnauthorized())  
        .andExpect(jsonPath("$.data").isEmpty())
        .andExpect(cookie().exists("refreshToken"))
        .andExpect(cookie().maxAge("refreshToken", 0))
        .andExpect(jsonPath("$.errors").value("Credentials token is not valid"));
    }
    
    
}
