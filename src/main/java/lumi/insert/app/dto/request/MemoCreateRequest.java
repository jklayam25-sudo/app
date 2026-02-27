package lumi.insert.app.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoCreateRequest {
    @NotBlank(message = "title cannot be empty") 
    String title;

    @NotBlank(message = "body cannot be empty") 
    String body;

    List<MultipartFile> images;

    @Pattern(regexp = "FINANCE|CASHIER|WAREHOUSE|OWNER", message = "check documentation for role specification")
    private String role;
}
