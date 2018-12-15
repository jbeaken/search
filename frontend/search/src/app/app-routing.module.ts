import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SearchComponent } from './search/search.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const appRoutes: Routes = [
  { path: 'search', component: SearchComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
