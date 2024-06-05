import { TestBed, ComponentFixture } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { SessionService } from 'src/app/services/session.service';
import { of, throwError } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginComponent } from 'src/app/features/auth/components/login/login.component';
import { AuthService } from 'src/app/features/auth/services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [RouterTestingModule],
      providers: [
        FormBuilder,
        {
          provide: AuthService,
          useValue: { login: jest.fn() } // Mock the AuthService
        },
        SessionService
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit login request', () => {
    const loginResponse: SessionInformation = {
        token: '',
        type: '',
        id: 0,
        username: '',
        firstName: '',
        lastName: '',
        admin: false
    };
    const loginRequest = { email: 'test@example.com', password: 'password' };
    
    // Mock the login method of AuthService to return a mock response
    (authService.login as jest.Mock).mockReturnValue(of(loginResponse));
  
    // Spy on sessionService.logIn method
    const logInSpy = jest.spyOn(sessionService, 'logIn');
  
    // Mock the navigate method of Router
    const navigateSpy = jest.spyOn((component as any).router, 'navigate');
  
    // Call the submit method
    component.form.patchValue(loginRequest);
    component.submit();
  
    // Expectations
    expect(authService.login).toHaveBeenCalledWith(loginRequest);
    expect(logInSpy).toHaveBeenCalledWith(loginResponse);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should handle login error', () => {
    const error = new Error('Login failed');
    
    // Mock the login method of AuthService to return an error
    (authService.login as jest.Mock).mockReturnValue(throwError(error));

    // Call the submit method
    component.submit();

    // Expectations
    expect(component.onError).toBeTruthy();
  });
});
