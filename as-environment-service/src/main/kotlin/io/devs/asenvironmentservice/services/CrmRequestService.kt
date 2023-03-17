package io.devs.asenvironmentservice.services

import com.tej.JooQDemo.jooq.sample.model.crm_request.Routines.*
import com.tej.JooQDemo.jooq.sample.model.crm_request.Tables.*
import com.tej.JooQDemo.jooq.sample.model.dictionaries.Tables.CONTRACTOR
import com.tej.JooQDemo.jooq.sample.model.dictionaries.Tables.PRODUCTS
import io.devs.asenvironmentservice.dto.*
import io.devs.asenvironmentservice.enums.CrmRequestStates
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Service

@Service
class CrmRequestService(private val dsl: DSLContext) {

    fun getCrmRequests(onlyInProduction: Boolean): List<CrmRequestDto> {
        return dsl.select(
            REQUEST.ID,
            REQUEST.S_NUMBER,
            REQUEST.D_DATE,
            REQUEST.ID_CONTRACTOR,
            CONTRACTOR.S_INN,
            CONTRACTOR.S_CAPTION,
            REQUEST.S_DESCRIPTION,
            REQUEST.S_STATE_CODE,
            REQUEST_STATES.S_CAPTION,
            REQUEST.D_RELEASE_DATE
        )
            .from(REQUEST)
            .innerJoin(REQUEST_STATES).on(REQUEST_STATES.S_CODE.eq(REQUEST.S_STATE_CODE))
            .innerJoin(CONTRACTOR).on(CONTRACTOR.ID.eq(REQUEST.ID_CONTRACTOR))
            .where(
                if (onlyInProduction) REQUEST.S_STATE_CODE.`in`(
                    CrmRequestStates.DRAFT.code,
                    CrmRequestStates.IN_PRODUCTION.code
                ) else DSL.trueCondition()
            )
            .fetch { record ->
                CrmRequestDto(
                    id = record.get(REQUEST.ID),
                    number = record.get(REQUEST.S_NUMBER),
                    date = record.get(REQUEST.D_DATE),
                    contractor = ContractorDto(
                        id = record.get(REQUEST.ID_CONTRACTOR),
                        inn = record.get(CONTRACTOR.S_INN),
                        caption = record.get(CONTRACTOR.S_CAPTION)
                    ),
                    description = record.get(REQUEST.S_DESCRIPTION) ?: "",
                    state = CrmRequestStateDto(
                        code = record.get(REQUEST.S_STATE_CODE),
                        caption = record.get(REQUEST_STATES.S_CAPTION)
                    ),
                    releaseDate = record.get(REQUEST.D_RELEASE_DATE)
                );
            };
    }

    fun getAllCrmRequests(): List<CrmRequestDto> {
        return getCrmRequests(false);
    }

    fun getActiveCrmRequests(): List<CrmRequestDto> {
        return getCrmRequests(true);
    }

    fun getCrmRequest(requestId: Long): CrmRequestDto {
        return dsl.select(
            REQUEST.ID,
            REQUEST.S_NUMBER,
            REQUEST.D_DATE,
            REQUEST.ID_CONTRACTOR,
            CONTRACTOR.S_INN,
            CONTRACTOR.S_CAPTION,
            REQUEST.S_DESCRIPTION,
            REQUEST.S_STATE_CODE,
            REQUEST_STATES.S_CAPTION,
            REQUEST.D_RELEASE_DATE
        )
            .from(REQUEST)
            .innerJoin(REQUEST_STATES).on(REQUEST_STATES.S_CODE.eq(REQUEST.S_STATE_CODE))
            .innerJoin(CONTRACTOR).on(CONTRACTOR.ID.eq(REQUEST.ID_CONTRACTOR))
            .where(REQUEST.ID.eq(requestId))
            .fetchOne { record ->
                CrmRequestDto(
                    id = record.get(REQUEST.ID),
                    number = record.get(REQUEST.S_NUMBER),
                    date = record.get(REQUEST.D_DATE),
                    contractor = ContractorDto(
                        id = record.get(REQUEST.ID_CONTRACTOR),
                        inn = record.get(CONTRACTOR.S_INN),
                        caption = record.get(CONTRACTOR.S_CAPTION)
                    ),
                    description = record.get(REQUEST.S_DESCRIPTION) ?: "",
                    state = CrmRequestStateDto(
                        code = record.get(REQUEST.S_STATE_CODE),
                        caption = record.get(REQUEST_STATES.S_CAPTION)
                    ),
                    releaseDate = record.get(REQUEST.D_RELEASE_DATE)
                );
            } ?: CrmRequestDto();
    }

