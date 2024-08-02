import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../auth/auth.service';
import { UserService } from './service/user.sevice';
import { SubjectService } from '../subject/service/subject.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject } from './interface/user.model'; 
import { User } from './interface/user.model'; 
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  profileForm: FormGroup;
  user: User | null = null;

  subscriptions: Subject[] = [];
  private subscriptions$: Subscription[] = [];
  
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
      console.log("USERSUBJ")
    console.log(this.user)
    }
  }

  ngOnDestroy(): void {
    this.subscriptions$.forEach(subscription$ => subscription$.unsubscribe());
  }

  loadSubscriptions(): void {
    if (this.user) {
      const subjectsSubscription =  this.userService.getUserById(this.user.id).subscribe(user => {
        this.subscriptions = user.subscription;
      });
      this.subscriptions$.push(subjectsSubscription);
    }
  }

  isSubscribed(subjectId: number): boolean {
    return this.subscriptions.some(subscription => subscription.id === subjectId);
  }

  onSubmit(): void {
    if (this.profileForm.valid && this.user) { 
      const subjectsSubscription =  this.userService.updateUser(this.user.id, this.profileForm.value).subscribe({
        next: (updatedUser: User) => {
          this.snackBar.open('Profil mis à jour avec succès', 'Fermer', {
            duration: 3000,
          });
          this.authService.updateUser(updatedUser);
        },
        error: (error: any) => {
          this.snackBar.open('Erreur lors de la mise à jour du profil', 'Fermer', {
            duration: 3000,
          });
        }
      });
      this.subscriptions$.push(subjectsSubscription);
    }
  }
  
  onLogout(): void {
    this.authService.logout();
  }

  unsubscribe(subjectId: number): void {
    if (this.user) {
      const subjectsSubscription =  this.subjectService.unsubscribeFromSubject(this.user.id, subjectId).subscribe({
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
      this.subscriptions$.push(subjectsSubscription);
    }
  }
}
