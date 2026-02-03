package lumi.insert.app.controller.wrapper;

import lombok.Builder;

@Builder
public record WebResponse<T>(T data, String errors) {
    public static <T> WebResponse<T> getWrapper(T data, String errors){
        return new WebResponse<T>(data, errors);
    }
}
