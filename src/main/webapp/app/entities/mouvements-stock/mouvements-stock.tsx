import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './mouvements-stock.reducer';

export const MouvementsStock = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const mouvementsStockList = useAppSelector(state => state.mouvementsStock.entities);
  const loading = useAppSelector(state => state.mouvementsStock.loading);
  const totalItems = useAppSelector(state => state.mouvementsStock.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="mouvements-stock-heading" data-cy="MouvementsStockHeading">
        Mouvements Stocks
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/mouvements-stock/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Mouvements Stock
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {mouvementsStockList && mouvementsStockList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  Id <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('epicerioId')}>
                  Epicerio Id <FontAwesomeIcon icon={getSortIconByFieldName('epicerioId')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  Created Date <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedDate')}>
                  Last Updated Date <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedDate')} />
                </th>
                <th className="hand" onClick={sort('importedDate')}>
                  Imported Date <FontAwesomeIcon icon={getSortIconByFieldName('importedDate')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  Date <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th className="hand" onClick={sort('utilisateur')}>
                  Utilisateur <FontAwesomeIcon icon={getSortIconByFieldName('utilisateur')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  Type <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('epicerioMouvement')}>
                  Epicerio Mouvement <FontAwesomeIcon icon={getSortIconByFieldName('epicerioMouvement')} />
                </th>
                <th className="hand" onClick={sort('mouvement')}>
                  Mouvement <FontAwesomeIcon icon={getSortIconByFieldName('mouvement')} />
                </th>
                <th className="hand" onClick={sort('solde')}>
                  Solde <FontAwesomeIcon icon={getSortIconByFieldName('solde')} />
                </th>
                <th className="hand" onClick={sort('vente')}>
                  Vente <FontAwesomeIcon icon={getSortIconByFieldName('vente')} />
                </th>
                <th className="hand" onClick={sort('codeProduit')}>
                  Code Produit <FontAwesomeIcon icon={getSortIconByFieldName('codeProduit')} />
                </th>
                <th className="hand" onClick={sort('produit')}>
                  Produit <FontAwesomeIcon icon={getSortIconByFieldName('produit')} />
                </th>
                <th className="hand" onClick={sort('responsableProduit')}>
                  Responsable Produit <FontAwesomeIcon icon={getSortIconByFieldName('responsableProduit')} />
                </th>
                <th className="hand" onClick={sort('fournisseurProduit')}>
                  Fournisseur Produit <FontAwesomeIcon icon={getSortIconByFieldName('fournisseurProduit')} />
                </th>
                <th className="hand" onClick={sort('codeFournisseur')}>
                  Code Fournisseur <FontAwesomeIcon icon={getSortIconByFieldName('codeFournisseur')} />
                </th>
                <th className="hand" onClick={sort('reduction')}>
                  Reduction <FontAwesomeIcon icon={getSortIconByFieldName('reduction')} />
                </th>
                <th className="hand" onClick={sort('ponderation')}>
                  Ponderation <FontAwesomeIcon icon={getSortIconByFieldName('ponderation')} />
                </th>
                <th className="hand" onClick={sort('venteChf')}>
                  Vente Chf <FontAwesomeIcon icon={getSortIconByFieldName('venteChf')} />
                </th>
                <th className="hand" onClick={sort('valeurChf')}>
                  Valeur Chf <FontAwesomeIcon icon={getSortIconByFieldName('valeurChf')} />
                </th>
                <th className="hand" onClick={sort('remarques')}>
                  Remarques <FontAwesomeIcon icon={getSortIconByFieldName('remarques')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  Active <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {mouvementsStockList.map((mouvementsStock, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/mouvements-stock/${mouvementsStock.id}`} color="link" size="sm">
                      {mouvementsStock.id}
                    </Button>
                  </td>
                  <td>{mouvementsStock.epicerioId}</td>
                  <td>
                    {mouvementsStock.createdDate ? (
                      <TextFormat type="date" value={mouvementsStock.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {mouvementsStock.lastUpdatedDate ? (
                      <TextFormat type="date" value={mouvementsStock.lastUpdatedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {mouvementsStock.importedDate ? (
                      <TextFormat type="date" value={mouvementsStock.importedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{mouvementsStock.date ? <TextFormat type="date" value={mouvementsStock.date} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{mouvementsStock.utilisateur}</td>
                  <td>{mouvementsStock.type}</td>
                  <td>{mouvementsStock.epicerioMouvement}</td>
                  <td>{mouvementsStock.mouvement}</td>
                  <td>{mouvementsStock.solde}</td>
                  <td>{mouvementsStock.vente}</td>
                  <td>{mouvementsStock.codeProduit}</td>
                  <td>{mouvementsStock.produit}</td>
                  <td>{mouvementsStock.responsableProduit}</td>
                  <td>{mouvementsStock.fournisseurProduit}</td>
                  <td>{mouvementsStock.codeFournisseur}</td>
                  <td>{mouvementsStock.reduction}</td>
                  <td>{mouvementsStock.ponderation}</td>
                  <td>{mouvementsStock.venteChf}</td>
                  <td>{mouvementsStock.valeurChf}</td>
                  <td>{mouvementsStock.remarques}</td>
                  <td>{mouvementsStock.active ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/mouvements-stock/${mouvementsStock.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/mouvements-stock/${mouvementsStock.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/mouvements-stock/${mouvementsStock.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Mouvements Stocks found</div>
        )}
      </div>
      {totalItems ? (
        <div className={mouvementsStockList && mouvementsStockList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default MouvementsStock;
