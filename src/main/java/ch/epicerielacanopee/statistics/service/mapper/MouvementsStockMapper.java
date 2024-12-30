package ch.epicerielacanopee.statistics.service.mapper;

import ch.epicerielacanopee.statistics.domain.MouvementsStock;
import ch.epicerielacanopee.statistics.service.dto.MouvementsStockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MouvementsStock} and its DTO {@link MouvementsStockDTO}.
 */
@Mapper(componentModel = "spring")
public interface MouvementsStockMapper extends EntityMapper<MouvementsStockDTO, MouvementsStock> {}
