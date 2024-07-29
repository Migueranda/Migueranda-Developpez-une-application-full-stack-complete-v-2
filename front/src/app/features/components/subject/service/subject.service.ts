import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, map } from "rxjs";
import { Subject } from "../interface/subject.model";

/**
 * Service pour gérer les thèmes et les abonnements aux thèmes.
 * Fournit des méthodes pour obtenir des thèmes, s'abonner et se désabonner des thèmes.
 */
@Injectable({
    providedIn: 'root'
})

export class SubjectService{

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
        return this.httpClient.get<{subject: Subject[]}>(this.pathService).pipe(
            map(response => response.subject) 
        );
    }

    /**
     * Abonne un utilisateur à un thème spécifique.
     * 
     * @param {number} userId - L'identifiant de l'utilisateur.
     * @param {number} subjectId - L'identifiant du thème.
     * @returns {Observable<any>} - Un observable contenant la réponse de l'abonnement.
     */
    subscribeToSubject(userId: number, subjectId: number): Observable<any> {
        return this.httpClient.post(`${this.subscriptionPathService}/${userId}/${subjectId}`, {});
    }

    /**
   * Désabonne l'utilisateur d'un thème spécifique.
   * 
   * @param {number} subjectId - L'identifiant du thème auquel se désabonner.
   */
    unsubscribeFromSubject(userId: number, subjectId: number): Observable<any> {
        return this.httpClient.delete(`${this.subscriptionPathService}/${userId}/${subjectId}`);
    }
}