import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {BrowserModule, By} from '@angular/platform-browser';

import {LoginComponent} from './login.component';
import {AppRoutingModule} from "../app-routing.module";
import {MainComponent} from "../main/main.component";
import {RegisterComponent} from "../register/register.component";
import {UserService} from "../services/user.service";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let html: HTMLElement;
  let button: HTMLElement;
  let registerButton: HTMLElement;

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
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.debugElement.componentInstance;
    html = fixture.debugElement.query(By.css("form")).nativeElement;
    button = fixture.debugElement.query(By.css(".btn-default")).nativeElement;
    registerButton = fixture.debugElement.query(By.css(".btn-space")).nativeElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeDefined();
  });

  it('should contain form', () => {
    expect(html).toBeTruthy();
  });

  it('should call onSubmit', async () => {
    spyOn(component, 'onSubmit');
    button.click();
    expect(component.onSubmit).toHaveBeenCalledTimes(1);
  });

  it('should call onRegister', async () => {
    spyOn(component, 'onRegister');
    registerButton.click();
    expect(component.onRegister).toHaveBeenCalledTimes(1);
  });
});
