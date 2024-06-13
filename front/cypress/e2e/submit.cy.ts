describe('Homepage spec', () => {
    it('should display Yoga app on the homepage', () => {
        cy.visit('http://localhost:4200');
        cy.contains('Yoga app').should('be.visible');
    });

    it('should navigate to the register page and display registration form fields', () => {
        cy.visit('http://localhost:4200');

        // Cliquer sur le bouton "Register"
        cy.contains('Register').click();

        // Vérifier que l'URL inclut "/register"
        cy.url().should('include', '/register');

        // Vérifier que les champs "first name", "last name", "email", et "password" sont présents
        cy.get('input[formControlName=firstName]').should('be.visible');
        cy.get('input[formControlName=lastName]').should('be.visible');
        cy.get('input[formControlName=email]').should('be.visible');
        cy.get('input[formControlName=password]').should('be.visible');
    });

    it('should fill the registration form and submit', () => {
        cy.visit('http://localhost:4200/register');

        // Remplir les champs du formulaire
        cy.get('input[formControlName=firstName]').type('test');
        cy.get('input[formControlName=lastName]').type('test');
        cy.get('input[formControlName=email]').type('test@test.com');
        cy.get('input[formControlName=password]').type('tst!1234');

        // Cliquer sur le bouton "Submit"
        cy.contains('Submit').click();

        // // Ajouter une vérification après le clic sur "Submit" si nécessaire
        // // par exemple, vérifier que la page est redirigée vers une nouvelle URL ou qu'un message de succès est affiché
        // cy.url().should('include', '/register');  // Remplacez '/dashboard' par l'URL vers laquelle vous êtes redirigé après l'inscription
        // cy.contains('Login').should('be.visible');  // Remplacez par le message de succès attendu
    });
});
