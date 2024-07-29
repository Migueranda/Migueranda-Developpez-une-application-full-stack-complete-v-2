import { Component, OnInit } from '@angular/core';
import { Subject } from '../interface/subject.model';
import { SubjectService } from '../service/subject.service';
import { AuthService } from 'src/app/features/auth/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

/**
 * Composant pour afficher et gérer les thèmes.
 * Gère l'affichage des thèmes et permet aux utilisateurs de s'abonner à des thèmes.
 */
@Component({
  selector: 'app-subject',
  templateUrl: './subject.component.html',
  styleUrls: ['./subject.component.scss']
})
export class SubjectComponent implements OnInit {
  subjects: Subject[] = [];
  user: any;

  /**
   * Constructeur pour injecter les services nécessaires.
   * 
   * @param {SubjectService} subjectService - Le service pour gérer les thèmes.
   * @param {AuthService} authService - Le service d'authentification.
   * @param {MatSnackBar} snackBar - Le MatSnackBar pour afficher des notifications.
   */

  constructor(
    private subjectService: SubjectService,
    private authService: AuthService, 
    private snackBar: MatSnackBar 
  ) { }

   /**
   * Initialisation du composant.
   * Récupère l'utilisateur actuel et charge les thèmes.
   */
  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.loadSubjects();
  }


  /**
   * Charge tous les thèmes disponibles.
   */
  loadSubjects(): void {
    this.subjectService.getSubjects().subscribe({
      next: (data) => {
        this.subjects = data;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des thèmes', error);
      },
      complete: () => {
        console.log('Chargement des thèmes complet');
      }
    });
  }

   /**
   * Abonne l'utilisateur connecté à un thème spécifique.
   * 
   * @param {number} subjectId - L'identifiant du thème auquel s'abonner.
   */
  subscribe(subjectId: number): void {
    if (this.user) {
      this.subjectService.subscribeToSubject(this.user.id, subjectId).subscribe({
        next: () => {
          this.snackBar.open('Abonné avec succès', 'Fermer', {
            duration: 3000
          });
        },
        error: (error) => {
          console.error('Erreur lors de la souscription', error);
          this.snackBar.open('Erreur lors de la souscription', 'Fermer', {
            duration: 3000
          });
        }
      });
    }
  }
}
