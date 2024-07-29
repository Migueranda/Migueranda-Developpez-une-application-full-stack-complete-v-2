import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

/**
 * Composant pour la page de connexion.
 * Gère l'authentification des utilisateurs en vérifiant leurs informations d'identification.
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  hidePassword = true;
  errorMessage: string = '';

/**
   * Constructeur pour injecter les services nécessaires et initialiser le formulaire de connexion.
   *
   * @param {AuthService} authService - Le service d'authentification.
   * @param {Router} router - Le routeur Angular pour la navigation.
   * @param {FormBuilder} fb - Le FormBuilder pour créer le formulaire de connexion.
   * @param {MatIconRegistry} iconRegistry - Le registre des icônes Material.
   * @param {DomSanitizer} sanitizer - Le service pour sécuriser les URL des icônes.
   */
  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer
  ) {

    iconRegistry.addSvgIcon(
      'arrow_back',
      sanitizer.bypassSecurityTrustResourceUrl('assets/icons/arrow_back.svg')
    );

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
    
  }

  ngOnInit(): void {}

  /**
   * Gère la soumission du formulaire de connexion.
   * Vérifie si le formulaire est valide, puis tente de connecter l'utilisateur via le service d'authentification.
   * En cas d'erreur, affiche un message d'erreur approprié.
   */
  login(): void {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.login({ email, password } as any).subscribe({
        next: (response) => {
          this.router.navigate(['/post']);
        },
        error: (error) => {
          this.errorMessage = 'Invalid email or password';
        },
      });
    } else {
      this.errorMessage = 'Please enter valid email and password';
    }
  }


  /**
   * Bascule la visibilité du mot de passe dans le champ de saisie.
   */
  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
}
