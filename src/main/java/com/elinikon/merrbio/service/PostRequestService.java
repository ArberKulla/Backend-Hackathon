package com.elinikon.merrbio.service;

import com.elinikon.merrbio.dto.PostDTO;
import com.elinikon.merrbio.dto.PostRequestDTO;
import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import com.elinikon.merrbio.entity.User;
import com.elinikon.merrbio.repository.PostRepository;
import com.elinikon.merrbio.repository.PostRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PostRequestService {
    @Autowired
    PostRequestRepository postRequestRepository;

    @Autowired
    PostRepository postRepository;

    public List<PostRequest> getFromUser(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return postRequestRepository.findByUserId(user.getId());
    }

    public PostRequest create(PostRequestDTO postRequestDTO,int post_id, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        ModelMapper modelMapper = new ModelMapper();
        PostRequest postRequest = modelMapper.map(postRequestDTO, PostRequest.class);
        postRequest.setUser(user);
        postRequest.setPost(postRepository.getById(post_id));

        return postRequest;
    }
}
