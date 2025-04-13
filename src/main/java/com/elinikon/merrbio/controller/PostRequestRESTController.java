package com.elinikon.merrbio.controller;

import com.elinikon.merrbio.dto.PostRequestDTO;
import com.elinikon.merrbio.dto.PostRequestResponse;
import com.elinikon.merrbio.dto.PostRequestResponsePost;
import com.elinikon.merrbio.entity.PostRequest;
import com.elinikon.merrbio.service.PostRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts_request")
public class PostRequestRESTController {

    @Autowired
    PostRequestService postRequestService;

    @GetMapping("/get/user")
    public List<PostRequestResponsePost> getPostRequestsByUser(Principal connectedUser) {
        return postRequestService.getFromUser(connectedUser);
    }

    @PostMapping("/post/{post_id}")
    public ResponseEntity<PostRequest> create(@RequestBody PostRequestDTO postRequest, @PathVariable int post_id, Principal connectedUser) {
        PostRequest createdPostRequest = postRequestService.create(postRequest,post_id,connectedUser);
        return new ResponseEntity<>(createdPostRequest, HttpStatus.CREATED);
    }

    @PostMapping("/approve/{post_request_id}")
    public ResponseEntity<PostRequest> approve(@PathVariable int post_request_id, Principal connectedUser) {
        PostRequest createdPostRequest = postRequestService.approve(post_request_id,connectedUser);
        return new ResponseEntity<>(createdPostRequest, HttpStatus.CREATED);
    }


    @GetMapping("/post/{post_id}")
    public ResponseEntity<List<PostRequestResponse>> getByPostId(@PathVariable int post_id, Principal connectedUser) {
        List<PostRequestResponse> createdPostRequest = postRequestService.getByPostId(post_id,connectedUser);
        return new ResponseEntity<>(createdPostRequest, HttpStatus.CREATED);
    }
}