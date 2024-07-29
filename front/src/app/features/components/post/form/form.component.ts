import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../service/post.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Post } from '../interface/post.model';
import { SubjectService } from '../../subject/service/subject.service';
import { AuthService } from 'src/app/features/auth/auth.service';

/**
 * Composant pour le formulaire de création et de mise à jour des posts.
 * Gère l'affichage et la validation du formulaire ainsi que la soumission des données.
 */
@Component({
  selector: 'form-field',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FormComponent implements OnInit {

  public postForm: FormGroup | undefined;
  public subjects$ = this.subjectService.getSubjects();
  private postId: string | null = null;
  public onUpdate: boolean = false;
  private userId: number | null = null;


/**
   * Constructeur pour injecter les services nécessaires et initialiser les propriétés du composant.
   * 
   * @param {ActivatedRoute} route - Le service ActivatedRoute pour accéder aux paramètres de la route.
   * @param {FormBuilder} fb - Le FormBuilder pour créer le formulaire.
   * @param {MatSnackBar} matSnackBar - Le MatSnackBar pour afficher des notifications.
   * @param {PostService} postService - Le service pour gérer les posts.
   * @param {SubjectService} subjectService - Le service pour gérer les thèmes.
   * @param {AuthService} authService - Le service d'authentification.
   * @param {Router} router - Le Router pour la navigation.
   */
  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private matSnackBar: MatSnackBar,
    private postService: PostService,
    private subjectService: SubjectService,
    private authService: AuthService,
    private router: Router
  ) { }

  /**
   * Initialisation du composant.
   * Charge l'utilisateur actuel et les détails du post s'ils sont disponibles.
   */
  ngOnInit(): void {
    const user = this.authService.userValue;
    this.userId = user?.id || null;

    this.postId = this.route.snapshot.paramMap.get('id');

    if (this.postId) {
      this.postService.getPostById(+this.postId).subscribe({
        next: (post) => this.initForm(post),
        error: () => this.matSnackBar.open('Error loading the post', 'Close', { duration: 3000 })
      });
    } else {
      this.initForm();
    }
  }

   /**
   * Initialise le formulaire avec les données du post s'il est fourni.
   * 
   * @param {Post} [post] - Le post à éditer. Si non fourni, un nouveau post est créé.
   */
  private initForm(post?: Post): void {
    this.postForm = this.fb.group({
      title: [
        post ? post.title : '',
        [Validators.required]
      ],
      themeId: [
        post ? post.themeId : '',
        [Validators.required]
      ],
      description: [
        post ? post.description : '',
        [
          Validators.required,
          Validators.maxLength(2000)
        ]
      ],
      userId: [this.userId, [Validators.required]]
    });
  }

   /**
   * Gère la soumission du formulaire.
   * Crée ou met à jour un post en fonction de la présence d'un identifiant de post.
   */
  public onSubmit(): void {
    if (this.postForm?.valid) {
      const post = this.postForm.value;
      const operation = this.postId ? this.postService.updatePost({ ...post, id: +this.postId }) : this.postService.createPost(post);

      operation.subscribe({
        next: (result) => {
          this.matSnackBar.open('Post saved successfully!', 'Close', { duration: 3000 });
          this.router.navigate(['/post']);
        },
        error: () => this.matSnackBar.open('Failed to save the post', 'Close', { duration: 3000 })
      });
    }
  }
}
