import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../auth/auth.service';
import { UserService } from './service/user.sevice';
import { SubjectService } from '../subject/service/subject.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject } from './interface/user.model'; 
import { User } from './interface/user.model'; 

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  profileForm: FormGroup;
  user: any;
  subscriptions: Subject[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private subjectService: SubjectService,
    private snackBar: MatSnackBar
  ) {
    this.profileForm = this.fb.group({
      userName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    if (this.user) {
      this.profileForm.patchValue({
        userName: this.user.userName,
        email: this.user.email
      });
      this.loadSubscriptions();
    }
  }

  loadSubscriptions(): void {
    if (this.user) {
      this.userService.getUserById(this.user.id).subscribe(user => {
        this.subscriptions = user.subscription;
      });
    }
  }

  onSubmit(): void {
    if (this.profileForm.valid) {
      this.userService.updateUser(this.user.id, this.profileForm.value).subscribe({
        next: (updatedUser: User) => {
          this.snackBar.open('Profil mis à jour avec succès', 'Fermer', {
            duration: 3000,
          });
          // mise à jour du profil utilisateur dans AuthService
          this.authService.updateUser(updatedUser);
          
        },
        error: (error) => {
          this.snackBar.open('Erreur lors de la mise à jour du profil', 'Fermer', {
            duration: 3000,
          });
        }
      });
    }
  }

  onLogout(): void {
    this.authService.logout();
  }

  unsubscribe(subjectId: number): void {
    if (this.user) {
      this.subjectService.unsubscribeFromSubject(this.user.id, subjectId).subscribe({
        next: () => {
          this.snackBar.open('Désabonné avec succès', 'Fermer', {
            duration: 3000
          });
          this.loadSubscriptions(); 
        },
        error: (error) => {
          console.error('Erreur lors de la désouscription', error);
          this.snackBar.open('Erreur lors de la désouscription', 'Fermer', {
            duration: 3000
          });
        }
      });
    }
  }
}
