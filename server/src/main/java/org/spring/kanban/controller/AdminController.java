package org.spring.kanban.controller;

import org.bson.types.ObjectId;
import org.spring.kanban.configuration.CurrentUser;
import org.spring.kanban.configuration.UserPrincipal;
import org.spring.kanban.domain.KanbanBoard;
import org.spring.kanban.domain.User;
import org.spring.kanban.payload.ApiResponse;
import org.spring.kanban.payload.SuccessResponse;
import org.spring.kanban.payload.UserSummaryResponse;
import org.spring.kanban.service.KanbanBoardService;
import org.spring.kanban.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/admin")

public class AdminController {

    private final UserService userService;
    private final KanbanBoardService kanbanBoardService;

    public AdminController(UserService userService, KanbanBoardService kanbanBoardService) {
        this.userService = userService;
        this.kanbanBoardService = kanbanBoardService;
    }

    /**
     * Get detailsa bout the logged in user
     * @param currentUser
     * @return
     */
    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserSummaryResponse> currentUser(@CurrentUser UserPrincipal currentUser) {
        return new ResponseEntity<UserSummaryResponse>(this.userService.currentUser(currentUser), HttpStatus.OK);
    }

    /**
     * Retrive a list of users
     * @return
     */
    @GetMapping(value = "/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<List<User>>(this.userService.findAll(), HttpStatus.OK);


    }

    /**
     * Delete a certain user
     * @param username
     * @param currentUser
     * @return
     */
    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteByUsername(@PathVariable("username") String username,
                                                  @CurrentUser UserPrincipal currentUser) {
//        this.userService.findByUsername((username));
        this.userService.deleteByUsername(username, currentUser);
        return new ResponseEntity<>(new SuccessResponse("User Deleted Successfully"), HttpStatus.OK);
    }

    /**
     * Retrive all boards from a certain user
     * @param username
     * @param currentUser
     * @return
     */
    @GetMapping(value = "/{username}/boards")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<KanbanBoard>> findAllBoards(@PathVariable("username") String username,
                                                           @CurrentUser UserPrincipal currentUser) {
        return new ResponseEntity<>(this.kanbanBoardService.findAllByCreatedBy(username, currentUser), HttpStatus.OK);
    }
}
