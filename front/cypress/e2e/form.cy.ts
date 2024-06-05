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
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session');
  
      cy.get('input[formControlName=email]').type("yoga@studio.com");
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
  
      // Attendre que la redirection vers '/sessions' se produise
      cy.url().should('include', '/sessions');
  
      // Cliquer sur le bouton "Create"
      cy.contains('Create').click();
  
      // Attendre que le formulaire soit visible
      cy.get('form').should('be.visible');
  
      // Vérifier si les champs "name", "date" et "description" sont présents
      cy.get('input[formControlName=name]').should('be.visible');
      cy.get('input[formControlName=date]').should('be.visible');
      cy.get('textarea[formControlName=description]').should('be.visible');
    });
  });
  