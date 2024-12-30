package ch.epicerielacanopee.statistics.service.mapper;

import static ch.epicerielacanopee.statistics.domain.MouvementsStockAsserts.*;
import static ch.epicerielacanopee.statistics.domain.MouvementsStockTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MouvementsStockMapperTest {

    private MouvementsStockMapper mouvementsStockMapper;

    @BeforeEach
    void setUp() {
        mouvementsStockMapper = new MouvementsStockMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMouvementsStockSample1();
        var actual = mouvementsStockMapper.toEntity(mouvementsStockMapper.toDto(expected));
        assertMouvementsStockAllPropertiesEquals(expected, actual);
    }
}
