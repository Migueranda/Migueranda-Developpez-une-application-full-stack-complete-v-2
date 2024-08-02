import { Component, Input, OnInit } from '@angular/core';
// import { Subject, User } from '../interface/subject.model';
import { SubjectService } from '../service/subject.service';
import { AuthService } from 'src/app/features/auth/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject } from '../../user/interface/user.model';
import { User } from '../../user/interface/user.model';
import { Subscription } from 'rxjs';
import { UserService } from '../../user/service/user.sevice';

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
  user: User | null = null; 
  private subscriptions: Subscription[] = [];
  userSubs:  Subject[] =[];

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
    private snackBar: MatSnackBar,
    private userService : UserService 
  ) { }

   /**
   * Initialisation du composant.
   * Récupère l'utilisateur actuel et charge les thèmes.
   */
  ngOnInit(): void {
    this.user = this.authService.userValue;
    if(this.user?.id){
      this.userService.getUserById(this.user.id).subscribe(user => {
        this.userSubs = user.subscription;
        this.loadSubjects();
      });
    }
  } 

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }  

  /**
   * Charge tous les thèmes disponibles.
  */
  loadSubjects(): void {
    const subjectsSubscription = this.subjectService.getSubjects().subscribe({
      next: (data) => {
        this.subjects = data;
        // routine pour assigner l'attribut followed 
        // en fonction de la présence du théme souscrit dans le tableau this.userSUbs
        this.subjects.forEach(subject => {
          const item = this.userSubs.find(userSub => userSub.id === subject.id);
          if (item) {
            subject.followed = true;
          }else{
            subject.followed = false;
          }
        });
      },
      error: (error) => {
        console.error('Erreur lors du chargement des thèmes', error);
      },
      complete: () => {
        console.log('Chargement des thèmes complet');
      }
    });
    this.subscriptions.push(subjectsSubscription);
  }  

   /**
   * Abonne l'utilisateur connecté à un thème spécifique.
   * 
   * @param {number} subjectId - L'identifiant du thème auquel s'abonner.
   */
  subscribe(subjectId: number): void {
    if (this.user) {
      const subscribeSubscription = this.subjectService.subscribeToSubject(this.user.id, subjectId).subscribe({
        next: () => {
          this.snackBar.open('Abonné avec succès', 'Fermer', {
            duration: 3000
          });
          this.updateSubjectFollowedStatus(subjectId, true);
        },
        error: (error) => {
          console.error('Erreur lors de la souscription', error);
          this.snackBar.open('Erreur lors de la souscription', 'Fermer', {
            duration: 3000
          });
        }
      });
      this.subscriptions.push(subscribeSubscription);
    }
  }

  updateSubjectFollowedStatus(subjectId: number, followed: boolean): void {
    this.subjects = this.subjects.map(subject =>
      subject.id === subjectId ? { ...subject, followed } : subject
    );
  }
 
}
