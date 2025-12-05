package ch.epicerielacanopee.statistics.web.rest;

import static ch.epicerielacanopee.statistics.domain.ProduitAsserts.*;
import static ch.epicerielacanopee.statistics.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.epicerielacanopee.statistics.IntegrationTest;
import ch.epicerielacanopee.statistics.domain.Produit;
import ch.epicerielacanopee.statistics.repository.ProduitRepository;
import ch.epicerielacanopee.statistics.service.dto.ProduitDTO;
import ch.epicerielacanopee.statistics.service.mapper.ProduitMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProduitResourceIT {

    private static final Integer DEFAULT_EPICERIO_ID = 1;
    private static final Integer UPDATED_EPICERIO_ID = 2;
    private static final Integer SMALLER_EPICERIO_ID = 1 - 1;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_IMPORTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IMPORTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DISPONIBLE = "AAAAAAAAAA";
    private static final String UPDATED_DISPONIBLE = "BBBBBBBBBB";

    private static final Float DEFAULT_PRIX_FOURNISSEUR = 1F;
    private static final Float UPDATED_PRIX_FOURNISSEUR = 2F;
    private static final Float SMALLER_PRIX_FOURNISSEUR = 1F - 1F;

    private static final String DEFAULT_HT_TTC = "AAAAAAAAAA";
    private static final String UPDATED_HT_TTC = "BBBBBBBBBB";

    private static final Float DEFAULT_TAUX_TVA = 1F;
    private static final Float UPDATED_TAUX_TVA = 2F;
    private static final Float SMALLER_TAUX_TVA = 1F - 1F;

    private static final Float DEFAULT_MARGE_PROFIT = 1F;
    private static final Float UPDATED_MARGE_PROFIT = 2F;
    private static final Float SMALLER_MARGE_PROFIT = 1F - 1F;

    private static final Float DEFAULT_PRIX_VENTE = 1F;
    private static final Float UPDATED_PRIX_VENTE = 2F;
    private static final Float SMALLER_PRIX_VENTE = 1F - 1F;

    private static final String DEFAULT_VENDU = "AAAAAAAAAA";
    private static final String UPDATED_VENDU = "BBBBBBBBBB";

    private static final Float DEFAULT_QUANTITE_PAR_PIECE = 1F;
    private static final Float UPDATED_QUANTITE_PAR_PIECE = 2F;
    private static final Float SMALLER_QUANTITE_PAR_PIECE = 1F - 1F;

    private static final String DEFAULT_UNITE = "AAAAAAAAAA";
    private static final String UPDATED_UNITE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIX_PAR_UNITE = "AAAAAAAAAA";
    private static final String UPDATED_PRIX_PAR_UNITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARQUES_INTERNES = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUES_INTERNES = "BBBBBBBBBB";

    private static final String DEFAULT_FOURNISSEUR = "AAAAAAAAAA";
    private static final String UPDATED_FOURNISSEUR = "BBBBBBBBBB";

    private static final String DEFAULT_REF_FOURNISSEUR = "AAAAAAAAAA";
    private static final String UPDATED_REF_FOURNISSEUR = "BBBBBBBBBB";

    private static final Float DEFAULT_STOCK = 1F;
    private static final Float UPDATED_STOCK = 2F;
    private static final Float SMALLER_STOCK = 1F - 1F;

    private static final Float DEFAULT_COMMANDES_CLIENTS = 1F;
    private static final Float UPDATED_COMMANDES_CLIENTS = 2F;
    private static final Float SMALLER_COMMANDES_CLIENTS = 1F - 1F;

    private static final LocalDate DEFAULT_DERNIERE_VERIFICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DERNIERE_VERIFICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DERNIERE_VERIFICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DERNIERE_LIVRAISON_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DERNIERE_LIVRAISON_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DERNIERE_LIVRAISON_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ACHAT_FOURNISSEUR = "AAAAAAAAAA";
    private static final String UPDATED_ACHAT_FOURNISSEUR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DERNIER_ACHAT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DERNIER_ACHAT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DERNIER_ACHAT_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_DERNIER_ACHAT_QUANTITE = 1F;
    private static final Float UPDATED_DERNIER_ACHAT_QUANTITE = 2F;
    private static final Float SMALLER_DERNIER_ACHAT_QUANTITE = 1F - 1F;

    private static final Float DEFAULT_STATS_LIVRAISON = 1F;
    private static final Float UPDATED_STATS_LIVRAISON = 2F;
    private static final Float SMALLER_STATS_LIVRAISON = 1F - 1F;

    private static final Float DEFAULT_STATS_PERTE = 1F;
    private static final Float UPDATED_STATS_PERTE = 2F;
    private static final Float SMALLER_STATS_PERTE = 1F - 1F;

    private static final Float DEFAULT_STATS_VENTE = 1F;
    private static final Float UPDATED_STATS_VENTE = 2F;
    private static final Float SMALLER_STATS_VENTE = 1F - 1F;

    private static final Float DEFAULT_STATS_VENTE_SPECIALE = 1F;
    private static final Float UPDATED_STATS_VENTE_SPECIALE = 2F;
    private static final Float SMALLER_STATS_VENTE_SPECIALE = 1F - 1F;

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private ProduitMapper produitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProduitMockMvc;

    private Produit produit;

    private Produit insertedProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createEntity() {
        return new Produit()
            .epicerioId(DEFAULT_EPICERIO_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
            .importedDate(DEFAULT_IMPORTED_DATE)
            .nom(DEFAULT_NOM)
            .code(DEFAULT_CODE)
            .disponible(DEFAULT_DISPONIBLE)
            .prixFournisseur(DEFAULT_PRIX_FOURNISSEUR)
            .htTtc(DEFAULT_HT_TTC)
            .tauxTva(DEFAULT_TAUX_TVA)
            .margeProfit(DEFAULT_MARGE_PROFIT)
            .prixVente(DEFAULT_PRIX_VENTE)
            .vendu(DEFAULT_VENDU)
            .quantiteParPiece(DEFAULT_QUANTITE_PAR_PIECE)
            .unite(DEFAULT_UNITE)
            .prixParUnite(DEFAULT_PRIX_PAR_UNITE)
            .description(DEFAULT_DESCRIPTION)
            .remarquesInternes(DEFAULT_REMARQUES_INTERNES)
            .fournisseur(DEFAULT_FOURNISSEUR)
            .refFournisseur(DEFAULT_REF_FOURNISSEUR)
            .stock(DEFAULT_STOCK)
            .commandesClients(DEFAULT_COMMANDES_CLIENTS)
            .derniereVerificationDate(DEFAULT_DERNIERE_VERIFICATION_DATE)
            .derniereLivraisonDate(DEFAULT_DERNIERE_LIVRAISON_DATE)
            .achatFournisseur(DEFAULT_ACHAT_FOURNISSEUR)
            .dernierAchatDate(DEFAULT_DERNIER_ACHAT_DATE)
            .dernierAchatQuantite(DEFAULT_DERNIER_ACHAT_QUANTITE)
            .statsLivraison(DEFAULT_STATS_LIVRAISON)
            .statsPerte(DEFAULT_STATS_PERTE)
            .statsVente(DEFAULT_STATS_VENTE)
            .statsVenteSpeciale(DEFAULT_STATS_VENTE_SPECIALE)
            .tags(DEFAULT_TAGS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createUpdatedEntity() {
        return new Produit()
            .epicerioId(UPDATED_EPICERIO_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .importedDate(UPDATED_IMPORTED_DATE)
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .disponible(UPDATED_DISPONIBLE)
            .prixFournisseur(UPDATED_PRIX_FOURNISSEUR)
            .htTtc(UPDATED_HT_TTC)
            .tauxTva(UPDATED_TAUX_TVA)
            .margeProfit(UPDATED_MARGE_PROFIT)
            .prixVente(UPDATED_PRIX_VENTE)
            .vendu(UPDATED_VENDU)
            .quantiteParPiece(UPDATED_QUANTITE_PAR_PIECE)
            .unite(UPDATED_UNITE)
            .prixParUnite(UPDATED_PRIX_PAR_UNITE)
            .description(UPDATED_DESCRIPTION)
            .remarquesInternes(UPDATED_REMARQUES_INTERNES)
            .fournisseur(UPDATED_FOURNISSEUR)
            .refFournisseur(UPDATED_REF_FOURNISSEUR)
            .stock(UPDATED_STOCK)
            .commandesClients(UPDATED_COMMANDES_CLIENTS)
            .derniereVerificationDate(UPDATED_DERNIERE_VERIFICATION_DATE)
            .derniereLivraisonDate(UPDATED_DERNIERE_LIVRAISON_DATE)
            .achatFournisseur(UPDATED_ACHAT_FOURNISSEUR)
            .dernierAchatDate(UPDATED_DERNIER_ACHAT_DATE)
            .dernierAchatQuantite(UPDATED_DERNIER_ACHAT_QUANTITE)
            .statsLivraison(UPDATED_STATS_LIVRAISON)
            .statsPerte(UPDATED_STATS_PERTE)
            .statsVente(UPDATED_STATS_VENTE)
            .statsVenteSpeciale(UPDATED_STATS_VENTE_SPECIALE)
            .tags(UPDATED_TAGS);
    }

    @BeforeEach
    public void initTest() {
        produit = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduit != null) {
            produitRepository.delete(insertedProduit);
            insertedProduit = null;
        }
    }

    @Test
    @Transactional
    void createProduit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);
        var returnedProduitDTO = om.readValue(
            restProduitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProduitDTO.class
        );

        // Validate the Produit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduit = produitMapper.toEntity(returnedProduitDTO);
        assertProduitUpdatableFieldsEquals(returnedProduit, getPersistedProduit(returnedProduit));

        insertedProduit = returnedProduit;
    }

    @Test
    @Transactional
    void createProduitWithExistingId() throws Exception {
        // Create the Produit with an existing ID
        insertedProduit = produitRepository.saveAndFlush(produit);
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produit.setCreatedDate(null);

        // Create the Produit, which fails.
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produit.setLastUpdatedDate(null);

        // Create the Produit, which fails.
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImportedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produit.setImportedDate(null);

        // Create the Produit, which fails.
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        restProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProduits() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList
        restProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().toString())))
            .andExpect(jsonPath("$.[*].epicerioId").value(hasItem(DEFAULT_EPICERIO_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].importedDate").value(hasItem(DEFAULT_IMPORTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].disponible").value(hasItem(DEFAULT_DISPONIBLE)))
            .andExpect(jsonPath("$.[*].prixFournisseur").value(hasItem(DEFAULT_PRIX_FOURNISSEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].htTtc").value(hasItem(DEFAULT_HT_TTC)))
            .andExpect(jsonPath("$.[*].tauxTva").value(hasItem(DEFAULT_TAUX_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].margeProfit").value(hasItem(DEFAULT_MARGE_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.doubleValue())))
            .andExpect(jsonPath("$.[*].vendu").value(hasItem(DEFAULT_VENDU)))
            .andExpect(jsonPath("$.[*].quantiteParPiece").value(hasItem(DEFAULT_QUANTITE_PAR_PIECE.doubleValue())))
            .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE)))
            .andExpect(jsonPath("$.[*].prixParUnite").value(hasItem(DEFAULT_PRIX_PAR_UNITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].remarquesInternes").value(hasItem(DEFAULT_REMARQUES_INTERNES.toString())))
            .andExpect(jsonPath("$.[*].fournisseur").value(hasItem(DEFAULT_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].refFournisseur").value(hasItem(DEFAULT_REF_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.doubleValue())))
            .andExpect(jsonPath("$.[*].commandesClients").value(hasItem(DEFAULT_COMMANDES_CLIENTS.doubleValue())))
            .andExpect(jsonPath("$.[*].derniereVerificationDate").value(hasItem(DEFAULT_DERNIERE_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].derniereLivraisonDate").value(hasItem(DEFAULT_DERNIERE_LIVRAISON_DATE.toString())))
            .andExpect(jsonPath("$.[*].achatFournisseur").value(hasItem(DEFAULT_ACHAT_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].dernierAchatDate").value(hasItem(DEFAULT_DERNIER_ACHAT_DATE.toString())))
            .andExpect(jsonPath("$.[*].dernierAchatQuantite").value(hasItem(DEFAULT_DERNIER_ACHAT_QUANTITE.doubleValue())))
            .andExpect(jsonPath("$.[*].statsLivraison").value(hasItem(DEFAULT_STATS_LIVRAISON.doubleValue())))
            .andExpect(jsonPath("$.[*].statsPerte").value(hasItem(DEFAULT_STATS_PERTE.doubleValue())))
            .andExpect(jsonPath("$.[*].statsVente").value(hasItem(DEFAULT_STATS_VENTE.doubleValue())))
            .andExpect(jsonPath("$.[*].statsVenteSpeciale").value(hasItem(DEFAULT_STATS_VENTE_SPECIALE.doubleValue())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())));
    }

    @Test
    @Transactional
    void getProduit() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get the produit
        restProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produit.getId().toString()))
            .andExpect(jsonPath("$.epicerioId").value(DEFAULT_EPICERIO_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(DEFAULT_LAST_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.importedDate").value(DEFAULT_IMPORTED_DATE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.disponible").value(DEFAULT_DISPONIBLE))
            .andExpect(jsonPath("$.prixFournisseur").value(DEFAULT_PRIX_FOURNISSEUR.doubleValue()))
            .andExpect(jsonPath("$.htTtc").value(DEFAULT_HT_TTC))
            .andExpect(jsonPath("$.tauxTva").value(DEFAULT_TAUX_TVA.doubleValue()))
            .andExpect(jsonPath("$.margeProfit").value(DEFAULT_MARGE_PROFIT.doubleValue()))
            .andExpect(jsonPath("$.prixVente").value(DEFAULT_PRIX_VENTE.doubleValue()))
            .andExpect(jsonPath("$.vendu").value(DEFAULT_VENDU))
            .andExpect(jsonPath("$.quantiteParPiece").value(DEFAULT_QUANTITE_PAR_PIECE.doubleValue()))
            .andExpect(jsonPath("$.unite").value(DEFAULT_UNITE))
            .andExpect(jsonPath("$.prixParUnite").value(DEFAULT_PRIX_PAR_UNITE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.remarquesInternes").value(DEFAULT_REMARQUES_INTERNES.toString()))
            .andExpect(jsonPath("$.fournisseur").value(DEFAULT_FOURNISSEUR))
            .andExpect(jsonPath("$.refFournisseur").value(DEFAULT_REF_FOURNISSEUR))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK.doubleValue()))
            .andExpect(jsonPath("$.commandesClients").value(DEFAULT_COMMANDES_CLIENTS.doubleValue()))
            .andExpect(jsonPath("$.derniereVerificationDate").value(DEFAULT_DERNIERE_VERIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.derniereLivraisonDate").value(DEFAULT_DERNIERE_LIVRAISON_DATE.toString()))
            .andExpect(jsonPath("$.achatFournisseur").value(DEFAULT_ACHAT_FOURNISSEUR))
            .andExpect(jsonPath("$.dernierAchatDate").value(DEFAULT_DERNIER_ACHAT_DATE.toString()))
            .andExpect(jsonPath("$.dernierAchatQuantite").value(DEFAULT_DERNIER_ACHAT_QUANTITE.doubleValue()))
            .andExpect(jsonPath("$.statsLivraison").value(DEFAULT_STATS_LIVRAISON.doubleValue()))
            .andExpect(jsonPath("$.statsPerte").value(DEFAULT_STATS_PERTE.doubleValue()))
            .andExpect(jsonPath("$.statsVente").value(DEFAULT_STATS_VENTE.doubleValue()))
            .andExpect(jsonPath("$.statsVenteSpeciale").value(DEFAULT_STATS_VENTE_SPECIALE.doubleValue()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()));
    }

    @Test
    @Transactional
    void getProduitsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        UUID id = produit.getId();

        defaultProduitFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllProduitsByEpicerioIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where epicerioId equals to
        defaultProduitFiltering("epicerioId.equals=" + DEFAULT_EPICERIO_ID, "epicerioId.equals=" + UPDATED_EPICERIO_ID);
    }

    @Test
    @Transactional
    void getAllProduitsByEpicerioIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where epicerioId in
        defaultProduitFiltering("epicerioId.in=" + DEFAULT_EPICERIO_ID + "," + UPDATED_EPICERIO_ID, "epicerioId.in=" + UPDATED_EPICERIO_ID);
    }

    @Test
    @Transactional
    void getAllProduitsByEpicerioIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where epicerioId is not null
        defaultProduitFiltering("epicerioId.specified=true", "epicerioId.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByEpicerioIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where epicerioId is greater than or equal to
        defaultProduitFiltering(
            "epicerioId.greaterThanOrEqual=" + DEFAULT_EPICERIO_ID,
            "epicerioId.greaterThanOrEqual=" + UPDATED_EPICERIO_ID
        );
    }

    @Test
    @Transactional
    void getAllProduitsByEpicerioIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where epicerioId is less than or equal to
        defaultProduitFiltering("epicerioId.lessThanOrEqual=" + DEFAULT_EPICERIO_ID, "epicerioId.lessThanOrEqual=" + SMALLER_EPICERIO_ID);
    }

    @Test
    @Transactional
    void getAllProduitsByEpicerioIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where epicerioId is less than
        defaultProduitFiltering("epicerioId.lessThan=" + UPDATED_EPICERIO_ID, "epicerioId.lessThan=" + DEFAULT_EPICERIO_ID);
    }

    @Test
    @Transactional
    void getAllProduitsByEpicerioIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where epicerioId is greater than
        defaultProduitFiltering("epicerioId.greaterThan=" + SMALLER_EPICERIO_ID, "epicerioId.greaterThan=" + DEFAULT_EPICERIO_ID);
    }

    @Test
    @Transactional
    void getAllProduitsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where createdDate equals to
        defaultProduitFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProduitsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where createdDate in
        defaultProduitFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where createdDate is not null
        defaultProduitFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByLastUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where lastUpdatedDate equals to
        defaultProduitFiltering(
            "lastUpdatedDate.equals=" + DEFAULT_LAST_UPDATED_DATE,
            "lastUpdatedDate.equals=" + UPDATED_LAST_UPDATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByLastUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where lastUpdatedDate in
        defaultProduitFiltering(
            "lastUpdatedDate.in=" + DEFAULT_LAST_UPDATED_DATE + "," + UPDATED_LAST_UPDATED_DATE,
            "lastUpdatedDate.in=" + UPDATED_LAST_UPDATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByLastUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where lastUpdatedDate is not null
        defaultProduitFiltering("lastUpdatedDate.specified=true", "lastUpdatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByImportedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where importedDate equals to
        defaultProduitFiltering("importedDate.equals=" + DEFAULT_IMPORTED_DATE, "importedDate.equals=" + UPDATED_IMPORTED_DATE);
    }

    @Test
    @Transactional
    void getAllProduitsByImportedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where importedDate in
        defaultProduitFiltering(
            "importedDate.in=" + DEFAULT_IMPORTED_DATE + "," + UPDATED_IMPORTED_DATE,
            "importedDate.in=" + UPDATED_IMPORTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByImportedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where importedDate is not null
        defaultProduitFiltering("importedDate.specified=true", "importedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom equals to
        defaultProduitFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProduitsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom in
        defaultProduitFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProduitsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom is not null
        defaultProduitFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom contains
        defaultProduitFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllProduitsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom does not contain
        defaultProduitFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllProduitsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where code equals to
        defaultProduitFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProduitsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where code in
        defaultProduitFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProduitsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where code is not null
        defaultProduitFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where code contains
        defaultProduitFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProduitsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where code does not contain
        defaultProduitFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllProduitsByDisponibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where disponible equals to
        defaultProduitFiltering("disponible.equals=" + DEFAULT_DISPONIBLE, "disponible.equals=" + UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProduitsByDisponibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where disponible in
        defaultProduitFiltering("disponible.in=" + DEFAULT_DISPONIBLE + "," + UPDATED_DISPONIBLE, "disponible.in=" + UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProduitsByDisponibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where disponible is not null
        defaultProduitFiltering("disponible.specified=true", "disponible.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByDisponibleContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where disponible contains
        defaultProduitFiltering("disponible.contains=" + DEFAULT_DISPONIBLE, "disponible.contains=" + UPDATED_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProduitsByDisponibleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where disponible does not contain
        defaultProduitFiltering("disponible.doesNotContain=" + UPDATED_DISPONIBLE, "disponible.doesNotContain=" + DEFAULT_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixFournisseur equals to
        defaultProduitFiltering("prixFournisseur.equals=" + DEFAULT_PRIX_FOURNISSEUR, "prixFournisseur.equals=" + UPDATED_PRIX_FOURNISSEUR);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixFournisseurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixFournisseur in
        defaultProduitFiltering(
            "prixFournisseur.in=" + DEFAULT_PRIX_FOURNISSEUR + "," + UPDATED_PRIX_FOURNISSEUR,
            "prixFournisseur.in=" + UPDATED_PRIX_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByPrixFournisseurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixFournisseur is not null
        defaultProduitFiltering("prixFournisseur.specified=true", "prixFournisseur.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByPrixFournisseurIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixFournisseur is greater than or equal to
        defaultProduitFiltering(
            "prixFournisseur.greaterThanOrEqual=" + DEFAULT_PRIX_FOURNISSEUR,
            "prixFournisseur.greaterThanOrEqual=" + UPDATED_PRIX_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByPrixFournisseurIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixFournisseur is less than or equal to
        defaultProduitFiltering(
            "prixFournisseur.lessThanOrEqual=" + DEFAULT_PRIX_FOURNISSEUR,
            "prixFournisseur.lessThanOrEqual=" + SMALLER_PRIX_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByPrixFournisseurIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixFournisseur is less than
        defaultProduitFiltering(
            "prixFournisseur.lessThan=" + UPDATED_PRIX_FOURNISSEUR,
            "prixFournisseur.lessThan=" + DEFAULT_PRIX_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByPrixFournisseurIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixFournisseur is greater than
        defaultProduitFiltering(
            "prixFournisseur.greaterThan=" + SMALLER_PRIX_FOURNISSEUR,
            "prixFournisseur.greaterThan=" + DEFAULT_PRIX_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByHtTtcIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where htTtc equals to
        defaultProduitFiltering("htTtc.equals=" + DEFAULT_HT_TTC, "htTtc.equals=" + UPDATED_HT_TTC);
    }

    @Test
    @Transactional
    void getAllProduitsByHtTtcIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where htTtc in
        defaultProduitFiltering("htTtc.in=" + DEFAULT_HT_TTC + "," + UPDATED_HT_TTC, "htTtc.in=" + UPDATED_HT_TTC);
    }

    @Test
    @Transactional
    void getAllProduitsByHtTtcIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where htTtc is not null
        defaultProduitFiltering("htTtc.specified=true", "htTtc.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByHtTtcContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where htTtc contains
        defaultProduitFiltering("htTtc.contains=" + DEFAULT_HT_TTC, "htTtc.contains=" + UPDATED_HT_TTC);
    }

    @Test
    @Transactional
    void getAllProduitsByHtTtcNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where htTtc does not contain
        defaultProduitFiltering("htTtc.doesNotContain=" + UPDATED_HT_TTC, "htTtc.doesNotContain=" + DEFAULT_HT_TTC);
    }

    @Test
    @Transactional
    void getAllProduitsByTauxTvaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva equals to
        defaultProduitFiltering("tauxTva.equals=" + DEFAULT_TAUX_TVA, "tauxTva.equals=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllProduitsByTauxTvaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva in
        defaultProduitFiltering("tauxTva.in=" + DEFAULT_TAUX_TVA + "," + UPDATED_TAUX_TVA, "tauxTva.in=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllProduitsByTauxTvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is not null
        defaultProduitFiltering("tauxTva.specified=true", "tauxTva.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByTauxTvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is greater than or equal to
        defaultProduitFiltering("tauxTva.greaterThanOrEqual=" + DEFAULT_TAUX_TVA, "tauxTva.greaterThanOrEqual=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllProduitsByTauxTvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is less than or equal to
        defaultProduitFiltering("tauxTva.lessThanOrEqual=" + DEFAULT_TAUX_TVA, "tauxTva.lessThanOrEqual=" + SMALLER_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllProduitsByTauxTvaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is less than
        defaultProduitFiltering("tauxTva.lessThan=" + UPDATED_TAUX_TVA, "tauxTva.lessThan=" + DEFAULT_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllProduitsByTauxTvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is greater than
        defaultProduitFiltering("tauxTva.greaterThan=" + SMALLER_TAUX_TVA, "tauxTva.greaterThan=" + DEFAULT_TAUX_TVA);
    }

    @Test
    @Transactional
    void getAllProduitsByMargeProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where margeProfit equals to
        defaultProduitFiltering("margeProfit.equals=" + DEFAULT_MARGE_PROFIT, "margeProfit.equals=" + UPDATED_MARGE_PROFIT);
    }

    @Test
    @Transactional
    void getAllProduitsByMargeProfitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where margeProfit in
        defaultProduitFiltering(
            "margeProfit.in=" + DEFAULT_MARGE_PROFIT + "," + UPDATED_MARGE_PROFIT,
            "margeProfit.in=" + UPDATED_MARGE_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllProduitsByMargeProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where margeProfit is not null
        defaultProduitFiltering("margeProfit.specified=true", "margeProfit.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByMargeProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where margeProfit is greater than or equal to
        defaultProduitFiltering(
            "margeProfit.greaterThanOrEqual=" + DEFAULT_MARGE_PROFIT,
            "margeProfit.greaterThanOrEqual=" + UPDATED_MARGE_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllProduitsByMargeProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where margeProfit is less than or equal to
        defaultProduitFiltering(
            "margeProfit.lessThanOrEqual=" + DEFAULT_MARGE_PROFIT,
            "margeProfit.lessThanOrEqual=" + SMALLER_MARGE_PROFIT
        );
    }

    @Test
    @Transactional
    void getAllProduitsByMargeProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where margeProfit is less than
        defaultProduitFiltering("margeProfit.lessThan=" + UPDATED_MARGE_PROFIT, "margeProfit.lessThan=" + DEFAULT_MARGE_PROFIT);
    }

    @Test
    @Transactional
    void getAllProduitsByMargeProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where margeProfit is greater than
        defaultProduitFiltering("margeProfit.greaterThan=" + SMALLER_MARGE_PROFIT, "margeProfit.greaterThan=" + DEFAULT_MARGE_PROFIT);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixVenteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVente equals to
        defaultProduitFiltering("prixVente.equals=" + DEFAULT_PRIX_VENTE, "prixVente.equals=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixVenteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVente in
        defaultProduitFiltering("prixVente.in=" + DEFAULT_PRIX_VENTE + "," + UPDATED_PRIX_VENTE, "prixVente.in=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixVenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVente is not null
        defaultProduitFiltering("prixVente.specified=true", "prixVente.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByPrixVenteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVente is greater than or equal to
        defaultProduitFiltering("prixVente.greaterThanOrEqual=" + DEFAULT_PRIX_VENTE, "prixVente.greaterThanOrEqual=" + UPDATED_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixVenteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVente is less than or equal to
        defaultProduitFiltering("prixVente.lessThanOrEqual=" + DEFAULT_PRIX_VENTE, "prixVente.lessThanOrEqual=" + SMALLER_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixVenteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVente is less than
        defaultProduitFiltering("prixVente.lessThan=" + UPDATED_PRIX_VENTE, "prixVente.lessThan=" + DEFAULT_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixVenteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVente is greater than
        defaultProduitFiltering("prixVente.greaterThan=" + SMALLER_PRIX_VENTE, "prixVente.greaterThan=" + DEFAULT_PRIX_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByVenduIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where vendu equals to
        defaultProduitFiltering("vendu.equals=" + DEFAULT_VENDU, "vendu.equals=" + UPDATED_VENDU);
    }

    @Test
    @Transactional
    void getAllProduitsByVenduIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where vendu in
        defaultProduitFiltering("vendu.in=" + DEFAULT_VENDU + "," + UPDATED_VENDU, "vendu.in=" + UPDATED_VENDU);
    }

    @Test
    @Transactional
    void getAllProduitsByVenduIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where vendu is not null
        defaultProduitFiltering("vendu.specified=true", "vendu.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByVenduContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where vendu contains
        defaultProduitFiltering("vendu.contains=" + DEFAULT_VENDU, "vendu.contains=" + UPDATED_VENDU);
    }

    @Test
    @Transactional
    void getAllProduitsByVenduNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where vendu does not contain
        defaultProduitFiltering("vendu.doesNotContain=" + UPDATED_VENDU, "vendu.doesNotContain=" + DEFAULT_VENDU);
    }

    @Test
    @Transactional
    void getAllProduitsByQuantiteParPieceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where quantiteParPiece equals to
        defaultProduitFiltering(
            "quantiteParPiece.equals=" + DEFAULT_QUANTITE_PAR_PIECE,
            "quantiteParPiece.equals=" + UPDATED_QUANTITE_PAR_PIECE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByQuantiteParPieceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where quantiteParPiece in
        defaultProduitFiltering(
            "quantiteParPiece.in=" + DEFAULT_QUANTITE_PAR_PIECE + "," + UPDATED_QUANTITE_PAR_PIECE,
            "quantiteParPiece.in=" + UPDATED_QUANTITE_PAR_PIECE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByQuantiteParPieceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where quantiteParPiece is not null
        defaultProduitFiltering("quantiteParPiece.specified=true", "quantiteParPiece.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByQuantiteParPieceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where quantiteParPiece is greater than or equal to
        defaultProduitFiltering(
            "quantiteParPiece.greaterThanOrEqual=" + DEFAULT_QUANTITE_PAR_PIECE,
            "quantiteParPiece.greaterThanOrEqual=" + UPDATED_QUANTITE_PAR_PIECE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByQuantiteParPieceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where quantiteParPiece is less than or equal to
        defaultProduitFiltering(
            "quantiteParPiece.lessThanOrEqual=" + DEFAULT_QUANTITE_PAR_PIECE,
            "quantiteParPiece.lessThanOrEqual=" + SMALLER_QUANTITE_PAR_PIECE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByQuantiteParPieceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where quantiteParPiece is less than
        defaultProduitFiltering(
            "quantiteParPiece.lessThan=" + UPDATED_QUANTITE_PAR_PIECE,
            "quantiteParPiece.lessThan=" + DEFAULT_QUANTITE_PAR_PIECE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByQuantiteParPieceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where quantiteParPiece is greater than
        defaultProduitFiltering(
            "quantiteParPiece.greaterThan=" + SMALLER_QUANTITE_PAR_PIECE,
            "quantiteParPiece.greaterThan=" + DEFAULT_QUANTITE_PAR_PIECE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByUniteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where unite equals to
        defaultProduitFiltering("unite.equals=" + DEFAULT_UNITE, "unite.equals=" + UPDATED_UNITE);
    }

    @Test
    @Transactional
    void getAllProduitsByUniteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where unite in
        defaultProduitFiltering("unite.in=" + DEFAULT_UNITE + "," + UPDATED_UNITE, "unite.in=" + UPDATED_UNITE);
    }

    @Test
    @Transactional
    void getAllProduitsByUniteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where unite is not null
        defaultProduitFiltering("unite.specified=true", "unite.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByUniteContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where unite contains
        defaultProduitFiltering("unite.contains=" + DEFAULT_UNITE, "unite.contains=" + UPDATED_UNITE);
    }

    @Test
    @Transactional
    void getAllProduitsByUniteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where unite does not contain
        defaultProduitFiltering("unite.doesNotContain=" + UPDATED_UNITE, "unite.doesNotContain=" + DEFAULT_UNITE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixParUniteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixParUnite equals to
        defaultProduitFiltering("prixParUnite.equals=" + DEFAULT_PRIX_PAR_UNITE, "prixParUnite.equals=" + UPDATED_PRIX_PAR_UNITE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixParUniteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixParUnite in
        defaultProduitFiltering(
            "prixParUnite.in=" + DEFAULT_PRIX_PAR_UNITE + "," + UPDATED_PRIX_PAR_UNITE,
            "prixParUnite.in=" + UPDATED_PRIX_PAR_UNITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByPrixParUniteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixParUnite is not null
        defaultProduitFiltering("prixParUnite.specified=true", "prixParUnite.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByPrixParUniteContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixParUnite contains
        defaultProduitFiltering("prixParUnite.contains=" + DEFAULT_PRIX_PAR_UNITE, "prixParUnite.contains=" + UPDATED_PRIX_PAR_UNITE);
    }

    @Test
    @Transactional
    void getAllProduitsByPrixParUniteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixParUnite does not contain
        defaultProduitFiltering(
            "prixParUnite.doesNotContain=" + UPDATED_PRIX_PAR_UNITE,
            "prixParUnite.doesNotContain=" + DEFAULT_PRIX_PAR_UNITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where fournisseur equals to
        defaultProduitFiltering("fournisseur.equals=" + DEFAULT_FOURNISSEUR, "fournisseur.equals=" + UPDATED_FOURNISSEUR);
    }

    @Test
    @Transactional
    void getAllProduitsByFournisseurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where fournisseur in
        defaultProduitFiltering(
            "fournisseur.in=" + DEFAULT_FOURNISSEUR + "," + UPDATED_FOURNISSEUR,
            "fournisseur.in=" + UPDATED_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByFournisseurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where fournisseur is not null
        defaultProduitFiltering("fournisseur.specified=true", "fournisseur.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByFournisseurContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where fournisseur contains
        defaultProduitFiltering("fournisseur.contains=" + DEFAULT_FOURNISSEUR, "fournisseur.contains=" + UPDATED_FOURNISSEUR);
    }

    @Test
    @Transactional
    void getAllProduitsByFournisseurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where fournisseur does not contain
        defaultProduitFiltering("fournisseur.doesNotContain=" + UPDATED_FOURNISSEUR, "fournisseur.doesNotContain=" + DEFAULT_FOURNISSEUR);
    }

    @Test
    @Transactional
    void getAllProduitsByRefFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where refFournisseur equals to
        defaultProduitFiltering("refFournisseur.equals=" + DEFAULT_REF_FOURNISSEUR, "refFournisseur.equals=" + UPDATED_REF_FOURNISSEUR);
    }

    @Test
    @Transactional
    void getAllProduitsByRefFournisseurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where refFournisseur in
        defaultProduitFiltering(
            "refFournisseur.in=" + DEFAULT_REF_FOURNISSEUR + "," + UPDATED_REF_FOURNISSEUR,
            "refFournisseur.in=" + UPDATED_REF_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByRefFournisseurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where refFournisseur is not null
        defaultProduitFiltering("refFournisseur.specified=true", "refFournisseur.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByRefFournisseurContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where refFournisseur contains
        defaultProduitFiltering("refFournisseur.contains=" + DEFAULT_REF_FOURNISSEUR, "refFournisseur.contains=" + UPDATED_REF_FOURNISSEUR);
    }

    @Test
    @Transactional
    void getAllProduitsByRefFournisseurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where refFournisseur does not contain
        defaultProduitFiltering(
            "refFournisseur.doesNotContain=" + UPDATED_REF_FOURNISSEUR,
            "refFournisseur.doesNotContain=" + DEFAULT_REF_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where stock equals to
        defaultProduitFiltering("stock.equals=" + DEFAULT_STOCK, "stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProduitsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where stock in
        defaultProduitFiltering("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK, "stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProduitsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where stock is not null
        defaultProduitFiltering("stock.specified=true", "stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where stock is greater than or equal to
        defaultProduitFiltering("stock.greaterThanOrEqual=" + DEFAULT_STOCK, "stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProduitsByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where stock is less than or equal to
        defaultProduitFiltering("stock.lessThanOrEqual=" + DEFAULT_STOCK, "stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProduitsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where stock is less than
        defaultProduitFiltering("stock.lessThan=" + UPDATED_STOCK, "stock.lessThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProduitsByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where stock is greater than
        defaultProduitFiltering("stock.greaterThan=" + SMALLER_STOCK, "stock.greaterThan=" + DEFAULT_STOCK);
    }

    @Test
    @Transactional
    void getAllProduitsByCommandesClientsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where commandesClients equals to
        defaultProduitFiltering(
            "commandesClients.equals=" + DEFAULT_COMMANDES_CLIENTS,
            "commandesClients.equals=" + UPDATED_COMMANDES_CLIENTS
        );
    }

    @Test
    @Transactional
    void getAllProduitsByCommandesClientsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where commandesClients in
        defaultProduitFiltering(
            "commandesClients.in=" + DEFAULT_COMMANDES_CLIENTS + "," + UPDATED_COMMANDES_CLIENTS,
            "commandesClients.in=" + UPDATED_COMMANDES_CLIENTS
        );
    }

    @Test
    @Transactional
    void getAllProduitsByCommandesClientsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where commandesClients is not null
        defaultProduitFiltering("commandesClients.specified=true", "commandesClients.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByCommandesClientsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where commandesClients is greater than or equal to
        defaultProduitFiltering(
            "commandesClients.greaterThanOrEqual=" + DEFAULT_COMMANDES_CLIENTS,
            "commandesClients.greaterThanOrEqual=" + UPDATED_COMMANDES_CLIENTS
        );
    }

    @Test
    @Transactional
    void getAllProduitsByCommandesClientsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where commandesClients is less than or equal to
        defaultProduitFiltering(
            "commandesClients.lessThanOrEqual=" + DEFAULT_COMMANDES_CLIENTS,
            "commandesClients.lessThanOrEqual=" + SMALLER_COMMANDES_CLIENTS
        );
    }

    @Test
    @Transactional
    void getAllProduitsByCommandesClientsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where commandesClients is less than
        defaultProduitFiltering(
            "commandesClients.lessThan=" + UPDATED_COMMANDES_CLIENTS,
            "commandesClients.lessThan=" + DEFAULT_COMMANDES_CLIENTS
        );
    }

    @Test
    @Transactional
    void getAllProduitsByCommandesClientsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where commandesClients is greater than
        defaultProduitFiltering(
            "commandesClients.greaterThan=" + SMALLER_COMMANDES_CLIENTS,
            "commandesClients.greaterThan=" + DEFAULT_COMMANDES_CLIENTS
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereVerificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereVerificationDate equals to
        defaultProduitFiltering(
            "derniereVerificationDate.equals=" + DEFAULT_DERNIERE_VERIFICATION_DATE,
            "derniereVerificationDate.equals=" + UPDATED_DERNIERE_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereVerificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereVerificationDate in
        defaultProduitFiltering(
            "derniereVerificationDate.in=" + DEFAULT_DERNIERE_VERIFICATION_DATE + "," + UPDATED_DERNIERE_VERIFICATION_DATE,
            "derniereVerificationDate.in=" + UPDATED_DERNIERE_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereVerificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereVerificationDate is not null
        defaultProduitFiltering("derniereVerificationDate.specified=true", "derniereVerificationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereVerificationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereVerificationDate is greater than or equal to
        defaultProduitFiltering(
            "derniereVerificationDate.greaterThanOrEqual=" + DEFAULT_DERNIERE_VERIFICATION_DATE,
            "derniereVerificationDate.greaterThanOrEqual=" + UPDATED_DERNIERE_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereVerificationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereVerificationDate is less than or equal to
        defaultProduitFiltering(
            "derniereVerificationDate.lessThanOrEqual=" + DEFAULT_DERNIERE_VERIFICATION_DATE,
            "derniereVerificationDate.lessThanOrEqual=" + SMALLER_DERNIERE_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereVerificationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereVerificationDate is less than
        defaultProduitFiltering(
            "derniereVerificationDate.lessThan=" + UPDATED_DERNIERE_VERIFICATION_DATE,
            "derniereVerificationDate.lessThan=" + DEFAULT_DERNIERE_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereVerificationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereVerificationDate is greater than
        defaultProduitFiltering(
            "derniereVerificationDate.greaterThan=" + SMALLER_DERNIERE_VERIFICATION_DATE,
            "derniereVerificationDate.greaterThan=" + DEFAULT_DERNIERE_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereLivraisonDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereLivraisonDate equals to
        defaultProduitFiltering(
            "derniereLivraisonDate.equals=" + DEFAULT_DERNIERE_LIVRAISON_DATE,
            "derniereLivraisonDate.equals=" + UPDATED_DERNIERE_LIVRAISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereLivraisonDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereLivraisonDate in
        defaultProduitFiltering(
            "derniereLivraisonDate.in=" + DEFAULT_DERNIERE_LIVRAISON_DATE + "," + UPDATED_DERNIERE_LIVRAISON_DATE,
            "derniereLivraisonDate.in=" + UPDATED_DERNIERE_LIVRAISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereLivraisonDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereLivraisonDate is not null
        defaultProduitFiltering("derniereLivraisonDate.specified=true", "derniereLivraisonDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereLivraisonDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereLivraisonDate is greater than or equal to
        defaultProduitFiltering(
            "derniereLivraisonDate.greaterThanOrEqual=" + DEFAULT_DERNIERE_LIVRAISON_DATE,
            "derniereLivraisonDate.greaterThanOrEqual=" + UPDATED_DERNIERE_LIVRAISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereLivraisonDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereLivraisonDate is less than or equal to
        defaultProduitFiltering(
            "derniereLivraisonDate.lessThanOrEqual=" + DEFAULT_DERNIERE_LIVRAISON_DATE,
            "derniereLivraisonDate.lessThanOrEqual=" + SMALLER_DERNIERE_LIVRAISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereLivraisonDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereLivraisonDate is less than
        defaultProduitFiltering(
            "derniereLivraisonDate.lessThan=" + UPDATED_DERNIERE_LIVRAISON_DATE,
            "derniereLivraisonDate.lessThan=" + DEFAULT_DERNIERE_LIVRAISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDerniereLivraisonDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where derniereLivraisonDate is greater than
        defaultProduitFiltering(
            "derniereLivraisonDate.greaterThan=" + SMALLER_DERNIERE_LIVRAISON_DATE,
            "derniereLivraisonDate.greaterThan=" + DEFAULT_DERNIERE_LIVRAISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByAchatFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where achatFournisseur equals to
        defaultProduitFiltering(
            "achatFournisseur.equals=" + DEFAULT_ACHAT_FOURNISSEUR,
            "achatFournisseur.equals=" + UPDATED_ACHAT_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByAchatFournisseurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where achatFournisseur in
        defaultProduitFiltering(
            "achatFournisseur.in=" + DEFAULT_ACHAT_FOURNISSEUR + "," + UPDATED_ACHAT_FOURNISSEUR,
            "achatFournisseur.in=" + UPDATED_ACHAT_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByAchatFournisseurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where achatFournisseur is not null
        defaultProduitFiltering("achatFournisseur.specified=true", "achatFournisseur.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByAchatFournisseurContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where achatFournisseur contains
        defaultProduitFiltering(
            "achatFournisseur.contains=" + DEFAULT_ACHAT_FOURNISSEUR,
            "achatFournisseur.contains=" + UPDATED_ACHAT_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByAchatFournisseurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where achatFournisseur does not contain
        defaultProduitFiltering(
            "achatFournisseur.doesNotContain=" + UPDATED_ACHAT_FOURNISSEUR,
            "achatFournisseur.doesNotContain=" + DEFAULT_ACHAT_FOURNISSEUR
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatDate equals to
        defaultProduitFiltering(
            "dernierAchatDate.equals=" + DEFAULT_DERNIER_ACHAT_DATE,
            "dernierAchatDate.equals=" + UPDATED_DERNIER_ACHAT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatDate in
        defaultProduitFiltering(
            "dernierAchatDate.in=" + DEFAULT_DERNIER_ACHAT_DATE + "," + UPDATED_DERNIER_ACHAT_DATE,
            "dernierAchatDate.in=" + UPDATED_DERNIER_ACHAT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatDate is not null
        defaultProduitFiltering("dernierAchatDate.specified=true", "dernierAchatDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatDate is greater than or equal to
        defaultProduitFiltering(
            "dernierAchatDate.greaterThanOrEqual=" + DEFAULT_DERNIER_ACHAT_DATE,
            "dernierAchatDate.greaterThanOrEqual=" + UPDATED_DERNIER_ACHAT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatDate is less than or equal to
        defaultProduitFiltering(
            "dernierAchatDate.lessThanOrEqual=" + DEFAULT_DERNIER_ACHAT_DATE,
            "dernierAchatDate.lessThanOrEqual=" + SMALLER_DERNIER_ACHAT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatDate is less than
        defaultProduitFiltering(
            "dernierAchatDate.lessThan=" + UPDATED_DERNIER_ACHAT_DATE,
            "dernierAchatDate.lessThan=" + DEFAULT_DERNIER_ACHAT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatDate is greater than
        defaultProduitFiltering(
            "dernierAchatDate.greaterThan=" + SMALLER_DERNIER_ACHAT_DATE,
            "dernierAchatDate.greaterThan=" + DEFAULT_DERNIER_ACHAT_DATE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatQuantiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatQuantite equals to
        defaultProduitFiltering(
            "dernierAchatQuantite.equals=" + DEFAULT_DERNIER_ACHAT_QUANTITE,
            "dernierAchatQuantite.equals=" + UPDATED_DERNIER_ACHAT_QUANTITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatQuantiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatQuantite in
        defaultProduitFiltering(
            "dernierAchatQuantite.in=" + DEFAULT_DERNIER_ACHAT_QUANTITE + "," + UPDATED_DERNIER_ACHAT_QUANTITE,
            "dernierAchatQuantite.in=" + UPDATED_DERNIER_ACHAT_QUANTITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatQuantiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatQuantite is not null
        defaultProduitFiltering("dernierAchatQuantite.specified=true", "dernierAchatQuantite.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatQuantiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatQuantite is greater than or equal to
        defaultProduitFiltering(
            "dernierAchatQuantite.greaterThanOrEqual=" + DEFAULT_DERNIER_ACHAT_QUANTITE,
            "dernierAchatQuantite.greaterThanOrEqual=" + UPDATED_DERNIER_ACHAT_QUANTITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatQuantiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatQuantite is less than or equal to
        defaultProduitFiltering(
            "dernierAchatQuantite.lessThanOrEqual=" + DEFAULT_DERNIER_ACHAT_QUANTITE,
            "dernierAchatQuantite.lessThanOrEqual=" + SMALLER_DERNIER_ACHAT_QUANTITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatQuantiteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatQuantite is less than
        defaultProduitFiltering(
            "dernierAchatQuantite.lessThan=" + UPDATED_DERNIER_ACHAT_QUANTITE,
            "dernierAchatQuantite.lessThan=" + DEFAULT_DERNIER_ACHAT_QUANTITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByDernierAchatQuantiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where dernierAchatQuantite is greater than
        defaultProduitFiltering(
            "dernierAchatQuantite.greaterThan=" + SMALLER_DERNIER_ACHAT_QUANTITE,
            "dernierAchatQuantite.greaterThan=" + DEFAULT_DERNIER_ACHAT_QUANTITE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsLivraisonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsLivraison equals to
        defaultProduitFiltering("statsLivraison.equals=" + DEFAULT_STATS_LIVRAISON, "statsLivraison.equals=" + UPDATED_STATS_LIVRAISON);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsLivraisonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsLivraison in
        defaultProduitFiltering(
            "statsLivraison.in=" + DEFAULT_STATS_LIVRAISON + "," + UPDATED_STATS_LIVRAISON,
            "statsLivraison.in=" + UPDATED_STATS_LIVRAISON
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsLivraisonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsLivraison is not null
        defaultProduitFiltering("statsLivraison.specified=true", "statsLivraison.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByStatsLivraisonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsLivraison is greater than or equal to
        defaultProduitFiltering(
            "statsLivraison.greaterThanOrEqual=" + DEFAULT_STATS_LIVRAISON,
            "statsLivraison.greaterThanOrEqual=" + UPDATED_STATS_LIVRAISON
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsLivraisonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsLivraison is less than or equal to
        defaultProduitFiltering(
            "statsLivraison.lessThanOrEqual=" + DEFAULT_STATS_LIVRAISON,
            "statsLivraison.lessThanOrEqual=" + SMALLER_STATS_LIVRAISON
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsLivraisonIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsLivraison is less than
        defaultProduitFiltering("statsLivraison.lessThan=" + UPDATED_STATS_LIVRAISON, "statsLivraison.lessThan=" + DEFAULT_STATS_LIVRAISON);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsLivraisonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsLivraison is greater than
        defaultProduitFiltering(
            "statsLivraison.greaterThan=" + SMALLER_STATS_LIVRAISON,
            "statsLivraison.greaterThan=" + DEFAULT_STATS_LIVRAISON
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsPerteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsPerte equals to
        defaultProduitFiltering("statsPerte.equals=" + DEFAULT_STATS_PERTE, "statsPerte.equals=" + UPDATED_STATS_PERTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsPerteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsPerte in
        defaultProduitFiltering("statsPerte.in=" + DEFAULT_STATS_PERTE + "," + UPDATED_STATS_PERTE, "statsPerte.in=" + UPDATED_STATS_PERTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsPerteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsPerte is not null
        defaultProduitFiltering("statsPerte.specified=true", "statsPerte.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByStatsPerteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsPerte is greater than or equal to
        defaultProduitFiltering(
            "statsPerte.greaterThanOrEqual=" + DEFAULT_STATS_PERTE,
            "statsPerte.greaterThanOrEqual=" + UPDATED_STATS_PERTE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsPerteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsPerte is less than or equal to
        defaultProduitFiltering("statsPerte.lessThanOrEqual=" + DEFAULT_STATS_PERTE, "statsPerte.lessThanOrEqual=" + SMALLER_STATS_PERTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsPerteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsPerte is less than
        defaultProduitFiltering("statsPerte.lessThan=" + UPDATED_STATS_PERTE, "statsPerte.lessThan=" + DEFAULT_STATS_PERTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsPerteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsPerte is greater than
        defaultProduitFiltering("statsPerte.greaterThan=" + SMALLER_STATS_PERTE, "statsPerte.greaterThan=" + DEFAULT_STATS_PERTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVente equals to
        defaultProduitFiltering("statsVente.equals=" + DEFAULT_STATS_VENTE, "statsVente.equals=" + UPDATED_STATS_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVente in
        defaultProduitFiltering("statsVente.in=" + DEFAULT_STATS_VENTE + "," + UPDATED_STATS_VENTE, "statsVente.in=" + UPDATED_STATS_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVente is not null
        defaultProduitFiltering("statsVente.specified=true", "statsVente.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVente is greater than or equal to
        defaultProduitFiltering(
            "statsVente.greaterThanOrEqual=" + DEFAULT_STATS_VENTE,
            "statsVente.greaterThanOrEqual=" + UPDATED_STATS_VENTE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVente is less than or equal to
        defaultProduitFiltering("statsVente.lessThanOrEqual=" + DEFAULT_STATS_VENTE, "statsVente.lessThanOrEqual=" + SMALLER_STATS_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVente is less than
        defaultProduitFiltering("statsVente.lessThan=" + UPDATED_STATS_VENTE, "statsVente.lessThan=" + DEFAULT_STATS_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVente is greater than
        defaultProduitFiltering("statsVente.greaterThan=" + SMALLER_STATS_VENTE, "statsVente.greaterThan=" + DEFAULT_STATS_VENTE);
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteSpecialeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVenteSpeciale equals to
        defaultProduitFiltering(
            "statsVenteSpeciale.equals=" + DEFAULT_STATS_VENTE_SPECIALE,
            "statsVenteSpeciale.equals=" + UPDATED_STATS_VENTE_SPECIALE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteSpecialeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVenteSpeciale in
        defaultProduitFiltering(
            "statsVenteSpeciale.in=" + DEFAULT_STATS_VENTE_SPECIALE + "," + UPDATED_STATS_VENTE_SPECIALE,
            "statsVenteSpeciale.in=" + UPDATED_STATS_VENTE_SPECIALE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteSpecialeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVenteSpeciale is not null
        defaultProduitFiltering("statsVenteSpeciale.specified=true", "statsVenteSpeciale.specified=false");
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteSpecialeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVenteSpeciale is greater than or equal to
        defaultProduitFiltering(
            "statsVenteSpeciale.greaterThanOrEqual=" + DEFAULT_STATS_VENTE_SPECIALE,
            "statsVenteSpeciale.greaterThanOrEqual=" + UPDATED_STATS_VENTE_SPECIALE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteSpecialeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVenteSpeciale is less than or equal to
        defaultProduitFiltering(
            "statsVenteSpeciale.lessThanOrEqual=" + DEFAULT_STATS_VENTE_SPECIALE,
            "statsVenteSpeciale.lessThanOrEqual=" + SMALLER_STATS_VENTE_SPECIALE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteSpecialeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVenteSpeciale is less than
        defaultProduitFiltering(
            "statsVenteSpeciale.lessThan=" + UPDATED_STATS_VENTE_SPECIALE,
            "statsVenteSpeciale.lessThan=" + DEFAULT_STATS_VENTE_SPECIALE
        );
    }

    @Test
    @Transactional
    void getAllProduitsByStatsVenteSpecialeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        // Get all the produitList where statsVenteSpeciale is greater than
        defaultProduitFiltering(
            "statsVenteSpeciale.greaterThan=" + SMALLER_STATS_VENTE_SPECIALE,
            "statsVenteSpeciale.greaterThan=" + DEFAULT_STATS_VENTE_SPECIALE
        );
    }

    private void defaultProduitFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProduitShouldBeFound(shouldBeFound);
        defaultProduitShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProduitShouldBeFound(String filter) throws Exception {
        restProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().toString())))
            .andExpect(jsonPath("$.[*].epicerioId").value(hasItem(DEFAULT_EPICERIO_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].importedDate").value(hasItem(DEFAULT_IMPORTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].disponible").value(hasItem(DEFAULT_DISPONIBLE)))
            .andExpect(jsonPath("$.[*].prixFournisseur").value(hasItem(DEFAULT_PRIX_FOURNISSEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].htTtc").value(hasItem(DEFAULT_HT_TTC)))
            .andExpect(jsonPath("$.[*].tauxTva").value(hasItem(DEFAULT_TAUX_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].margeProfit").value(hasItem(DEFAULT_MARGE_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.doubleValue())))
            .andExpect(jsonPath("$.[*].vendu").value(hasItem(DEFAULT_VENDU)))
            .andExpect(jsonPath("$.[*].quantiteParPiece").value(hasItem(DEFAULT_QUANTITE_PAR_PIECE.doubleValue())))
            .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE)))
            .andExpect(jsonPath("$.[*].prixParUnite").value(hasItem(DEFAULT_PRIX_PAR_UNITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].remarquesInternes").value(hasItem(DEFAULT_REMARQUES_INTERNES.toString())))
            .andExpect(jsonPath("$.[*].fournisseur").value(hasItem(DEFAULT_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].refFournisseur").value(hasItem(DEFAULT_REF_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.doubleValue())))
            .andExpect(jsonPath("$.[*].commandesClients").value(hasItem(DEFAULT_COMMANDES_CLIENTS.doubleValue())))
            .andExpect(jsonPath("$.[*].derniereVerificationDate").value(hasItem(DEFAULT_DERNIERE_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].derniereLivraisonDate").value(hasItem(DEFAULT_DERNIERE_LIVRAISON_DATE.toString())))
            .andExpect(jsonPath("$.[*].achatFournisseur").value(hasItem(DEFAULT_ACHAT_FOURNISSEUR)))
            .andExpect(jsonPath("$.[*].dernierAchatDate").value(hasItem(DEFAULT_DERNIER_ACHAT_DATE.toString())))
            .andExpect(jsonPath("$.[*].dernierAchatQuantite").value(hasItem(DEFAULT_DERNIER_ACHAT_QUANTITE.doubleValue())))
            .andExpect(jsonPath("$.[*].statsLivraison").value(hasItem(DEFAULT_STATS_LIVRAISON.doubleValue())))
            .andExpect(jsonPath("$.[*].statsPerte").value(hasItem(DEFAULT_STATS_PERTE.doubleValue())))
            .andExpect(jsonPath("$.[*].statsVente").value(hasItem(DEFAULT_STATS_VENTE.doubleValue())))
            .andExpect(jsonPath("$.[*].statsVenteSpeciale").value(hasItem(DEFAULT_STATS_VENTE_SPECIALE.doubleValue())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())));

        // Check, that the count call also returns 1
        restProduitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProduitShouldNotBeFound(String filter) throws Exception {
        restProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProduitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduit() throws Exception {
        // Get the produit
        restProduitMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduit() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produit
        Produit updatedProduit = produitRepository.findById(produit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduit are not directly saved in db
        em.detach(updatedProduit);
        updatedProduit
            .epicerioId(UPDATED_EPICERIO_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .importedDate(UPDATED_IMPORTED_DATE)
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .disponible(UPDATED_DISPONIBLE)
            .prixFournisseur(UPDATED_PRIX_FOURNISSEUR)
            .htTtc(UPDATED_HT_TTC)
            .tauxTva(UPDATED_TAUX_TVA)
            .margeProfit(UPDATED_MARGE_PROFIT)
            .prixVente(UPDATED_PRIX_VENTE)
            .vendu(UPDATED_VENDU)
            .quantiteParPiece(UPDATED_QUANTITE_PAR_PIECE)
            .unite(UPDATED_UNITE)
            .prixParUnite(UPDATED_PRIX_PAR_UNITE)
            .description(UPDATED_DESCRIPTION)
            .remarquesInternes(UPDATED_REMARQUES_INTERNES)
            .fournisseur(UPDATED_FOURNISSEUR)
            .refFournisseur(UPDATED_REF_FOURNISSEUR)
            .stock(UPDATED_STOCK)
            .commandesClients(UPDATED_COMMANDES_CLIENTS)
            .derniereVerificationDate(UPDATED_DERNIERE_VERIFICATION_DATE)
            .derniereLivraisonDate(UPDATED_DERNIERE_LIVRAISON_DATE)
            .achatFournisseur(UPDATED_ACHAT_FOURNISSEUR)
            .dernierAchatDate(UPDATED_DERNIER_ACHAT_DATE)
            .dernierAchatQuantite(UPDATED_DERNIER_ACHAT_QUANTITE)
            .statsLivraison(UPDATED_STATS_LIVRAISON)
            .statsPerte(UPDATED_STATS_PERTE)
            .statsVente(UPDATED_STATS_VENTE)
            .statsVenteSpeciale(UPDATED_STATS_VENTE_SPECIALE)
            .tags(UPDATED_TAGS);
        ProduitDTO produitDTO = produitMapper.toDto(updatedProduit);

        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produitDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProduitToMatchAllProperties(updatedProduit);
    }

    @Test
    @Transactional
    void putNonExistingProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(UUID.randomUUID());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, produitDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(UUID.randomUUID());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(UUID.randomUUID());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produit using partial update
        Produit partialUpdatedProduit = new Produit();
        partialUpdatedProduit.setId(produit.getId());

        partialUpdatedProduit
            .code(UPDATED_CODE)
            .prixFournisseur(UPDATED_PRIX_FOURNISSEUR)
            .htTtc(UPDATED_HT_TTC)
            .prixVente(UPDATED_PRIX_VENTE)
            .vendu(UPDATED_VENDU)
            .description(UPDATED_DESCRIPTION)
            .remarquesInternes(UPDATED_REMARQUES_INTERNES)
            .fournisseur(UPDATED_FOURNISSEUR)
            .refFournisseur(UPDATED_REF_FOURNISSEUR)
            .stock(UPDATED_STOCK)
            .commandesClients(UPDATED_COMMANDES_CLIENTS)
            .derniereVerificationDate(UPDATED_DERNIERE_VERIFICATION_DATE)
            .dernierAchatQuantite(UPDATED_DERNIER_ACHAT_QUANTITE)
            .tags(UPDATED_TAGS);

        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduit))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProduitUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduit, produit), getPersistedProduit(produit));
    }

    @Test
    @Transactional
    void fullUpdateProduitWithPatch() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produit using partial update
        Produit partialUpdatedProduit = new Produit();
        partialUpdatedProduit.setId(produit.getId());

        partialUpdatedProduit
            .epicerioId(UPDATED_EPICERIO_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
            .importedDate(UPDATED_IMPORTED_DATE)
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .disponible(UPDATED_DISPONIBLE)
            .prixFournisseur(UPDATED_PRIX_FOURNISSEUR)
            .htTtc(UPDATED_HT_TTC)
            .tauxTva(UPDATED_TAUX_TVA)
            .margeProfit(UPDATED_MARGE_PROFIT)
            .prixVente(UPDATED_PRIX_VENTE)
            .vendu(UPDATED_VENDU)
            .quantiteParPiece(UPDATED_QUANTITE_PAR_PIECE)
            .unite(UPDATED_UNITE)
            .prixParUnite(UPDATED_PRIX_PAR_UNITE)
            .description(UPDATED_DESCRIPTION)
            .remarquesInternes(UPDATED_REMARQUES_INTERNES)
            .fournisseur(UPDATED_FOURNISSEUR)
            .refFournisseur(UPDATED_REF_FOURNISSEUR)
            .stock(UPDATED_STOCK)
            .commandesClients(UPDATED_COMMANDES_CLIENTS)
            .derniereVerificationDate(UPDATED_DERNIERE_VERIFICATION_DATE)
            .derniereLivraisonDate(UPDATED_DERNIERE_LIVRAISON_DATE)
            .achatFournisseur(UPDATED_ACHAT_FOURNISSEUR)
            .dernierAchatDate(UPDATED_DERNIER_ACHAT_DATE)
            .dernierAchatQuantite(UPDATED_DERNIER_ACHAT_QUANTITE)
            .statsLivraison(UPDATED_STATS_LIVRAISON)
            .statsPerte(UPDATED_STATS_PERTE)
            .statsVente(UPDATED_STATS_VENTE)
            .statsVenteSpeciale(UPDATED_STATS_VENTE_SPECIALE)
            .tags(UPDATED_TAGS);

        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduit))
            )
            .andExpect(status().isOk());

        // Validate the Produit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProduitUpdatableFieldsEquals(partialUpdatedProduit, getPersistedProduit(partialUpdatedProduit));
    }

    @Test
    @Transactional
    void patchNonExistingProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(UUID.randomUUID());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, produitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(UUID.randomUUID());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(produitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produit.setId(UUID.randomUUID());

        // Create the Produit
        ProduitDTO produitDTO = produitMapper.toDto(produit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProduitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(produitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduit() throws Exception {
        // Initialize the database
        insertedProduit = produitRepository.saveAndFlush(produit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the produit
        restProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, produit.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return produitRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Produit getPersistedProduit(Produit produit) {
        return produitRepository.findById(produit.getId()).orElseThrow();
    }

    protected void assertPersistedProduitToMatchAllProperties(Produit expectedProduit) {
        assertProduitAllPropertiesEquals(expectedProduit, getPersistedProduit(expectedProduit));
    }

    protected void assertPersistedProduitToMatchUpdatableProperties(Produit expectedProduit) {
        assertProduitAllUpdatablePropertiesEquals(expectedProduit, getPersistedProduit(expectedProduit));
    }
}
