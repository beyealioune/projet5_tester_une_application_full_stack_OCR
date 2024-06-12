import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

import { RouterTestingModule } from '@angular/router/testing';
import { FormComponent } from 'src/app/features/sessions/components/form/form.component';
import { SessionService } from 'src/app/services/session.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionService: any;
  let mockTeacherService: any;
  let mockSessionApiService: any;
  let mockRouter: any;
  let mockActivatedRoute: any;
  let mockMatSnackBar: any;

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: { admin: true }
    };

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([]))
    };

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({})),
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({}))
    };

    mockRouter = {
      navigate: jest.fn(),
      url: ''
    };

    mockActivatedRoute = {
      snapshot: { paramMap: { get: jest.fn().mockReturnValue('1') } }
    };

    mockMatSnackBar = {
      open: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        MatSnackBarModule,
        RouterTestingModule
      ],
      declarations: [FormComponent],
      providers: [
        FormBuilder,
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    component.ngOnInit();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should call initForm with session data on update', () => {
    mockRouter.url = '/sessions/update';
    const sessionData = { name: 'Session 1', date: '2024-01-01', teacher_id: '1', description: 'Description 1' };
    mockSessionApiService.detail.mockReturnValue(of(sessionData));
    component.ngOnInit();
    expect(component.onUpdate).toBe(true);
    expect(component.id).toBe('1');
    expect(component.sessionForm?.value).toEqual({
      name: 'Session 1',
      date: '2024-01-01',
      teacher_id: '1',
      description: 'Description 1'
    });
  });

  it('should submit and create a new session', () => {
    component.onUpdate = false;
    component.initForm();
    component.sessionForm?.setValue({ name: 'Session 1', date: '2024-01-01', teacher_id: '1', description: 'Description 1' });
    component.submit();
    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should submit and update an existing session', () => {
    component.onUpdate = true;
    component.id = '1';
    component.initForm();
    component.sessionForm?.setValue({ name: 'Session 1', date: '2024-01-01', teacher_id: '1', description: 'Description 1' });
    component.submit();
    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', { name: 'Session 1', date: '2024-01-01', teacher_id: '1', description: 'Description 1' });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should show snackbar message on exitPage', () => {
    component.exitPage('Test message');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Test message', 'Close', { duration: 3000 });
  });
});
