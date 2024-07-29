import { RouterModule, Routes } from "@angular/router";
import { SubjectComponent } from "../list/subject.component"; 
import { NgModule } from "@angular/core";
import { AuthGuard } from "src/app/guards/auth.guards";

const routes : Routes = [
    { path:'subject',title: 'Subject', component: SubjectComponent, canActivate: [AuthGuard]},
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class SubjectRoutingModule {}