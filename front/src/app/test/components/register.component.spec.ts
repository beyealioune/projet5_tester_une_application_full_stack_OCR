

import { RegisterComponent } from 'src/app/features/auth/components/register/register.component';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { spyOn } from 'jest-mock'


describe('RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let authService: AuthService;
    let router: Router;
  
    // Configuration de base du module de test
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [RegisterComponent],
        // Importez les modules nécessaires pour les tests
        imports: [ReactiveFormsModule, RouterTestingModule, HttpClientTestingModule], // Ajoutez HttpClientTestingModule ici
        // Fournissez les services requis pour le composant
        providers: [AuthService]
      }).compileComponents(); // Compile les composants asynchrones
    });
  
    // Configuration spécifique pour chaque test
    beforeEach(() => {
      fixture = TestBed.createComponent(RegisterComponent); // Crée un composant de test
      component = fixture.componentInstance; // Récupère l'instance du composant
      authService = TestBed.inject(AuthService); // Injecte le service AuthService
      router = TestBed.inject(Router); // Injecte le service Router
      fixture.detectChanges(); // Déclenche la détection des changements dans le composant
    });
  
    // Test pour vérifier si le composant est créé
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  
    it('should show error on registration failure', () => {
        const errorMessage = 'Registration failed';
        jest.spyOn(authService, 'register').mockReturnValue(throwError(errorMessage));
    
        component.submit(); // Appelle la méthode submit du composant
    
        expect(component.onError).toBeTruthy();
    });
    // Test pour vérifier si la navigation vers la page de connexion est effectuée en cas d'enregistrement réussi
    it('should navigate to login on successful registration', () => {
        jest.spyOn(authService, 'register').mockReturnValue(of(undefined)); // Simule un enregistrement réussi
        const navigateSpy = jest.spyOn(router, 'navigate'); // Espionne la méthode navigate du Router

        component.submit(); // Appelle la méthode submit du composant

        expect(navigateSpy).toHaveBeenCalledWith(['/login']); // Vérifie si la méthode navigate a été appelée avec ['/login']
    });
  
});
