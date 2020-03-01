import {Component, OnInit} from '@angular/core';
import {UserService} from '../services/user.service';
import {User, YoutubeResult} from '../models';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {Router} from '@angular/router';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  result: YoutubeResult;
  countryCode: string;
  jobTime: number;
  errorMessage: string;
  stomp: any;
  message: string;
  error: any;

  constructor(private service: UserService,
              private router: Router) {
  }

  ngOnInit() {
    this.getUserInfo();
    this.connect();
    this.getLastResult();
  }

  onUpdate() {
    const user = new User();
    user.jobExecutionTime = this.jobTime;
    user.country = this.countryCode;
    this.service.update(user)
      .subscribe(success => {
        this.message = 'Updated';
        setTimeout(() => {
          this.message = undefined;
        }, 1000);
      }, error => {
        const status = error.status;
        if (status === 401) {
          localStorage.clear();
        }

        this.errorMessage = 'Unable to update';
      });
  }

  getLastResult() {
    this.service.getLastResult().subscribe(result => {
      console.log(result);
      if (result === null) {
        this.errorMessage = 'No results found';
        setTimeout(() => {
          this.errorMessage = undefined;
        }, 5000);
      } else {
        this.result = result;
      }
    }, error1 => {
      this.errorMessage = error1.error;
    });
  }

  onLogOut() {
    this.service.logOut().subscribe(() => {
      localStorage.clear();
      this.router.navigate(['/login']);
    }, error => {
      this.error = error.error;
    });
  }

  connect() {
    const sockJs = new SockJS('http://localhost:8080/web-socket');
    this.stomp = Stomp.over(sockJs);
    const thisObject = this;
    thisObject.stomp.connect({}, (frame) => {
      thisObject.stomp.subscribe('/topic/job/result/' + localStorage.getItem('username'), (sdkEvent) => {
        thisObject.onMessageReceived(sdkEvent);
      });
    }, this.errorCallBack);
  }

  errorCallBack(error) {
    console.log('errorCallBack -> ' + error);
    setTimeout(() => {
      this.connect();
    }, 5000);
  }

  onMessageReceived(message) {
    this.result = JSON.parse(message.body);
  }

  private getUserInfo() {
    this.service.getUser()
      .subscribe(user => {
        this.countryCode = user.country;
        this.jobTime = user.jobExecutionTime;
      }, error => {
        const status = error.status;
        if (status === 401) {
          localStorage.clear();
        }
        this.errorMessage = error.error;
      });
  }
}
