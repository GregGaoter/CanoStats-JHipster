import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MouvementsStock from './mouvements-stock';
import MouvementsStockDetail from './mouvements-stock-detail';
import MouvementsStockUpdate from './mouvements-stock-update';
import MouvementsStockDeleteDialog from './mouvements-stock-delete-dialog';

const MouvementsStockRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MouvementsStock />} />
    <Route path="new" element={<MouvementsStockUpdate />} />
    <Route path=":id">
      <Route index element={<MouvementsStockDetail />} />
      <Route path="edit" element={<MouvementsStockUpdate />} />
      <Route path="delete" element={<MouvementsStockDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MouvementsStockRoutes;
