package com.zh.controller;

import com.zh.service.GameService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@Setter(onMethod_ = {@Autowired})
public class GameController {
    private GameService gameService;

    @PostMapping("/startGame/{userId}")
    public void startGame(@PathVariable String userId) {
        gameService.startGame(userId);
    }

    @PostMapping("/endGame/{userId}")
    public void endGame(@PathVariable String userId) {
        gameService.endGame(userId);
    }
}