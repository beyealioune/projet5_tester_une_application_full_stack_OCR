import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';
import { Session } from 'src/app/features/sessions/interfaces/session.interface';

describe('SessionApiService', () => {
    let service: SessionApiService;
    let httpTestingController: HttpTestingController;
  
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [SessionApiService]
      });
      service = TestBed.inject(SessionApiService);
      httpTestingController = TestBed.inject(HttpTestingController);
    });
  
    afterEach(() => {
      httpTestingController.verify();
    });
  
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  
    it('should send a GET request to fetch all sessions', () => {
      const mockSessions: Session[] = [{
          name: '',
          description: '',
          date: new Date,
          teacher_id: 0,
          users: []
      }];
  
      service.all().subscribe((sessions) => {
        expect(sessions).toEqual(mockSessions);
      });
  
      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toEqual('GET');
      req.flush(mockSessions);
    });
      
    // Test similar to above for other methods like detail, delete, create, update, participate, unParticipate
  });
  