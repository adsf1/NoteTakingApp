import { Routes } from '@angular/router';
import { NotesComponent } from './notes/notes.component';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

export const routes: Routes = [
    { path: 'notes', component: NotesComponent },
    { path: '', component: AppComponent },
    { path: '**', component: PageNotFoundComponent }
];
