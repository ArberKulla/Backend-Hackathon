package com.elinikon.merrbio.controller;

import com.elinikon.merrbio.dto.PostDTO;
import com.elinikon.merrbio.dto.PostRequestDTO;
import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import com.elinikon.merrbio.service.PostRequestService;
import com.elinikon.merrbio.service.PostService;
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
    public List<PostRequest> getPostRequestsByUser(Principal connectedUser) {
        return postRequestService.getFromUser(connectedUser);
    }

    @PostMapping("/post/{post_id}")
    public ResponseEntity<PostRequest> create(@RequestBody PostRequestDTO postReqeust, @PathVariable int post_id, Principal connectedUser) {
        PostRequest createdPostRequest = postRequestService.create(postReqeust,post_id,connectedUser);
        return new ResponseEntity<>(createdPostRequest, HttpStatus.CREATED);
    }
}