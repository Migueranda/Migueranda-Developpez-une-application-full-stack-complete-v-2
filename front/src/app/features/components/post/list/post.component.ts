import { Component, OnInit } from '@angular/core';
import { Post } from '../interface/post.model';
import { PostService } from '../service/post.service';
import { Router } from '@angular/router';
import { UserService } from '../../user/service/user.sevice';
import { forkJoin } from 'rxjs';
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

   /**
   * Charge tous les posts disponibles et les noms des utilisateurs associés.
   */

  loadPosts(): void {
    const order = this.sortAscending ? 'asc' : 'desc';
    this.postService.getPosts('date', order).subscribe({
      next: (posts) => {
        this.posts = posts;
        this.loadUserNames();
      },
      error: (error) => {
        console.error('Erreur lors du chargement des posts', error);
      },
      complete: () => {
        console.log('Chargement des posts complet');
      }
    });
  }
   /**
   * Charge les noms des utilisateurs associés aux posts.
   */
  loadUserNames(): void {
    const userIds = Array.from(new Set(this.posts.map(post => post.userId))); 
    const userRequests = userIds.map(id => this.userService.getUserById(id));
    
    forkJoin(userRequests).subscribe(users => {
      users.forEach(user => {
        this.userNames[user.id] = user.userName;
      });
    });
  }

 /**
   * Bascule le tri des posts par date.
   */

  toggleSortByDate(): void {
    this.sortAscending = !this.sortAscending;
    this.loadPosts(); 
  }

   /**
   * Trie les posts par date dans l'ordre spécifié.
   */
  sortPosts(): void {
    if (this.sortAscending) {
      this.posts.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
    } else {
      this.posts.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
    }
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
