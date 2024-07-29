import { RouterModule, Routes } from "@angular/router";
import { NgModule } from "@angular/core";
import { FormComponent } from "./form/form.component";
import { PostComponent } from "./list/post.component";
import { DetailComponent } from "./detail/detail.component";
import { AuthGuard } from "src/app/guards/auth.guards";

const routes : Routes = [
    
    { path:'post', title: 'Post', component: PostComponent, canActivate: [AuthGuard] },
    { path: 'create', title: 'Post - create', component: FormComponent },
    { path: 'post/:id', title: 'Post - detail', component: DetailComponent },
    { path: '', redirectTo: '/post', pathMatch: 'full' }
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class PostRoutingModule {}