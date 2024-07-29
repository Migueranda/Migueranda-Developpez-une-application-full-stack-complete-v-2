import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AppModule } from './app.module';
import { SubjectComponent } from './features/components/subject/list/subject.component';
import { PostComponent } from './features/components/post/list/post.component';
import { AuthGuard } from './guards/auth.guards';
import { UserComponent } from './features/components/user/user.component';


const routes: Routes = [
  { 
    path: 'home', component: HomeComponent, 
  },
  {
    path: 'subject', component : SubjectComponent, canActivate: [AuthGuard]
  },
  {
    path: 'post', component : PostComponent, canActivate: [AuthGuard]
  },
  {
    path: 'user', component : UserComponent, canActivate: [AuthGuard]
  },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
