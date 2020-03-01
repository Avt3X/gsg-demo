import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {of} from 'rxjs';
import {UserService} from './services/user.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private route: Router,
              private service: UserService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    if (!localStorage.getItem('username')) {
      this.route.navigate(['login']);
    }
    return of(true);
  }
}
