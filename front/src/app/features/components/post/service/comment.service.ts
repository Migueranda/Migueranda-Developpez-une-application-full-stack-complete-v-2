import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Comment, CreateComment } from '../interface/comment.model'; 

/**
 * Service pour gérer les opérations CRUD des commentaires.
 * Fournit des méthodes pour obtenir et ajouter des commentaires associés à un post.
 */

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private pathService = '/api';
   /**
   * Constructeur pour injecter le service HttpClient.
   * 
   * @param {HttpClient} httpClient - Le service HttpClient pour effectuer des requêtes HTTP.
   */
  constructor(private httpClient: HttpClient) {}

  /**
   * Récupère tous les commentaires associés à un post spécifique.
   * 
   * @param {number} postId - L'identifiant du post pour lequel récupérer les commentaires.
   * @returns {Observable<Comment[]>} - Un observable contenant un tableau de commentaires.
   */
  getCommentsByPostId(postId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(`${this.pathService}/post/${postId}/comment`);
  }
  /**
   * Ajoute un nouveau commentaire à un post spécifique.
   * 
   * @param {number} postId - L'identifiant du post auquel ajouter le commentaire.
   * @param {CreateComment} comment - L'objet commentaire à ajouter.
   * @returns {Observable<Comment>} - Un observable contenant le commentaire ajouté.
   */

  addComment(postId: number, comment: CreateComment): Observable<Comment> {
    return this.httpClient.post<Comment>(`${this.pathService}/post/${postId}/comment`, comment);
  }
}
