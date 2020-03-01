import { Component, OnInit } from '@angular/core';
import {NgForm} from '@angular/forms';
import {User} from '../models';
import {Router} from '@angular/router';
import {UserService} from '../services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  errorMessage: string;

  private static constructUserModel(form: NgForm): User {
    const user = new User();
    user.username = form.value.username;
    user.password = form.value.password;
    user.country = form.value.country;
    user.jobExecutionTime = form.value.jobTime;

    return user;
  }

  constructor(private router: Router,
              private service: UserService) { }

  ngOnInit() {
  }

  onSubmit(form: NgForm): void {
    const user = RegisterComponent.constructUserModel(form);

    if (user.jobExecutionTime > 0 && user.jobExecutionTime < 60) {
      this.register(user);
    } else {
      this.errorMessage = 'Job time must be between 1 and 60';
    }
  }

  private register(user: User): void {
    this.service.register(user).subscribe(data => {
        this.router.navigate(['/login']);
      },
      error1 => {
        this.errorMessage = error1.error;
      });
  }
}
