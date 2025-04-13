package com.elinikon.merrbio.service;

import com.elinikon.merrbio.dto.*;
import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import com.elinikon.merrbio.entity.User;
import com.elinikon.merrbio.entity.UserRole;
import com.elinikon.merrbio.repository.PostRepository;
import com.elinikon.merrbio.repository.PostRequestRepository;
import com.elinikon.merrbio.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostRequestService {
    @Autowired
    PostRequestRepository postRequestRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    public List<PostRequestResponsePost> getFromUser(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        List<PostRequest> postRequests = postRequestRepository.findByUserId(user.getId());
        List<PostRequestResponsePost> postRequestResponsePosts = new ArrayList<>();

        return  postRequestResponsePosts;
    }

    public List<PostRequestResponse> getByPostId(int post_id, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Fetch the Post entity by ID, throwing an exception if not found
        Post current = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + post_id));

        // Check if the current user is the owner of the post
        if (current.getUser().getId() != user.getId()  || user.getUserRole()== UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot fetch this post");
        }

        // Fetch all the PostRequests associated with the post
        List<PostRequest> postRequests = current.getPostRequests();

        // Map the PostRequest entities to PostRequestResponse DTOs
        List<PostRequestResponse> postRequestResponses = postRequests.stream()
                .map(postRequest -> {
                    // Map each PostRequest to a PostRequestResponse
                    PostRequestResponse response = new PostRequestResponse();
                    response.setId(postRequest.getId());
                    response.setPhoneNumber(user.getPhoneNumber());
                    response.setUserName(user.getUsername());
                    response.setCreatedDate(postRequest.getCreatedDate());
                    response.setDescription(postRequest.getDescription());
                    response.setApproved(postRequest.isApproved());
                    return response;
                })
                .collect(Collectors.toList());

        return postRequestResponses;
    }


    @Transactional
    public PostRequest create(PostRequestDTO postRequestDTO,int post_id, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        PostRequest postRequest = new PostRequest();
        postRequest.setDescription(postRequestDTO.getDescription());
        postRequest.setUser(userRepository.findById(user.getId()).get());
        postRequest.setPost(postRepository.findById(post_id).get());

        return postRequestRepository.save(postRequest);
    }

    @Transactional
    public PostRequest approve(int post_request_id, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(user.getId()!=postRequestRepository.findById(post_request_id).get().getPost().getUser().getId() || user.getUserRole()== UserRole.ADMIN){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot fetch this post");
        }

        PostRequest current = postRequestRepository.findById(post_request_id).get();
        current.setApproved(true);

        return postRequestRepository.save(current);
    }
}
