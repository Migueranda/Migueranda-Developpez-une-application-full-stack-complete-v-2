package com.openclassrooms.mddapi.controllers;


import com.openclassrooms.mddapi.model.dtos.CommentDto;
import com.openclassrooms.mddapi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Contrôleur gérant les opérations sur les commentaires.
 * Fournit des points de terminaison pour ajouter et récupérer les commentaires associés à un post.
 */

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;


    /**
     * Ajoute un commentaire à un post spécifié.
     *
     * @param postId l'identifiant du post auquel ajouter le commentaire
     * @param commentDto l'objet de transfert de données contenant les informations du commentaire à ajouter
     * @return une ResponseEntity contenant l'objet CommentDto du commentaire créé ou un message d'erreur en cas de format d'ID de post invalide
     */
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<?> addComment(@PathVariable String postId, @RequestBody CommentDto commentDto) {
        try {
            Long postID = Long.parseLong(postId);
            CommentDto createdComment = commentService.addComment(postID, commentDto);
            return ResponseEntity.created(URI.create("/post/" + postID + "/comments/" + createdComment.getId())).body(createdComment);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid post ID format");
        }
    }

    /**
     * Récupère tous les commentaires associés à un post spécifié.
     *
     * @param postId l'identifiant du post pour lequel récupérer les commentaires
     * @return une ResponseEntity contenant une liste de CommentDto représentant les commentaires du post ou un message d'erreur en cas de format d'ID de post invalide
     */
    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<?> getComments(@PathVariable String postId) {
        try {
            Long postID = Long.parseLong(postId);
            List<CommentDto> comments = commentService.getCommentsByPostId(postID);
            return ResponseEntity.ok(comments);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid post ID format");
        }
    }
}
