import { of } from 'rxjs';
import { DetailComponent } from 'src/app/features/sessions/components/detail/detail.component';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar'; // Importer MatSnackBar

describe('DetailComponent', () => {
    let component: DetailComponent;
    let mockSessionService: any;
    let mockSessionApiService: any;
    let mockTeacherService: any;
    let mockMatSnackBar: any; // Définir mockMatSnackBar
    let mockRouter: any; // Définir mockRouter
  
    beforeEach(() => {
        // Initialisation des mocks nécessaires
        
        // Mock du service de l'API de session
        mockSessionApiService = {
          // Mock des méthodes de l'API de session avec des valeurs de retour observables
          delete: jest.fn().mockReturnValue(of({})),
          participate: jest.fn().mockReturnValue(of({})),
          unParticipate: jest.fn().mockReturnValue(of({})),
          // Mock de la méthode de détail avec une valeur de retour observable
          detail: jest.fn().mockReturnValue(of({ id: '123', users: [123], teacher_id: '456' }))
        };
        
        // Mock du service de session
        mockSessionService = {
          sessionInformation: { admin: true, id: 123 } // Simuler une valeur pour sessionInformation
        };
        
        // Mock du service d'enseignant
        mockTeacherService = {
          detail: jest.fn() // Définir une fonction mock pour le service teacher
      };
      
        // Mock d'ActivatedRoute avec une méthode paramMap
        const mockActivatedRoute = {
          snapshot: {
            paramMap: {
              get: jest.fn().mockReturnValue('123')
            }
          }
        };
      
        // Mock de MatSnackBar
        mockMatSnackBar = {
          open: jest.fn() // Simuler la méthode open
        };
      
        // Mock de Router
        mockRouter = {
          navigate: jest.fn() // Simuler la méthode navigate
        };
      
        // Initialisation de DetailComponent avec les mocks
        component = new DetailComponent(
          mockActivatedRoute as unknown as ActivatedRoute,
          {} as any,
          mockSessionService,
          mockSessionApiService,
          mockTeacherService, // Injecter mockTeacherService
          mockMatSnackBar, // Injecter mockMatSnackBar
          mockRouter // Injecter mockRouter
        );
      });
      

    it('should create', () => {
      expect(component).toBeTruthy(); // Vérifier si le composant a été créé avec succès
    });

    it('should delete session', () => {
      component.delete(); // Appeler la méthode delete du composant
      // Vérifier si la méthode delete du service de l'API de session a été appelée avec l'ID de session correct
      expect(mockSessionApiService.delete).toHaveBeenCalledWith('123');
      // Vérifier si la méthode open de MatSnackBar a été appelée avec le bon message
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      // Vérifier si la méthode navigate de Router a été appelée avec la bonne route
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should participate', () => {
      component.participate(); // Appeler la méthode participate du composant
      // Vérifier si la méthode participate du service de l'API de session a été appelée avec les bons paramètres
      expect(mockSessionApiService.participate).toHaveBeenCalledWith('123', '123');
    });

    it('should unParticipate', () => {
      component.unParticipate(); // Appeler la méthode unParticipate du composant
      // Vérifier si la méthode unParticipate du service de l'API de session a été appelée avec les bons paramètres
      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('123', '123');
    });

    it('should fetch session', () => {
      component.ngOnInit(); // Appeler la méthode ngOnInit du composant
      // Vérifier si la méthode detail du service de l'API de session a été appelée avec le bon ID de session
      expect(mockSessionApiService.detail).toHaveBeenCalledWith('123');
      // Vérifier si la session du composant a été définie correctement après l'appel de la méthode ngOnInit
      expect(component.session).toEqual({ id: '123', users: [123], teacher_id: '456' });
      // Vérifier si la variable isParticipate du composant est définie à true après l'appel de la méthode ngOnInit
      expect(component.isParticipate).toBeTruthy();
      // Vérifier si la méthode detail du service d'enseignant a été appelée avec le bon ID d'enseignant
      expect(mockTeacherService.detail).toHaveBeenCalledWith('456');
    });
});
