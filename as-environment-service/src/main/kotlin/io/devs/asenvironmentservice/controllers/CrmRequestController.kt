package io.devs.asenvironmentservice.controllers

import io.devs.asenvironmentservice.dto.CrmRequestDto
import io.devs.asenvironmentservice.dto.CrmRequestItemDto
import io.devs.asenvironmentservice.enums.CrmRequestStates
import io.devs.asenvironmentservice.services.CrmRequestService
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
@RequestMapping("crm/requests")
class CrmRequestController(private val crmRequestService: CrmRequestService) {

    @GetMapping
    fun getAllRequests(): List<CrmRequestDto> {
        return crmRequestService.getAllCrmRequests();
    }

    @GetMapping("active")
    fun getActiveRequests(): List<CrmRequestDto> {
        return crmRequestService.getActiveCrmRequests();
    }

    @GetMapping("{requestId}")
    fun getRequest(@PathVariable("requestId") requestId: Long): CrmRequestDto {
        return crmRequestService.getCrmRequest(requestId);
    }

    @GetMapping("{requestId}/items")
    fun getRequestItems(@PathVariable("requestId") requestId: Long): List<CrmRequestItemDto> {
        return crmRequestService.getCrmRequestItems(requestId);
    }

    @GetMapping("{requestId}/items/{itemId}")
    fun getRequestItem(
        @PathVariable("requestId") requestId: Long,
        @PathVariable("itemId") itemId: Long
    ): CrmRequestItemDto {
        return crmRequestService.getCrmRequestItem(requestId, itemId);
    }

    @PostMapping("{requestId}/set-state/in-production")
    fun setStateInProduction(@PathVariable("requestId") requestId: Long) {
        return crmRequestService.setRequestState(requestId, CrmRequestStates.IN_PRODUCTION);
    }

    @PostMapping("{requestId}/set-state/closed")
    fun setStateClosed(@PathVariable("requestId") requestId: Long) {
        return crmRequestService.setRequestState(requestId, CrmRequestStates.CLOSED);
    }

    @PutMapping("{requestId}/items/{itemId}/add-execution-qty/{addingQuantity}")
    fun addRequestItemExecutionQty(
        @PathVariable("requestId") requestId: Long,
        @PathVariable("itemId") itemId: Long,
        @PathVariable("addingQuantity") addingQuantity: Long
    ): CrmRequestItemDto {
        return crmRequestService.addRequestItemExecutionQty(requestId, itemId, addingQuantity);
    }
}