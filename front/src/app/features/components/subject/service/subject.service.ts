import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, map } from "rxjs";
import { Subject } from "../../user/interface/user.model";
import { ApiResponse } from "../interface/apis.response"; 

/**
 * Service pour gérer les thèmes et les abonnements aux thèmes.
 * Fournit des méthodes pour obtenir des thèmes, s'abonner et se désabonner des thèmes.
 */
@Injectable({
    providedIn: 'root'
})
export class SubjectService {

    private pathService = '/api/subject';
    private subscriptionPathService = '/api/subscriptions';

    /**
     * Constructeur pour injecter le service HttpClient.
     * 
     * @param {HttpClient} httpClient - Le service HttpClient pour effectuer des requêtes HTTP.
     */
    constructor(private httpClient: HttpClient) {}

    /**
     * Récupère tous les thèmes disponibles.
     * 
     * @returns {Observable<Subject[]>} - Un observable contenant un tableau de thèmes.
     */
    getSubjects(): Observable<Subject[]> {
        return this.httpClient.get<{ subject: Subject[] }>(this.pathService).pipe(
            map(response => response.subject)
        );
    }

    getSubjectsForUser(userId: number): Observable<Subject[]> {
        return this.httpClient.get<Subject[]>(`${this.pathService}/subjects/user/${userId}`);
    }

    /**
     * Abonne un utilisateur à un thème spécifique.
     * 
     * @param {number} userId - L'identifiant de l'utilisateur.
     * @param {number} subjectId - L'identifiant du thème.
     * @returns {Observable<ApiResponse>} - Un observable contenant la réponse de l'abonnement.
     */
    subscribeToSubject(userId: number, subjectId: number): Observable<ApiResponse> {
        return this.httpClient.post<ApiResponse>(`${this.subscriptionPathService}/${userId}/${subjectId}`, {});
    }

    /**
     * Désabonne l'utilisateur d'un thème spécifique.
     * 
     * @param {number} userId - L'identifiant de l'utilisateur.
     * @param {number} subjectId - L'identifiant du thème.
     * @returns {Observable<ApiResponse>} - Un observable contenant la réponse du désabonnement.
     */
    unsubscribeFromSubject(userId: number, subjectId: number): Observable<ApiResponse> {
        return this.httpClient.delete<ApiResponse>(`${this.subscriptionPathService}/${userId}/${subjectId}`);
    }
}
