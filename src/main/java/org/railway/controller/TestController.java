package org.railway.controller;

import org.railway.annotation.CheckUserId;
import org.railway.dto.request.ChangeRecordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class TestController {

    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    @CheckUserId
    public ResponseEntity<String> test(@RequestBody ChangeRecordRequest request) {
        System.out.println(request);
        return ResponseEntity.ok("Hello, Railway! This is a test endpoint.");
    }
}

