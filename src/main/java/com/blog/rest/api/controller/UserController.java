package com.blog.rest.api.controller;

import com.blog.rest.api.entity.user.User;
import com.blog.rest.api.payload.user.UserInfoRequest;
import com.blog.rest.api.payload.ApiResponse;
import com.blog.rest.api.payload.user.UserIdentityAvailability;
import com.blog.rest.api.payload.user.UserProfile;
import com.blog.rest.api.payload.user.UserSummary;
import com.blog.rest.api.security.CurrentUser;
import com.blog.rest.api.security.UserPrincipal;
import com.blog.rest.api.service.UserService;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        final UserSummary userSummary = userService.getCurrentUser(currentUser);
        return new ResponseEntity<>(userSummary, HttpStatus.OK);
    }

    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(@RequestParam(value = "username") String username) {
        UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(username);
        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(@RequestParam(value = "email") String email) {
        final UserIdentityAvailability userIdentityAvailability = userService.checkEmailAvailability(email);
        return new ResponseEntity<>(userIdentityAvailability, HttpStatus.OK);
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable(value = "username") String username) {
        final UserProfile userProfile = userService.getUserProfile(username);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(
            @Valid @RequestBody User newUser,
            @PathVariable(value = "username") String username,
            @CurrentUser UserPrincipal currentUser) {

        User updatedUSer = userService.updateUser(newUser, username, currentUser);
        return new ResponseEntity<>(updatedUSer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable(value = "username") String username,
            @CurrentUser UserPrincipal currentUser) {

        ApiResponse apiResponse = userService.deleteUser(username, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/giveAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(value = "username") String username) {
        ApiResponse apiResponse = userService.giveAdmin(username);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{username}/removeAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> removeAdmin(@PathVariable(value = "username") String username) {
        ApiResponse apiResponse = userService.removeAdmin(username);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/setOrUpdateInfo")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserProfile> setAddress(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody UserInfoRequest userInfoRequest) {

        UserProfile userProfile = userService.setOrUpdateInfo(currentUser, userInfoRequest);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

}
