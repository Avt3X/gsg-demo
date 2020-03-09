import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {BrowserModule, By} from '@angular/platform-browser';

import {AppRoutingModule} from "../app-routing.module";
import {MainComponent} from "./main.component";
import {UserService} from "../services/user.service";
import {LoginComponent} from "../login/login.component";
import {RegisterComponent} from "../register/register.component";
import {YoutubeResult} from "../models";

describe('MainComponent', () => {
  let component: MainComponent;
  let fixture: ComponentFixture<MainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent, MainComponent, RegisterComponent],
      imports: [
        BrowserModule,
        FormsModule,
        AppRoutingModule,
        HttpClientModule
      ],
      providers: [UserService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MainComponent);
    component = fixture.debugElement.componentInstance;
    fixture.detectChanges();
  });

  it('should click on logout', () => {
    spyOn(component, 'onLogOut');
    let logout = fixture.debugElement.query(By.css('#logout')).nativeElement;
    logout.click();
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      expect(component.onLogOut).toHaveBeenCalledTimes(1);
    });
  });

  it('should redirect to login', () => {
    component.onLogOut();
    fixture.detectChanges();
  });

  it('should set error message value', () => {
    component.jobTime = 0;
    component.onUpdate();
    fixture.detectChanges();

    expect(component.message).toBe('Job time must be between 1 and 60');
  });

  it('should create result elements', () => {
    let r = new YoutubeResult();
    r.upVotedCommentLink = 'link';
    r.trendedVideoLink = 'link';
    r.createdAt = 'date';
    component.result = r;
    fixture.detectChanges();

    expect(component.result).toBeDefined();

    fixture.whenStable().then(() => {
      expect(fixture.debugElement.query(By.css("#video"))).toBeTruthy();
      expect(fixture.debugElement.query(By.css("#comment"))).toBeTruthy();
      expect(fixture.debugElement.query(By.css("#date"))).toBeTruthy();
      expect(fixture.debugElement.query(By.css("#error"))).toBeUndefined();
    });
  });

  it('should show result error text', () => {
    let r = new YoutubeResult();
    r.errorMessage = 'error';
    component.result = r;
    fixture.detectChanges();

    expect(component.result).toBeDefined();

    fixture.whenStable().then(() => {
      expect(fixture.debugElement.query(By.css("#video"))).toBeUndefined();
      expect(fixture.debugElement.query(By.css("#comment"))).toBeUndefined();
      expect(fixture.debugElement.query(By.css("#date"))).toBeUndefined();
      expect(fixture.debugElement.query(By.css("#error"))).toBeTruthy();
      expect(fixture.debugElement.query(By.css("#errText")).nativeElement.text).toBe('error');
    });
  });
});
