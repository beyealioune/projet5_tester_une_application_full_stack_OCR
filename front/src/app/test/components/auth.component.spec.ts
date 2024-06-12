import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { RegisterRequest } from 'src/app/features/auth/interfaces/registerRequest.interface';
import { LoginRequest } from 'src/app/features/auth/interfaces/loginRequest.interface';

describe('AuthService', () => {
    let service: AuthService;
    let httpTestingController: HttpTestingController;
  
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule], // Importer HttpClientTestingModule pour simuler les requêtes HTTP
        providers: [AuthService]
      });
      service = TestBed.inject(AuthService);
      httpTestingController = TestBed.inject(HttpTestingController);
    });
  
    afterEach(() => {
      // Vérifier qu'il n'y a pas de requêtes HTTP en attente ou non interceptées
      httpTestingController.verify();
    });
  
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  
    it('should send a POST request to register', () => {
      const mockRegisterRequest: RegisterRequest = {
          email: '',
          firstName: '',
          lastName: '',
          password: ''
      };
      
      service.register(mockRegisterRequest).subscribe(() => {
        // Le test réussit si aucune erreur n'est survenue
      });
  
      const req = httpTestingController.expectOne('api/auth/register'); // S'attend à une requête POST à l'URL spécifiée
      expect(req.request.method).toEqual('POST'); // Vérifie que la méthode de la requête est POST
      req.flush(null); // Répond à la requête avec null pour simuler une réponse vide
    });
  
    it('should send a POST request to login', () => {
      const mockLoginRequest: LoginRequest = {
          email: '',
          password: ''
      };
      const mockSessionInformation: SessionInformation = {
          token: '',
          type: '',
          id: 0,
          username: '',
          firstName: '',
          lastName: '',
          admin: false
      };
      
      service.login(mockLoginRequest).subscribe((response) => {
        expect(response).toEqual(mockSessionInformation); // Vérifie que la réponse est celle attendue
      });
  
      const req = httpTestingController.expectOne('api/auth/login');
      expect(req.request.method).toEqual('POST');
      req.flush(mockSessionInformation); // Répond à la requête avec les données de session mock
    });
  });
  