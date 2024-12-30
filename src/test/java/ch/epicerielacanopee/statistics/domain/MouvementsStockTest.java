package ch.epicerielacanopee.statistics.domain;

import static ch.epicerielacanopee.statistics.domain.MouvementsStockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ch.epicerielacanopee.statistics.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MouvementsStockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MouvementsStock.class);
        MouvementsStock mouvementsStock1 = getMouvementsStockSample1();
        MouvementsStock mouvementsStock2 = new MouvementsStock();
        assertThat(mouvementsStock1).isNotEqualTo(mouvementsStock2);

        mouvementsStock2.setId(mouvementsStock1.getId());
        assertThat(mouvementsStock1).isEqualTo(mouvementsStock2);

        mouvementsStock2 = getMouvementsStockSample2();
        assertThat(mouvementsStock1).isNotEqualTo(mouvementsStock2);
    }
}
