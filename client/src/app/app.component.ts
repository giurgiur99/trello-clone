import { Component, OnInit } from '@angular/core';
import { UserService } from './modules/user/shared/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import {Location} from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit { 

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router:Router,
    private location:Location) { }

  ngOnInit(): void {
    console.log(this.router.url);
    console.log(this.location.path());
     if(this.location.path() === ''){
      this.userService.currentUser().subscribe(user => {
        this.router.navigate([user.username+'/boards']);
      })
     console.log(this.router.url);
    }  
  }

}
