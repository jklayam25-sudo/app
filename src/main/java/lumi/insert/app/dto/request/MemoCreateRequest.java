package lumi.insert.app.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request to create a new memo")
public class MemoCreateRequest {
    @NotBlank(message = "title cannot be empty")
    @Schema(description = "Memo title", example = "Important Announcement")
    String title;

    @NotBlank(message = "body cannot be empty")
    @Schema(description = "Memo content", example = "This is an important announcement for all staff.")
    String body;

    @Schema(description = "List of image files to attach")
    List<MultipartFile> images;

    @Pattern(regexp = "FINANCE|CASHIER|WAREHOUSE|OWNER", message = "check documentation for role specification")
    @Schema(description = "Target role for this memo (FINANCE, CASHIER, WAREHOUSE, or OWNER)", example = "OWNER")
    private String role;
}
