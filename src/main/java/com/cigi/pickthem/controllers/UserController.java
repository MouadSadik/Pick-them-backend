package com.cigi.pickthem.controllers;

import com.cigi.pickthem.domain.dtos.UserRequestDto;
import com.cigi.pickthem.domain.dtos.UserResponseDto;
import com.cigi.pickthem.domain.dtos.UserUpdateRequestDto;
import com.cigi.pickthem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        UserResponseDto updatedUser = userService.updateUser(userId, requestDto);
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

}
