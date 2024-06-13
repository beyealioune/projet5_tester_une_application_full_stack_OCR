describe('Homepage spec', () => {
    it('should display Yoga app on the homepage', () => {
      cy.visit('http://localhost:4200');
      cy.contains('Yoga app').should('be.visible');
    });
  });
  