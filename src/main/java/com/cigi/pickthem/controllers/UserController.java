package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.dtos.users.UserResponseDto;
import com.cigi.pickthem.domain.dtos.users.UserUpdateRequestDto;
import com.cigi.pickthem.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @ModelAttribute UserUpdateRequestDto requestDto
    ) {
        UserResponseDto updatedUser = userService.updateUser(id, requestDto);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(
            @PathVariable("id") Long userId
    ) {
        UserResponseDto deletedUser = userService.deleteUser(userId);
        return ResponseEntity.ok(deletedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}/points")
    public ResponseEntity<Integer> getTotalPoints(@PathVariable("id") Long userId) {
        int totalPoints = userService.getTotalPoints(userId);
        return ResponseEntity.ok(totalPoints);
    }

    @PutMapping("/{id}/points")
    public ResponseEntity<UserResponseDto> updateTotalPoints(@PathVariable("id") Long userId) {
        UserResponseDto updatedUser = userService.updateTotalPoints(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/top")
    public ResponseEntity<List<UserResponseDto>> getTopUsers(
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        List<UserResponseDto> topUsers = userService.getTopUsers(limit);
        return ResponseEntity.ok(topUsers);
    }

    @GetMapping("/top3")
    public ResponseEntity<List<UserResponseDto>> getTop3Users(){
        List<UserResponseDto> top3Users = userService.getTop3Users();
        return ResponseEntity.ok(top3Users);
    }

}
