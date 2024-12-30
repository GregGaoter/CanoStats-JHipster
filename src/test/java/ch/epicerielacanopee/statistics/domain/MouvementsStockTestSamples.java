package ch.epicerielacanopee.statistics.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MouvementsStockTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MouvementsStock getMouvementsStockSample1() {
        return new MouvementsStock()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .epicerioId(1)
            .utilisateur("utilisateur1")
            .type("type1")
            .vente("vente1")
            .codeProduit("codeProduit1")
            .produit("produit1")
            .responsableProduit("responsableProduit1")
            .fournisseurProduit("fournisseurProduit1")
            .codeFournisseur("codeFournisseur1");
    }

    public static MouvementsStock getMouvementsStockSample2() {
        return new MouvementsStock()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .epicerioId(2)
            .utilisateur("utilisateur2")
            .type("type2")
            .vente("vente2")
            .codeProduit("codeProduit2")
            .produit("produit2")
            .responsableProduit("responsableProduit2")
            .fournisseurProduit("fournisseurProduit2")
            .codeFournisseur("codeFournisseur2");
    }

    public static MouvementsStock getMouvementsStockRandomSampleGenerator() {
        return new MouvementsStock()
            .id(UUID.randomUUID())
            .epicerioId(intCount.incrementAndGet())
            .utilisateur(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .vente(UUID.randomUUID().toString())
            .codeProduit(UUID.randomUUID().toString())
            .produit(UUID.randomUUID().toString())
            .responsableProduit(UUID.randomUUID().toString())
            .fournisseurProduit(UUID.randomUUID().toString())
            .codeFournisseur(UUID.randomUUID().toString());
    }
}
