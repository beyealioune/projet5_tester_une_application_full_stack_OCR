describe('Login spec', () => {
    it('Login successful', () => {
      cy.visit('/login');
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true
        },
      });
  
      cy.intercept({
        method: 'GET',
        url: '/api/session',
      }, []).as('session');
  
      cy.get('input[formControlName=email]').type("yoga@studio.com");
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
  
      // Attendre que la redirection vers '/sessions' se produise
      cy.url().should('include', '/sessions');
  
      // Cliquer sur le bouton "Create"
      cy.contains('Create').click();
  
      // Attendre que le formulaire soit visible
      cy.get('form').should('be.visible');
  
      // Cliquer sur le bouton "Account"
      cy.contains('Account').click();
  
      // Attendre que la redirection vers '/me' se produise
      cy.url().should('include', '/me');
  
      // Vérifier si le texte "User information" est présent
      cy.contains('User information').should('be.visible');
  
      // Cliquer sur le bouton "Logout"
      cy.contains('Logout').click();
  
      // Vérifier que l'URL est redirigée vers localhost:4200
      cy.url().should('eq', 'http://localhost:4200/');
  
      // Vérifier que les boutons "Login" et "Register" sont visibles
      cy.contains('Login').should('be.visible');
      cy.contains('Register').should('be.visible');
    });
  });
  