import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User, YoutubeResult} from '../models';
import {Observable} from 'rxjs';

@Injectable()
export class UserService {

  private baseUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) {
  }

  login(user: User): Observable<{}> {
    return this.httpClient.post<User>(this.baseUrl.concat('/login'), user);
  }

  register(user: User): Observable<{}> {
    return this.httpClient.post<User>(this.baseUrl.concat('/users/register'), user);
  }

  update(user: User): Observable<any> {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');
    return this.httpClient.put<User>(this.baseUrl.concat('/users'), user, {headers: {username, password}});
  }

  getUser(): Observable<User> {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');
    return this.httpClient.get<User>(this.baseUrl.concat('/users'), {headers: {username, password}});
  }

  logOut(): Observable<any> {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');
    return this.httpClient.post<any>(this.baseUrl.concat('/log-out'), {}, {headers: {username, password}});
  }

  getLastResult(): Observable<YoutubeResult> {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');
    return this.httpClient.get<YoutubeResult>(this.baseUrl.concat('/users/job/result'), {headers: {username, password}});
  }
}
