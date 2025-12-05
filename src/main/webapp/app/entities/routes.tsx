import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MouvementsStock from './mouvements-stock';
import Produit from './produit';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="mouvements-stock/*" element={<MouvementsStock />} />
        <Route path="produit/*" element={<Produit />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
