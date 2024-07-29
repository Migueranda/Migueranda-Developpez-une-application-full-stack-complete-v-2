import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../service/post.service';
import { SubjectService } from '../../subject/service/subject.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Post } from '../interface/post.model';
import { CommentService } from '../service/comment.service';
import { UserService } from '../../user/service/user.sevice';
import { Comment, CreateComment } from '../interface/comment.model';
import { AuthService } from 'src/app/features/auth/auth.service'; 
import { forkJoin, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { DatePipe } from '@angular/common';

/**
 * Composant pour afficher les détails d'un post.
 * Gère l'affichage des commentaires et permet aux utilisateurs d'ajouter des commentaires.
 */

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  providers: [DatePipe]
})
export class DetailComponent implements OnInit {
 

  posts: Post | undefined;
  public commentForm: FormGroup;
  public subjects$ = this.subjectService.getSubjects();
  subjectMap = new Map<number, string>();
  comments: any[] = [];
  user: any;
  userMap = new Map<number, string>(); 
  private postId: number | undefined;

  /**
   * Constructeur pour injecter les services nécessaires et initialiser le formulaire de commentaire.
   * 
   * @param route - Le service ActivatedRoute pour accéder aux paramètres de la route.
   * @param fb - Le FormBuilder pour créer le formulaire de commentaire.
   * @param postService - Le service pour gérer les posts.
   * @param subjectService - Le service pour gérer les thèmes.
   * @param commentService - Le service pour gérer les commentaires.
   * @param userService - Le service pour gérer les utilisateurs.
   * @param matSnackBar - Le MatSnackBar pour afficher des notifications.
   * @param router - Le Router pour la navigation.
   * @param authService - Le service d'authentification.
   * @param datePipe - Le DatePipe pour formater les dates.
   */

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private postService: PostService,
    private subjectService: SubjectService,
    private commentService: CommentService,
    private userService: UserService,
    private matSnackBar: MatSnackBar,
    public router: Router,
    private authService: AuthService,
    private datePipe: DatePipe 

  ) {
    this.commentForm = this.fb.group({
      description: ['', Validators.required]
    });
  }

   /**
   * Initialisation du composant.
   * Récupère les détails du post et charge les thèmes et les commentaires associés.
   */
  ngOnInit(): void {
    this.user = this.authService.userValue; 
    const postIdParam = this.route.snapshot.paramMap.get('id');

    if (postIdParam !== null) {
      this.postId = +postIdParam; 
      this.postService.getPostById(this.postId).pipe(
        switchMap(post => {
          this.posts = post;
          this.loadSubject(post.themeId);
          return this.userService.getUserById(post.userId);
        })
      ).subscribe(user => {
        if (user) {
          this.userMap.set(user.id, user.userName);
          this.loadComments(this.postId!); 
        }
      });
    }
    this.loadSubjects();
  }

  /**
   * Charge tous les thèmes disponibles et les stocke dans une map.
   */
  loadSubjects(): void {
    this.subjectService.getSubjects().subscribe(themes => {
      themes.forEach(theme => {
        this.subjectMap.set(theme.id, theme.title);
      });
    });
  }

  /**
   * Charge le sujet associé à un thèmeId spécifique.
   * 
   * @param themeId - L'identifiant du thème.
   */
  loadSubject(themeId: number): void {
    const themeTitle = this.subjectMap.get(themeId) || 'Thème inconnu';
    console.log('Le thème associé:', themeTitle);
  }

  /**
   * Charge les commentaires associés à un post spécifique et enrichit chaque commentaire avec le nom de l'utilisateur.
   * 
   * @param postId - L'identifiant du post.
   */

loadComments(postId: number): void {
  this.commentService.getCommentsByPostId(postId).subscribe(comments => {
      const userObservables = comments.map(comment => {
          //  pour récupérer le nom d'utilisateur pour chaque commentaire
          return this.userService.getUserById(comment.userId).pipe(
            map(user => {
                return {...comment, userName: user.userName};
            }),
            catchError(error => {
                console.error(`Erreur lors de la récupération du nom d'utilisateur pour userId: ${comment.userId}`, error);
                return of({...comment, userName: 'Erreur de récupération'});
            })
        );
        
      });

      forkJoin(userObservables).subscribe(commentsWithUsers => {
          this.comments = commentsWithUsers.map(comment => ({
              ...comment,
              formattedDate: this.datePipe.transform(comment.date, 'short')
          }));
          console.log("Commentaires après traitement:", this.comments);
      });
  });
}

 /**
   * Gère la soumission du formulaire de commentaire.
   * Ajoute un nouveau commentaire et met à jour la liste des commentaires affichés.
   */
onSubmit(): void {
  if (this.commentForm?.valid && this.posts?.id && this.user) {
      const commentData: CreateComment = {
          postId: this.posts.id,
          userId: this.user.id,
          description: this.commentForm.value.description,
          date: new Date() 
      };
      this.commentService.addComment(this.posts.id, commentData).subscribe({
          next: (comment) => {
              this.matSnackBar.open('Commentaire ajouté avec succès', 'Fermer', { duration: 3000 });
              this.commentForm?.reset();
          
              this.comments.push({
                  ...comment,
                  username: this.user.userName,
                  formattedDate: this.datePipe.transform(new Date(), 'short')
              });
          },
          error: (error) => {
              this.matSnackBar.open('Échec de l\'ajout du commentaire', 'Fermer', { duration: 3000 });
          }
      });
  }
}

 /**
   * Récupère le nom d'utilisateur pour un identifiant utilisateur spécifique.
   * 
   * @param userId - L'identifiant de l'utilisateur.
   * @returns Le nom de l'utilisateur ou 'Utilisateur inconnu' si l'utilisateur n'est pas trouvé.
   */
getUsername(userId: number): string {
  if (!userId) { // Vérification si userId est null ou undefined
      console.log("Appel de getUsername avec userId indéfini.");
      return 'Utilisateur inconnu';
  }
  const username = this.userMap.get(userId);
  return username || 'Utilisateur inconnu';
}

}
