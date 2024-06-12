import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';
import { RouterTestingModule } from '@angular/router/testing';
import { MeComponent } from 'src/app/components/me/me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: any;
  let mockUserService: any;
  let mockRouter: any;
  let mockMatSnackBar: any;

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: { id: 1 },
      logOut: jest.fn()  // Assurez-vous que logOut est bien une fonction mockée
    };

    mockUserService = {
      getById: jest.fn().mockReturnValue(of({ id: 1, name: 'Test User' })),
      delete: jest.fn().mockReturnValue(of({}))
    };

    mockRouter = {
      navigate: jest.fn()
    };

    mockMatSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        RouterTestingModule
      ],
      declarations: [MeComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init', () => {
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual({ id: 1, name: 'Test User' });
  });

  it('should navigate back on back()', () => {
    const historyBackSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historyBackSpy).toHaveBeenCalled();
  });

  it('should delete user and navigate to home', () => {
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
