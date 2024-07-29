import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Post } from '../interface/post.model';
/**
 * Service pour gérer les opérations CRUD des posts.
 * Fournit des méthodes pour créer, lire, mettre à jour et obtenir des posts par ID.
 */
@Injectable({
  providedIn: 'root'
})
export class PostService {
  private pathService = '/api/post';


  /**
   * Constructeur pour injecter le service HttpClient.
   * 
   * @param {HttpClient} httpClient - Le service HttpClient pour effectuer des requêtes HTTP.
   */
  constructor(private httpClient: HttpClient) {}

/**
   * Récupère tous les posts disponibles.
   * 
   * @returns {Observable<Post[]>} - Un observable contenant un tableau de posts.
   */

  getPosts(sortBy: string = 'date', order: string = 'desc'): Observable<Post[]> {
    const params = new HttpParams().set('sortBy', sortBy).set('order', order);
    return this.httpClient.get<Post[]>(this.pathService, { params });
  }
   /**
   * Crée un nouveau post.
   * 
   * @param {Post} post - L'objet post à créer.
   * @returns {Observable<Post>} - Un observable contenant le post créé.
   */
  createPost(post: Post): Observable<Post> {
    return this.httpClient.post<Post>(this.pathService, post);
  }

  /**
   * Récupère un post par son identifiant.
   * 
   * @param {number} id - L'identifiant du post à récupérer.
   * @returns {Observable<Post>} - Un observable contenant le post récupéré.
   */
  getPostById(id: number): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/${id}`);
  }

  /**
   * Met à jour un post existant.
   * 
   * @param {Post} post - L'objet post à mettre à jour.
   * @returns {Observable<Post>} - Un observable contenant le post mis à jour.
   */
  updatePost(post: Post): Observable<Post> {
    return this.httpClient.put<Post>(`${this.pathService}/${post.id}`, post);
  }

}
