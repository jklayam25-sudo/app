package lumi.insert.app.controller;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lumi.insert.app.controller.wrapper.WebResponse;
import lumi.insert.app.dto.request.MemoCreateRequest;
import lumi.insert.app.dto.request.MemoUpdateRequest;
import lumi.insert.app.dto.response.MemoResponse;
import lumi.insert.app.entity.nondatabase.EmployeeLogin;
import lumi.insert.app.service.MemoService;

@RestController
public class MemoController {
    
    @Autowired
    MemoService memoService;

    @PostMapping(
        path = "/api/memos",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<WebResponse<MemoResponse>> createMemoAPI(@Valid MemoCreateRequest request){
        MemoResponse resultFromService = memoService.createMemo(request);

        WebResponse<MemoResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/" + resultFromService.id())
        .buildAndExpand(resultFromService.id())
        .toUri();

        return ResponseEntity.created(location).body(wrappedResult);
    }

    @PostMapping(
        path = "/api/memos/{id}/read",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Boolean>> createMemoViewAPI(@AuthenticationPrincipal EmployeeLogin login, @PathVariable(name = "id") Long id){
        Boolean resultFromService = memoService.createMemoView(login, id);

        WebResponse<Boolean> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    
    @PatchMapping(
        path = "/api/memos/{id}",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<MemoResponse>> updateMemoAPI(@PathVariable(name = "id", required = true) Long id, @Valid MemoUpdateRequest request){
        MemoResponse resultFromService = memoService.updateMemo(id, request);

        WebResponse<MemoResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @PreAuthorize("hasAuthority('OWNER')")
    @PostMapping(
        path = "/api/memos/{id}/archive",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<MemoResponse>> archiveMemoAPI(@PathVariable(name = "id") Long id){
        MemoResponse resultFromService = memoService.archiveMemo(id);

        WebResponse<MemoResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/memos/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<MemoResponse>> getMemoAPI(@PathVariable(name = "id") Long id){
        MemoResponse resultFromService = memoService.getMemo(id);

        WebResponse<MemoResponse> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }

    @GetMapping(
        path = "/api/memos",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<WebResponse<Slice<MemoResponse>>> getMemosAPI(@AuthenticationPrincipal EmployeeLogin login, @RequestParam(name = "updatedAt", required = false) LocalDateTime time){
        if(time == null) time = LocalDateTime.now().minusMonths(1);
        Slice<MemoResponse> resultFromService = memoService.getMemos(login, time);

        WebResponse<Slice<MemoResponse>> wrappedResult = WebResponse.getWrapper(resultFromService, null);
 
        return ResponseEntity.ok(wrappedResult);
    }



}