    fun getCrmRequestItems(requestId: Long): List<CrmRequestItemDto> {
        return dsl.select(
            REQUEST_ITEM.ID,
            REQUEST_ITEM.ID_REQUEST,
            REQUEST.S_NUMBER,
            REQUEST.D_DATE,
            REQUEST.ID_CONTRACTOR,
            CONTRACTOR.S_INN,
            CONTRACTOR.S_CAPTION,
            REQUEST.S_DESCRIPTION,
            REQUEST.S_STATE_CODE,
            REQUEST_STATES.S_CAPTION,
            REQUEST.D_RELEASE_DATE,
            REQUEST_ITEM.ID_PRODUCT,
            PRODUCTS.S_CODE,
            PRODUCTS.S_CAPTION,
            PRODUCTS.N_LATHE_TIME,
            PRODUCTS.N_MILLING_TIME,
            REQUEST_ITEM.F_QUANTITY,
            REQUEST_ITEM.F_QUANTITY_EXEC
        )
            .from(REQUEST_ITEM)
            .innerJoin(REQUEST).on(REQUEST.ID.eq(REQUEST_ITEM.ID_REQUEST))
            .innerJoin(REQUEST_STATES).on(REQUEST_STATES.S_CODE.eq(REQUEST.S_STATE_CODE))
            .innerJoin(CONTRACTOR).on(CONTRACTOR.ID.eq(REQUEST.ID_CONTRACTOR))
            .innerJoin(PRODUCTS).on(PRODUCTS.ID.eq(REQUEST_ITEM.ID_PRODUCT))
            .where(REQUEST_ITEM.ID_REQUEST.eq(requestId))
            .fetch { record ->
                CrmRequestItemDto(
                    id = record.get(REQUEST_ITEM.ID),
                    request = CrmRequestDto(
                        id = record.get(REQUEST.ID),
                        number = record.get(REQUEST.S_NUMBER),
                        date = record.get(REQUEST.D_DATE),
                        contractor = ContractorDto(
                            id = record.get(REQUEST.ID_CONTRACTOR),
                            inn = record.get(CONTRACTOR.S_INN),
                            caption = record.get(CONTRACTOR.S_CAPTION)
                        ),
                        description = record.get(REQUEST.S_DESCRIPTION) ?: "",
                        state = CrmRequestStateDto(
                            code = record.get(REQUEST.S_STATE_CODE),
                            caption = record.get(REQUEST_STATES.S_CAPTION)
                        ),
                        releaseDate = record.get(REQUEST.D_RELEASE_DATE)
                    ),
                    product = ProductDto(
                        id = record.get(REQUEST_ITEM.ID_PRODUCT),
                        code = record.get(PRODUCTS.S_CODE),
                        caption = record.get(PRODUCTS.S_CAPTION),
                        latheTime = record.get(PRODUCTS.N_LATHE_TIME).toLong(),
                        millingTime = record.get(PRODUCTS.N_MILLING_TIME).toLong()
                    ),
                    quantity = record.get(REQUEST_ITEM.F_QUANTITY).toLong(),
                    quantityExec = record.get(REQUEST_ITEM.F_QUANTITY_EXEC).toLong()
                )
            };
    }

