import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {BrowserModule, By} from '@angular/platform-browser';
import {NgForm} from '@angular/forms';

import {AppRoutingModule} from "../app-routing.module";
import {MainComponent} from "../main/main.component";
import {RegisterComponent} from "./register.component";
import {UserService} from "../services/user.service";
import {LoginComponent} from "../login/login.component";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let html: HTMLElement;
  let button: HTMLElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RegisterComponent, MainComponent, LoginComponent],
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
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.debugElement.componentInstance;
    html = fixture.debugElement.query(By.css("form")).nativeElement;
    button = fixture.debugElement.query(By.css(".btn-default")).nativeElement;
    fixture.detectChanges();
  });

  it('should create register', () => {
    let compiled = fixture.debugElement.nativeElement;
    expect(component).toBeDefined();
    expect(compiled.querySelector('h1').textContent).toBe('Register');
  });

  it('should contain form', () => {
    expect(html).toBeTruthy();
  });

  it('should call onSubmit', async () => {
    spyOn(component, 'onSubmit');

    await fixture.whenStable().then(() => {
      expect(fixture.debugElement.query(By.css("#email"))).toBeTruthy();
      expect(fixture.debugElement.query(By.css("#pwd"))).toBeTruthy();
      expect(fixture.debugElement.query(By.css("#country"))).toBeTruthy();
      expect(fixture.debugElement.query(By.css("#jobTime"))).toBeTruthy();
      button.click();
      expect(component.onSubmit).toHaveBeenCalledTimes(1);
    });
  });

  it('should set error message value', () => {
    let form = new NgForm([], []);
    form.value.username = "user";
    form.value.password = "test";
    form.value.country = "GE";
    form.value.jobTime = 0;

    component.onSubmit(form);
    fixture.detectChanges();

    expect(component.errorMessage).toBe('Job time must be between 1 and 60');
  });
});
