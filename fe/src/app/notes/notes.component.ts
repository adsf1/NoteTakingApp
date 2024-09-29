import { Component } from '@angular/core';
import { Note } from '../model/note.model';
import { NgFor, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { ReactiveFormsModule, Validators, FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-notes',
  standalone: true,
  imports: [NgFor, NgIf, ReactiveFormsModule],
  templateUrl: './notes.component.html',
  styleUrl: './notes.component.css'
})
export class NotesComponent { // TODO: Change URLs
  notes: Note[] = [];
  error: string | null = null;

  showForm: boolean = false;
  isEdit: boolean = false;
  currentNoteId: number | null = null;
  noteForm = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.minLength(5)]),
    description: new FormControl('', null),
  });

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

  toggleCreateForm() {
    this.showForm = !this.showForm;
    this.isEdit = false;
    this.noteForm.reset({ title: '', description: '' });
    this.error = null;
    this.currentNoteId = null;
  }

  uploadNote() {
    if (this.noteForm.valid) {
      if(this.isEdit){
        this.updateNote();
      } else {
        this.createNote();
      }
    }
  }

  createNote(){    
    this.http.post<Note>('http://localhost:8080/notes', this.noteForm.value)
      .pipe(
        catchError(error => {
          this.error = 'Failed to create note!';
          console.log(error);
          return of(null);
        })
      )
      .subscribe(note => {
        if (note) {
          this.notes.push(note);
          this.noteForm.reset({ title: '', description: '' });
          this.showForm = false;
          this.currentNoteId = null;
          this.error = null;
        }
      });
  }

  updateNote(){
    this.http.put<Note>(`http://localhost:8080/notes/${this.currentNoteId}`, this.noteForm.value)
      .pipe(
        catchError(error => {
          this.error = 'Failed to update note!';
          console.log(error);
          return of(null);
        })
      )
      .subscribe(note => {
        if (note) {
          const index = this.notes.findIndex(c => c.id === this.currentNoteId);

          if (index !== -1) {
            this.notes[index] = note;

          this.noteForm.reset({ title: '', description: '' });
          this.showForm = false;
          this.isEdit = false;
          this.currentNoteId = null;
          this.error = null;
          }
        }
      });
  }

  openEditForm(noteId: number){
    const note = this.notes.find(note => note.id === noteId);
    this.currentNoteId = noteId;

    if (note) {
      this.noteForm.patchValue({
        title: note.title,
        description: note.description
      });
    }

    this.showForm = true;
    this.isEdit = true;
    this.error = null;
  }
}
