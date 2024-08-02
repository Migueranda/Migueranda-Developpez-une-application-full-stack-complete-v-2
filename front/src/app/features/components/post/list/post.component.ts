import { Component, OnInit } from '@angular/core';
import { Post } from '../interface/post.model';
import { PostService } from '../service/post.service';
import { Router } from '@angular/router';
import { UserService } from '../../user/service/user.sevice';
import { forkJoin, Subscription } from 'rxjs';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

/**
 * Composant pour afficher et gérer les posts.
 * Gère le chargement, le tri et la navigation des posts.
 */
@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  userNames: { [key: number]: string } = {};
  posts : Post[] =[];
  sorted: boolean = false;
  sortAscending: boolean = true;
  private subscriptions: Subscription[] = [];

  /**
   * Constructeur pour injecter les services nécessaires.
   * 
   * @param {PostService} postService - Le service pour gérer les posts.
   * @param {Router} router - Le routeur Angular pour la navigation.
   * @param {UserService} userService - Le service pour gérer les utilisateurs.
   */
  constructor(private postService : PostService, private router: Router, private userService: UserService,
   
  ){ }

  /**
   * Initialisation du composant.
   * Charge les posts lors de l'initialisation.
   */
  ngOnInit(): void {
    this.loadPosts();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
  
   /**
   * Charge tous les posts disponibles et les noms des utilisateurs associés.
   */
  loadPosts(): void {
    const subjectsSubscription = this.userService.getCurrentUser().subscribe({
      next: (user) => {
        const subjectsSubscription =  this.postService.getPostsByUserSubject(user.id).subscribe({
          next: (posts) => {
            this.posts = posts;
            console.log('Posts loaded:', this.posts);
            this.sortPosts(); 
          },
          error: (error) => {
            console.error('Erreur lors du chargement des posts', error);
          },
          complete: () => {
            console.log('Chargement des posts complet');
          }
        });
        this.subscriptions.push(subjectsSubscription);
      },
      error: (error) => {
        console.error('Erreur lors de la récupération de l\'utilisateur courant', error);
      }
    });
    this.subscriptions.push(subjectsSubscription);    
  }

   /**
   * Charge les noms des utilisateurs associés aux posts.
   */
  loadUserNames(): void {
    const userIds = Array.from(new Set(this.posts.map(post => post.userId))); 
    const userRequests = userIds.map(id => this.userService.getUserById(id));
    
    const subjectsSubscription = forkJoin(userRequests).subscribe(users => {
      users.forEach(user => {
        this.userNames[user.id] = user.userName;
      });
    });
    this.subscriptions.push(subjectsSubscription);
  }

 /**
   * Bascule le tri des posts par date.
  */
  toggleSortByDate(): void {
    this.sortAscending = !this.sortAscending;
    this.loadPosts(); 
  }

  sortPosts(): void {
    this.posts.sort((a, b) => {
        const dateA = new Date(a.date).getTime();
        const dateB = new Date(b.date).getTime();
        return this.sortAscending ? dateA - dateB : dateB - dateA;
    });
  }

   /**
   * Navigue vers la page de détails d'un post spécifique.
   * 
   * @param {number} postId - L'identifiant du post à afficher.
   */
  navigateToPost(postId: number): void {
    this.router.navigate(['/post', postId]);
  }
}
