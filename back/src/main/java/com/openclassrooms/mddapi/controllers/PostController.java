package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.model.dtos.PostDto;
import com.openclassrooms.mddapi.model.entities.PostEntity;
import com.openclassrooms.mddapi.services.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


/**
 * Contrôleur gérant les opérations sur les posts.
 * Fournit des points de terminaison pour créer, récupérer tous les posts et récupérer un post par identifiant.
 */
@RestController
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    /**
     * Récupère tous les posts, triés par un champ spécifique.
     *
    * @param postDto l'objet de transfert de données contenant les critères de récupération des posts
     * @param sortBy le champ par lequel trier les résultats (par défaut, "date")
     * @param order l'ordre de tri (ascendant ou descendant, par défaut "desc")
     * @return une Map contenant une liste de PostDto représentant tous les posts triés
     */
    @GetMapping("/post")
    public List<PostDto> getAllPost(
            PostDto postDto,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        return postService.getAllPost(postDto, sortBy, order);
    }

    /**
     * Crée un nouveau post.
     *
     * @param postDto l'objet de transfert de données contenant les informations du post à créer
     * @return une ResponseEntity contenant l'objet PostDto du post créé
     */
    @PostMapping("/post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        return ResponseEntity.created(URI.create("/post/" + createdPost.getId())).body(createdPost);
    }

    /**
     * Récupère un post par son identifiant.
     *
     * @param id l'identifiant du post à récupérer
     * @return une ResponseEntity contenant l'objet PostDto représentant le post récupéré
     */
    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto postDto = postService.getPostById(id);
        return ResponseEntity.ok(postDto);
    }

    /**
     * Récupère tous les posts associés à un utilisateur spécifique.
     *
     * @param userId l'identifiant de l'utilisateur pour lequel récupérer les posts
     * @return une liste de PostEntity représentant les posts de l'utilisateur spécifié
     */
    @GetMapping("/post/user/{userId}")
    public List<PostEntity> getPostsForUser(@PathVariable Long userId) {
        return postService.getPostsForUser(userId);
    }
}
