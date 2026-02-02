package lumi.insert.app.controller.wrapper;

import lombok.Builder;

@Builder
public record WebResponse<T>(T data, String errors) {
    
}
