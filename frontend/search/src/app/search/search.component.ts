import { Component, OnInit } from '@angular/core';
import { SearchService } from '../search.service';
import {Article} from "../article";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  constructor(searchService: SearchService) {
    this.searchService = searchService
  }

  ngOnInit() {
  }

  submitted = false;
  searchService : SearchService;
  articles : Article[];
  error : string;

  q = "";

  onSubmit() {
    this.submitted = true;
    this.searchService.search( this.q ).subscribe(
      data => { this.articles = data;  },
      error => { console.log(error); this.error = error },
      () => { console.log("ARticles : "); console.log( this.articles) })
  }

}
