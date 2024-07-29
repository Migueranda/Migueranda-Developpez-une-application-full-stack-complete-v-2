import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../interface/user.model';
import { AuthService } from 'src/app/features/auth/auth.service';

/**
 * Service pour gérer les utilisateurs.
 * Fournit des méthodes pour obtenir, mettre à jour les utilisateurs et vérifier l'état de connexion.
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {


  private pathService = '/api/user';

   /**
   * Constructeur pour injecter les services HttpClient et AuthService.
   * 
   * @param {HttpClient} httpClient - Le service HttpClient pour effectuer des requêtes HTTP.
   * @param {AuthService} authService - Le service d'authentification pour gérer les informations de l'utilisateur connecté.
   */

  constructor(private httpClient: HttpClient,  private authService: AuthService ) {}

   /**
   * Récupère les informations de l'utilisateur actuellement connecté.
   * 
   * @returns {any} - Les informations de l'utilisateur actuellement connecté.
   */
  getUser(): any {
    return this.authService.userValue;
  }

  /**
   * Récupère un utilisateur par son identifiant.
   * 
   * @param {number} userId - L'identifiant de l'utilisateur à récupérer.
   * @returns {Observable<User>} - Un observable contenant les informations de l'utilisateur.
   */
  getUserById(userId: number): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/${userId}`);
  }

   /**
   * Met à jour les informations d'un utilisateur.
   * 
   * @param {number} userId - L'identifiant de l'utilisateur à mettre à jour.
   * @param {User} user - L'objet utilisateur contenant les nouvelles informations.
   * @returns {Observable<User>} - Un observable contenant les informations mises à jour de l'utilisateur.
   */
  updateUser(userId: number, user: User): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/${userId}`, user);
  }

  /**
   * Vérifie si un utilisateur est connecté.
   * 
   * @returns {boolean} - `true` si un utilisateur est connecté, sinon `false`.
   */
  isLoggedIn(): boolean {
    return !!this.authService.userValue;
  }
  
}
