package com.elinikon.merrbio.service;

import com.elinikon.merrbio.dto.PostDTO;
import com.elinikon.merrbio.entity.Post;
import com.elinikon.merrbio.entity.PostRequest;
import com.elinikon.merrbio.entity.User;
import com.elinikon.merrbio.repository.PostRepository;
import com.elinikon.merrbio.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByTitle(String title) {
        // If no title filter, return all posts
        if (title == null || title.isEmpty()) {
            return getAll();
        }
        return postRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Post> getFromUser(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        return postRepository.findByUserId(user.getId());
    }

    @Transactional
    public Post create(PostDTO postDTO, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        ModelMapper modelMapper = new ModelMapper();
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user);

        return post;
    }

    @Transactional
    public Post update(int id, PostDTO postDTO, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Post current = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));

        if(current.getUser()!=user){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot edit this post");
        }

        current.setDescription(postDTO.getDescription());
        current.setTitle(postDTO.getTitle());
        current.setPrice(postDTO.getPrice());
        current.setImage(postDTO.getImage());

        return postRepository.save(current);
    }

    @Transactional
    public void deleteById(int id, Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Post current = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));

        if(current.getUser()!=user){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this post");
        }

        postRepository.deleteById(id);
    }
}
