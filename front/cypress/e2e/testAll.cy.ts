const firstNameInput = 'input[formcontrolname="firstName"]';
const lastNameInput = 'input[formcontrolname="lastName"]';
const emailInput = 'input[formcontrolname="email"]';
const password = 'input[formcontrolname="password"]';
const buttonRegister = 'button[type="submit"]';

const ADMIN_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
  };
  const USER_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 2,
    username: 'user@user.com',
    firstName: 'User',
    lastName: 'User',
    admin: false,
  };
  const TEST_SESSION = {
    id: 1,
    name: 'TEST session',
    date: '2024-06-13T13:27:22.000+00:00',
    teacher_id: 1,
    description: 'TEST description for the session',
    users: [],
    createdAt: '2024-06-13T14:24:33',
    updatedAt: '2024-06-26T09:20:22',
  };
  const SESSIONS_LIST = [TEST_SESSION];
  const EDITED_TEST_SESSION = {
    ...TEST_SESSION,
    name: 'EDITED TEST session',
  };
  
  export const TEACHERS_LIST = [
    {
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: '2024-06-12T15:33:42',
      updatedAt: '2024-06-12T15:33:42',
    },
    {
      id: 2,
      lastName: 'THIERCELIN',
      firstName: 'Hélène',
      createdAt: '2024-06-12T15:33:42',
      updatedAt: '2024-06-12T15:33:42',
    },
  ];
  
  describe('Sessions page', () => {
    beforeEach(() => {
      cy.intercept('GET', '/api/session', (req) => {
        req.reply(SESSIONS_LIST);
      });
      cy.intercept('POST', '/api/session', (req) => {
        SESSIONS_LIST.push(TEST_SESSION);
  
        req.reply(TEST_SESSION);
      });
  
      cy.intercept('GET', `/api/session/${TEST_SESSION.id}`, TEST_SESSION);
  
      cy.intercept('DELETE', `/api/session/${TEST_SESSION.id}`, (req) => {
        SESSIONS_LIST.splice(0, 1);
  
        req.reply(EDITED_TEST_SESSION);
      });
  
      cy.intercept('PUT', `/api/session/${TEST_SESSION.id}`, (req) => {
        SESSIONS_LIST.splice(0, 1, EDITED_TEST_SESSION);
  
        req.reply(EDITED_TEST_SESSION);
      });
  
      cy.intercept('GET', `/api/teacher`, TEACHERS_LIST);
    });
    describe('As an admin', () => {
      beforeEach(() => {
        cy.visit('/login');
  
        cy.intercept('POST', '/api/auth/login', ADMIN_DETAILS);
  
        // Log in with valid credentials
        cy.get('input[formControlName=email]').type('yoga@studio.com');
        cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
  
   
        cy.url().should('include', '/sessions');
      });
  
   
      it('Performs the following actions:', () => {
        // * Visits the list of sessions and sees all active sessions
        cy.get('mat-card').should('have.length', 2);
        cy.get('mat-card-title').should('contain', TEST_SESSION.name);
  
        // * Creates a new session and adds it to the list
        cy.get('button[mat-raised-button] span').contains('Create').click();
        cy.get('input[formControlName="name"]').type(TEST_SESSION.name);
  
      
        const formattedDate: string = TEST_SESSION.date.split('T')[0];
  
        cy.get('input[formControlName="date"]').type(formattedDate);
        cy.get('mat-select[formControlName="teacher_id"]').click();
  
        cy.get('mat-option').contains(TEACHERS_LIST[0].firstName).click();
        cy.get('textarea[formControlName="description"]').type(
          TEST_SESSION.description
        );
  
   
        cy.get('button[mat-raised-button]').contains('Save').click();
  

   
        cy.get('snack-bar-container')
          .contains('Session created !')
          .should('exist');
        cy.get('snack-bar-container button span').contains('Close').click();
  
        cy.get('mat-card').should('have.length', 3);
  
        // * Edits a session and saves the changes
        cy.get('button[mat-raised-button] span').contains('Edit').click();
        cy.get('input[formControlName="name"]')
          .clear()
          .type('EDITED TEST session');
        cy.get('button[mat-raised-button]').contains('Save').click();
  
        cy.get('snack-bar-container')
          .contains('Session updated !')
          .should('exist');
        cy.get('snack-bar-container button span').contains('Close').click();
        cy.get('mat-card-title').should('contain', EDITED_TEST_SESSION.name);
  
        // * Deletes a session
        cy.get('button').contains('Detail').click();
        cy.get('button').contains('Delete').click();
  
        cy.get('snack-bar-container')
          .contains('Session deleted !')
          .should('exist');
        cy.get('snack-bar-container button span').contains('Close').click();
  
        cy.get('mat-card').should('have.length', 2);
      });
    });
    describe('As a regular user', () => {
        beforeEach(() => {
          cy.visit('/login');
    
          cy.intercept('POST', '/api/auth/login', USER_DETAILS);
    
          cy.get('input[formControlName=email]').type('user@user.com');
          cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    
         
          cy.intercept('GET', '/api/session', (req) => {
            req.reply(SESSIONS_LIST);
          });
    

    
          cy.url().should('include', '/sessions');
        });
    
        it('Performs the following actions:', () => {
          // * Navigate to the details page
    
          // ? Views session details and does not see the delete button
          // TODO: Navigate to the details page
          // TODO: Check for absence of delete button
          cy.get('button[mat-raised-button] span')
            .contains('Edit')
            .should('not.exist');
    
          cy.get('button[mat-raised-button] span').contains('Detail').click();
          cy.get('button').contains('Delete').should('not.exist');
          //
          // ? Enables and disables session participation
          // TODO: Navigate to the details page
          // TODO: Enable participation
          // TODO: Disable participation
          // TODO: Check that participation state changes appropriately
          // TODO: Check that we do not have the button to delete a session
          // TODO: Navigate back to the sessions page clicking the button
        });
      });
    });

    describe('Account page', () => {
        const ADMIN_DETAILS = {
          token: 'jwt',
          type: 'Bearer',
          id: 1,
          email: 'yoga@studio.com',
          firstName: 'Admin',
          lastName: 'ADMIN',
          admin: true,
          createdAt: '2024-06-12T15:33:42',
          updatedAt: '2024-06-12T15:33:42',
        };
        const USER_DETAILS = {
          token: 'jwt',
          type: 'Bearer',
          id: 2,
          email: 'user@user.com',
          firstName: 'User',
          lastName: 'USER',
          admin: false,
          createdAt: '2024-06-12T15:33:42',
          updatedAt: '2024-06-12T15:33:42',
        };
        beforeEach(() => {
          cy.intercept('GET', '/api/session', (req) => {
            req.reply([]);
          });
      
          cy.intercept('DELETE', '/api/user');
        });
      
        describe('As an admin', () => {
          beforeEach(() => {
            cy.visit('/login');
            cy.intercept('POST', '/api/auth/login', ADMIN_DETAILS);
            cy.intercept('GET', `/api/user/${ADMIN_DETAILS.id}`, (req) => {
              req.reply(ADMIN_DETAILS);
            });
            cy.get('input[formControlName=email]').type('yoga@studio.com');
            cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
            cy.url().should('include', '/sessions');
          });
          it('should show their info and NOT include a delete button', () => {
            cy.get('span.link').contains('Account').click();
      
            cy.url().should('include', '/me');
      
            // * We check that all the info matches
            cy.get('mat-card-title h1').should('contain', 'User information');
            cy.get('p')
              .eq(0)
              .should(
                'contain',
                `Name: ${ADMIN_DETAILS.firstName} ${ADMIN_DETAILS.lastName}`
              );
            cy.get('p').eq(1).should('contain', `Email: ${ADMIN_DETAILS.email}`);
            cy.get('p').eq(2).should('contain', `You are admin`);
            cy.get('p').eq(3).should('contain', `Create at:  June 12, 2024`);
            cy.get('p').eq(4).should('contain', `Last update:  June 12, 2024`);
      
            cy.get('button[mat-raised-button]').should('not.exist');
          });
        });
        describe('As a regular user', () => {
          beforeEach(() => {
            cy.visit('/login');
            cy.intercept('POST', '/api/auth/login', USER_DETAILS);
            cy.intercept('GET', `/api/user/${USER_DETAILS.id}`, (req) => {
              req.reply(USER_DETAILS);
            });
            cy.get('input[formControlName=email]').type('user@user.com');
            cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
            cy.url().should('include', '/sessions');
          });
          it('should show their info and include a delete button', () => {
            cy.get('span.link').contains('Account').click();
      
            cy.url().should('include', '/me');
      
            cy.get('mat-card-title h1').should('contain', 'User information');
            cy.get('p')
              .eq(0)
              .should(
                'contain',
                `Name: ${USER_DETAILS.firstName} ${USER_DETAILS.lastName}`
              );
            cy.get('p').eq(1).should('contain', `Email: ${USER_DETAILS.email}`);
            cy.get('p').eq(2).should('contain', `Delete my account:`);
            cy.get('p').eq(3).should('contain', `Create at:  June 12, 2024`);
            cy.get('p').eq(4).should('contain', `Last update:  June 12, 2024`);
      
            cy.get('button[mat-raised-button]').should('exist');
          });
        });
      });



describe('Register page', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('Should create account with success', () => {
    cy.intercept('POST', '/api/auth/register', {}).as('register');

    cy.get('mat-card-title').should('be.visible');
    cy.contains('First name').should('be.visible');
    cy.contains('Last name').should('be.visible');
    cy.contains('Email').should('be.visible');
    cy.contains('Password').should('be.visible');
    cy.contains('Submit').should('be.visible');

    cy.get(firstNameInput).type('Alioune');
    cy.get(lastNameInput).type('Beye');
    cy.get(emailInput).type('ali@test.com');
    cy.get(password).type('test!1234');

    cy.get(buttonRegister).click();

    cy.url().should('include', '/login');
  });

  it('should show error for a required field not properly filled', () => {
    cy.get(firstNameInput).type('Test');
    cy.get(lastNameInput).type('TEST');
    cy.get(emailInput).type('invalid');

    cy.get(password).type('test!1234');

    cy.get(buttonRegister).then(($buttons) => {
      const disabledButtonsCount = $buttons.filter(':disabled').length;
      expect(disabledButtonsCount).to.equal(1);
    });
  });
});