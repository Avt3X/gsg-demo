import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';
import {User} from '../models';
import {UserService} from '../services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage: string;

  private static constructUserModel(form: NgForm): User {
    const user = new User();
    user.username = form.value.username;
    user.password = form.value.password;

    return user;
  }

  constructor(private router: Router,
              private service: UserService) { }

  ngOnInit() {
  }

  onSubmit(form: NgForm): void {
    const user = LoginComponent.constructUserModel(form);
    this.login(user);
  }

  onRegister(): void {
    this.router.navigate(['/register']);
  }

  private login(user: User): void {
    this.service.login(user).subscribe(data => {
        localStorage.setItem('username', user.username);
        localStorage.setItem('password', user.password);
        this.router.navigate(['/main']);
      },
      error1 => {
        this.errorMessage = error1.error;
      });
  }

}
