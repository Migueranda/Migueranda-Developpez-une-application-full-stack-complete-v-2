import { ChangeDetectionStrategy, NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule, DomSanitizer } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';
import { HeaderComponent } from './features/components/header/header.component';
import { FormComponent } from './features/components/post/form/form.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule} from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { PostRoutingModule } from './features/components/post/post-routing.module';
import {LayoutModule} from '@angular/cdk/layout';
import { FlexLayoutModule } from '@angular/flex-layout';
import {MatInputModule} from '@angular/material/input';
import { DetailComponent } from './features/components/post/detail/detail.component';
import { SubjectComponent } from './features/components/subject/list/subject.component';
import { PostComponent } from './features/components/post/list/post.component';
import { UserComponent } from './features/components/user/user.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { AuthService } from './features/auth/auth.service';
import { LoginRoutingModule } from './features/auth/auth-routing.module';
import { MatSidenavModule } from '@angular/material/sidenav';
import { AuthInterceptor } from './features/auth/AuthInterceptor';


const materialModule = [
  MatButtonModule,
  MatCardModule,
  MatIconModule,
  MatToolbarModule,
  MatSnackBarModule,
  MatSelectModule,
  BrowserAnimationsModule, 
  FormsModule, 
  
]

@NgModule({
  declarations: [AppComponent, HomeComponent, SubjectComponent, HeaderComponent, PostComponent, FormComponent, DetailComponent, UserComponent, LoginComponent, RegisterComponent],
  imports: [
    BrowserModule,
    PostRoutingModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    HttpClientModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    MatFormFieldModule,
    MatInputModule,   
    MatSelectModule,
    LoginRoutingModule,
    FlexLayoutModule,
    MatSidenavModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
   
    ...materialModule
  ],
  providers: [{provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'outline'}}, AuthService, 
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  
  bootstrap: [AppComponent],
})
export class AppModule {
  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
    iconRegistry.addSvgIcon(
      'arrow_back',
      sanitizer.bypassSecurityTrustResourceUrl('assets/icons/arrow_back.svg')
    );

    iconRegistry.addSvgIcon(
      'arrow-down',
      sanitizer.bypassSecurityTrustResourceUrl('assets/icons/arrow-down.svg')
    );
  } 
}
