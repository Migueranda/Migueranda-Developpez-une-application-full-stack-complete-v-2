import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, tap, throwError } from 'rxjs';
import { User } from '../components/subject/interface/subject.model';
import { Router } from '@angular/router';

/**
 * Service d'authentification pour gérer l'enregistrement, la connexion et la déconnexion des utilisateurs.
 * Fournit des méthodes pour interagir avec l'API d'authentification.
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private userSubject: BehaviorSubject<User | null>;
  public user: Observable<User | null>;

  private pathService = '/api/auth';
  private token: string | null = null;
   /**
   * Constructeur pour initialiser les services HttpClient et Router, ainsi que les thèmes de comportement pour l'utilisateur et le token.
   * 
   * @param httpClient - Le service HttpClient pour effectuer des requêtes HTTP.
   * @param router - Le service Router pour la navigation.
   */

  constructor(private httpClient: HttpClient, private router: Router) {
    const userFromStorage = localStorage.getItem('user');
    const tokenFromStorage = localStorage.getItem('token');
    this.userSubject = new BehaviorSubject<User | null>(userFromStorage ? JSON.parse(userFromStorage) : null);
    this.user = this.userSubject.asObservable();
    this.token = tokenFromStorage;
  }

  /**
   * Getter pour obtenir la valeur actuelle de l'utilisateur connecté.
   * 
   * @returns L'utilisateur actuellement connecté ou null.
   */
  public get userValue(): User | null {
    return this.userSubject.value;
  }

  /**
   * Enregistre un nouvel utilisateur en envoyant une requête POST à l'API d'authentification.
   * 
   * @param user - L'objet User contenant les informations de l'utilisateur à enregistrer.
   * @returns Un Observable contenant l'objet User enregistré.
   */
  register(user: User): Observable<User> {
    return this.httpClient.post<User>(`${this.pathService}/register`, user).pipe(
      tap(response => {
        console.log('User registration successful:', response);
      }),
      catchError(error => {
        console.error('User registration error:', error);
        return throwError(error.error || 'Server error');
      })
    );
  }

  /**
   * Connecte un utilisateur en envoyant une requête POST à l'API d'authentification avec les informations de connexion.
   * 
   * @param credentials - Un objet contenant l'email et le mot de passe de l'utilisateur.
   * @returns Un Observable contenant l'objet User connecté.
   */
  login(credentials: { email: string, password: string }): Observable<User> {
    return this.httpClient.post<User>(`${this.pathService}/login`, credentials).pipe(
      tap(user => {
        localStorage.setItem('user', JSON.stringify(user));
        localStorage.setItem('token', user.token);
        this.token = user.token;
        this.userSubject.next(user);
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(error.error || 'Server error');
      })
    );
  }

/**
   * Déconnecte l'utilisateur en supprimant les informations de l'utilisateur et du token du localStorage,
   * puis redirige vers la page de connexion.
   */
  logout(): void {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  /**
   * Obtient le token JWT actuel.
   * 
   * @returns Le token JWT actuel ou null.
   */
  getToken(): string | null {
    return this.token;
  }
}
