package com.project.backend.service;

import com.project.backend.entity.Bot;
import com.project.backend.repository.BotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotService {

    private final BotRepository botRepository;

    public BotService(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    public Bot save(Bot bot) {
        return botRepository.save(bot);
    }

    public List<Bot> getAll() {
        return botRepository.findAll();
    }
}