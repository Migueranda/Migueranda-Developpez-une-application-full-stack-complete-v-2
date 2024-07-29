import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Post } from '../post/interface/post.model';
import { PostService } from '../post/service/post.service';
import { MatSidenav } from '@angular/material/sidenav';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
/**
 * Composant pour l'en-tête de l'application.
 * Gère l'affichage responsive de l'en-tête en fonction de la taille de la fenêtre.
 */

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
 
})
export class HeaderComponent implements OnInit {

  isMobile: boolean | undefined;


  /**
   * Constructeur pour injecter les services nécessaires et initialiser la variable `isMobile`.
   * 
   * @param {PostService} postService - Le service pour gérer les posts.
   * @param {BreakpointObserver} breakpointObserver - Le service pour observer les points de rupture de la mise en page.
   */
  constructor(
    private postService: PostService, private breakpointObserver: BreakpointObserver) {  this.isMobile = window.innerWidth <= 600; }
    @HostListener('window:resize', ['$event'])
    onResize(event: any) {
      this.isMobile = event.target.innerWidth <= 600;
    }
  ngOnInit(): void {
    
  }
  
} 
