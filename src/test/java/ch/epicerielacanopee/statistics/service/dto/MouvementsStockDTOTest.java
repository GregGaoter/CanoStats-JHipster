package ch.epicerielacanopee.statistics.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ch.epicerielacanopee.statistics.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MouvementsStockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MouvementsStockDTO.class);
        MouvementsStockDTO mouvementsStockDTO1 = new MouvementsStockDTO();
        mouvementsStockDTO1.setId(UUID.randomUUID());
        MouvementsStockDTO mouvementsStockDTO2 = new MouvementsStockDTO();
        assertThat(mouvementsStockDTO1).isNotEqualTo(mouvementsStockDTO2);
        mouvementsStockDTO2.setId(mouvementsStockDTO1.getId());
        assertThat(mouvementsStockDTO1).isEqualTo(mouvementsStockDTO2);
        mouvementsStockDTO2.setId(UUID.randomUUID());
        assertThat(mouvementsStockDTO1).isNotEqualTo(mouvementsStockDTO2);
        mouvementsStockDTO1.setId(null);
        assertThat(mouvementsStockDTO1).isNotEqualTo(mouvementsStockDTO2);
    }
}
