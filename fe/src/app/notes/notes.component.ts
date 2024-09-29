import { Component } from '@angular/core';
import { Note } from '../model/note.model';
import { NgFor } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-notes',
  standalone: true,
  imports: [NgFor],
  templateUrl: './notes.component.html',
  styleUrl: './notes.component.css'
})
export class NotesComponent { // TODO: Change URLs
  notes: Note[] = [];
  error: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchNotes();
  }

  fetchNotes(){
    this.http.get<Note[]>('http://localhost:8080/notes')
      .pipe(
        catchError(error => {
          this.error = 'Failed to get notes!';
          return of([]);
        })
      )
      .subscribe(data => {
        if (data) {
          this.notes = data;
          console.log(this.notes);
          this.error = null;
        } else {
          this.error = 'Failed to get notes!';
        }
      });
  }

  deleteNote(noteId: number){
    this.http.delete(`http://localhost:8080/notes/${noteId}`)
      .pipe(
        catchError(error => {
          this.error = 'Failed to delete note!';
          return of(null);
        })
      )
      .subscribe(response => {
        this.notes = this.notes.filter(notes => notes.id !== noteId);
        this.error = null;
      });
  }
}
