package org.spring.kanban.controller;

import org.bson.types.ObjectId;
import org.spring.kanban.configuration.CurrentUser;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.payload.ApiResponse;
import org.spring.kanban.payload.CardWithAttachmentsResponse;
import org.spring.kanban.payload.DescriptionRequest;
import org.spring.kanban.payload.NameRequest;
import org.spring.kanban.payload.SuccessResponse;
import org.spring.kanban.service.KanbanCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/card")
public class KanbanCardController {

	private final KanbanCardService kanbanCardService;

	public KanbanCardController(KanbanCardService kanbanCardService) {
		this.kanbanCardService = kanbanCardService;
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<CardWithAttachmentsResponse> findById(@PathVariable("id") ObjectId id,
			@CurrentUser UserPrincipal currentUser) {
		CardWithAttachmentsResponse card = this.kanbanCardService.findCardWithAttachmentsById(id, currentUser);
		return new ResponseEntity<>(card, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteById(@PathVariable("id") ObjectId id,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.deleteById(id, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Card Deleted successfully"), HttpStatus.OK);
	}

	@PostMapping(value = "/{idColumn}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> saveCard(@PathVariable("idColumn") ObjectId idColumn,
			@RequestBody NameRequest name, @CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.saveName(idColumn, name, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Card Saved Successfully"), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> updateName(@PathVariable("id") ObjectId id, @RequestBody NameRequest name,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.updateName(id, name, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Card Updated Successfully"), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}/description")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> UpdateDescription(@PathVariable("id") ObjectId id,
			@RequestBody DescriptionRequest description, @CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.updateDescription(id, description, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Card Description Successfully"), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}/attachment")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> uploadAttachments(@PathVariable("id") ObjectId id,
			@RequestParam("file[]") MultipartFile[] files, @CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.UploadingMultipleFiles(id, files, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Save Attachments Successfully"), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}/attachment/{idAttachment}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteAttachmentByIdCardsByidCard(@PathVariable("id") ObjectId idCard,
			@PathVariable("idAttachment") ObjectId idAttachment, @CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.deleteAttachmentByIdCard(idCard, idAttachment, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Attachment Deleted Successfully"), HttpStatus.OK);
	}

	@PutMapping(value = "/{id}/attachment/{idAttachment}/cover")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> setCover(@PathVariable("id") ObjectId idCard,
			@PathVariable("idAttachment") ObjectId idAttachment, @CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.setCover(idCard, idAttachment, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Cover Updated successfully "), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}/attachment/{idAttachment}/cover")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> removeCover(@PathVariable("id") ObjectId cardId,
			@PathVariable("idAttachment") ObjectId idAttachment, @CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.removeCover(cardId, idAttachment, currentUser);
		return new ResponseEntity<>(new SuccessResponse("Cover Removed Successfully"), HttpStatus.OK);
	}
	
	@PutMapping(value = "/{idColumn}/{currentPosition}/{desiredPosition}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> MovePositions(@PathVariable("idColumn") ObjectId idColumn,@PathVariable("currentPosition") Long currentPosition
			,@PathVariable("desiredPosition") Long desiredPosition,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.moveCardPosition(currentPosition, desiredPosition, idColumn);
		return new ResponseEntity<>(new SuccessResponse("Card Updated Successfully"), HttpStatus.OK);
	}
	
	@PutMapping(value = "/{idColumn}/moveCardToAnotherColumn/{idCurrentCard}/{desiredPosition}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> moveCardToAnotherColumn(@PathVariable("idColumn") ObjectId idColumn,@PathVariable("idCurrentCard") ObjectId idCurrentCard
			,@PathVariable("desiredPosition") Long desiredPosition,
			@CurrentUser UserPrincipal currentUser) {
		this.kanbanCardService.moveCardToAnotherColumn(idCurrentCard, desiredPosition, idColumn);
		return new ResponseEntity<>(new SuccessResponse("Card Updated Successfully"), HttpStatus.OK);
	}

}