    fun getCrmRequestItem(requestId: Long, itemId: Long): CrmRequestItemDto {
        return dsl.select(
            REQUEST_ITEM.ID,
            REQUEST_ITEM.ID_REQUEST,
            REQUEST.S_NUMBER,
            REQUEST.D_DATE,
            REQUEST.ID_CONTRACTOR,
            CONTRACTOR.S_INN,
            CONTRACTOR.S_CAPTION,
            REQUEST.S_DESCRIPTION,
            REQUEST.S_STATE_CODE,
            REQUEST_STATES.S_CAPTION,
            REQUEST.D_RELEASE_DATE,
            REQUEST_ITEM.ID_PRODUCT,
            PRODUCTS.S_CODE,
            PRODUCTS.S_CAPTION,
            PRODUCTS.N_LATHE_TIME,
            PRODUCTS.N_MILLING_TIME,
            REQUEST_ITEM.F_QUANTITY,
            REQUEST_ITEM.F_QUANTITY_EXEC
        )
            .from(REQUEST_ITEM)
            .innerJoin(REQUEST).on(REQUEST.ID.eq(REQUEST_ITEM.ID_REQUEST))
            .innerJoin(REQUEST_STATES).on(REQUEST_STATES.S_CODE.eq(REQUEST.S_STATE_CODE))
            .innerJoin(CONTRACTOR).on(CONTRACTOR.ID.eq(REQUEST.ID_CONTRACTOR))
            .innerJoin(PRODUCTS).on(PRODUCTS.ID.eq(REQUEST_ITEM.ID_PRODUCT))
            .where(REQUEST_ITEM.ID_REQUEST.eq(requestId))
            .and(REQUEST_ITEM.ID.eq(itemId))
            .fetchOne { record ->
                CrmRequestItemDto(
                    id = record.get(REQUEST_ITEM.ID),
                    request = CrmRequestDto(
                        id = record.get(REQUEST.ID),
                        number = record.get(REQUEST.S_NUMBER),
                        date = record.get(REQUEST.D_DATE),
                        contractor = ContractorDto(
                            id = record.get(REQUEST.ID_CONTRACTOR),
                            inn = record.get(CONTRACTOR.S_INN),
                            caption = record.get(CONTRACTOR.S_CAPTION)
                        ),
                        description = record.get(REQUEST.S_DESCRIPTION) ?: "",
                        state = CrmRequestStateDto(
                            code = record.get(REQUEST.S_STATE_CODE),
                            caption = record.get(REQUEST_STATES.S_CAPTION)
                        ),
                        releaseDate = record.get(REQUEST.D_RELEASE_DATE)
                    ),
                    product = ProductDto(
                        id = record.get(REQUEST_ITEM.ID_PRODUCT),
                        code = record.get(PRODUCTS.S_CODE),
                        caption = record.get(PRODUCTS.S_CAPTION),
                        latheTime = record.get(PRODUCTS.N_LATHE_TIME).toLong(),
                        millingTime = record.get(PRODUCTS.N_MILLING_TIME).toLong()
                    ),
                    quantity = record.get(REQUEST_ITEM.F_QUANTITY).toLong(),
                    quantityExec = record.get(REQUEST_ITEM.F_QUANTITY_EXEC).toLong()
                )
            } ?: CrmRequestItemDto();
    }

    fun setRequestState(requestId: Long, state: CrmRequestStates) {
        dsl.transaction { config ->
            request_SetStateCode(config, requestId, state.code, false);
            request_AfterEdit(config, requestId);
        }
    }

    fun addRequestItemExecutionQty(requestId: Long, itemId: Long, addingQuantity: Long): CrmRequestItemDto {
        dsl.transaction { config ->
            val reqItem: CrmRequestItemDto = getCrmRequestItem(requestId, itemId);
            requestItem_SetQuantityExec(config, itemId, (reqItem.quantityExec + addingQuantity).toBigDecimal(), false);
            requestItem_AfterEdit(config, itemId);
            request_AfterEdit(config, requestId);
        }
        return getCrmRequestItem(requestId, itemId);
    }
}