package com.elinikon.merrbio.controller;

import com.elinikon.merrbio.dto.PostDTO;
import com.elinikon.merrbio.dto.PostResponse;
import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import com.elinikon.merrbio.repository.PostRequestRepository;
import com.elinikon.merrbio.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostRESTController {

    @Autowired
    PostService postService;

    @Autowired
    PostRequestRepository postRequestRepository;

    @GetMapping("/get")
    public ResponseEntity<Page<PostResponse>> getPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        // Call the service method with all the parameters
        return new ResponseEntity<>(postService.getPostsByTitleAndPrice(title, page, minPrice, maxPrice), HttpStatus.OK);
    }

    @GetMapping("/get/user")
    public ResponseEntity<List<Post>> getUserPosts(Principal connectedUser) {
       return new ResponseEntity<>(postService.getFromUser(connectedUser), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Post> create(@RequestBody PostDTO post,Principal connectedUser) {
        Post createdPost = postService.create(post,connectedUser);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/put/{post_id}")
    public ResponseEntity<Post> edit(@RequestBody PostDTO post,@PathVariable int post_id,Principal connectedUser) {
        Post createdPost = postService.update(post_id,post,connectedUser);
        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<Void> delete(@PathVariable int post_id,Principal connectedUser) {
        postService.deleteById(post_id,connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}