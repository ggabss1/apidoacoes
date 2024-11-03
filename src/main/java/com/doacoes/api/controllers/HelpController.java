package com.doacoes.api.controllers;


import com.doacoes.api.models.Help;
import com.doacoes.api.payload.request.HelpRequest;
import com.doacoes.api.payload.response.HelpResponse;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.HelpRepository;
import com.doacoes.api.repository.UserRepository;
import com.doacoes.api.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/help")
public class HelpController {
    private static final Logger logger = LoggerFactory.getLogger(HelpController.class);

    @Autowired
    private HelpRepository helpRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{helpId}")
    public ResponseEntity<?> getHelpInfo(@PathVariable Long helpId) {
        if (!helpRepository.existsById(helpId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Help not found!"));
        }
        HelpResponse help = new HelpResponse(helpId, helpRepository.findById(helpId).get().getTitle(), helpRepository.findById(helpId).get().getDescription(), helpRepository.findById(helpId).get().getCep(), helpRepository.findById(helpId).get().getCity(), helpRepository.findById(helpId).get().getUf(), helpRepository.findById(helpId).get().getUrlImage());
        return ResponseEntity.ok(help);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getHelpByUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }

        List<Help> helps = helpRepository.findByUser(userRepository.findById(userId).get());
        List<HelpResponse> helpsResponse = new ArrayList<>();
        for (Help help : helps) {
            helpsResponse.add(new HelpResponse(help.getId(), help.getTitle(), help.getDescription(), help.getCep(), help.getCity(), help.getUf(), help.getUrlImage()));
        }
        return ResponseEntity.ok(helpsResponse);
    }

    @GetMapping("/user/{userId}/inactive")
    public ResponseEntity<?> getInactiveHelpByUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }
        List<Help> helps = helpRepository.findByUserAndIsActiveOrderByCreatedAtDesc(userRepository.findById(userId).get(), false);
        List<HelpResponse> helpsResponse = new ArrayList<>();
        for (Help help : helps) {
            helpsResponse.add(new HelpResponse(help.getId(), help.getTitle(), help.getDescription(), help.getCep(), help.getCity(), help.getUf(), help.getUrlImage()));
        }
        return ResponseEntity.ok(helpsResponse);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<?> getActiveHelpByUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }
        List<Help> helps = helpRepository.findByUserAndIsActiveOrderByCreatedAtDesc(userRepository.findById(userId).get(), true);
        List<HelpResponse> helpsResponse = new ArrayList<>();
        for (Help help : helps) {
            helpsResponse.add(new HelpResponse(help.getId(), help.getTitle(), help.getDescription(), help.getCep(), help.getCity(), help.getUf(), help.getUrlImage()));
        }
        return ResponseEntity.ok(helpsResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllHelps() {
        List<Help> helps = helpRepository.findByIsActive(true);
        List<HelpResponse> helpsResponse = new ArrayList<>();
        for (Help help : helps) {
            helpsResponse.add(new HelpResponse(help.getId(), help.getTitle(), help.getDescription(), help.getCep(), help.getCity(), help.getUf(), help.getUrlImage()));
        }
        return ResponseEntity.ok(helpsResponse);
    }

    @PostMapping
    public ResponseEntity<?> createHelp(@RequestBody HelpRequest helpRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId(); // Assuming you have a UserDetailsImpl class

        if (!userRepository.existsById(helpRequest.getUser_id())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }

        if (authUserId != helpRequest.getUser_id()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only create a help for yourself!"));
        }

        Help help = new Help(userRepository.findById(helpRequest.getUser_id()).get(), helpRequest.getTitle(), helpRequest.getDescription(), helpRequest.getCep(), helpRequest.getCity(), helpRequest.getUf(), helpRequest.getUrlImage());
        helpRepository.save(help);
        return ResponseEntity.ok(new MessageResponse("Help created successfully!"));
    }

    @PutMapping("/{helpId}/deactivate")
    public ResponseEntity<?> deactivateHelp(@PathVariable Long helpId) {
        if (!helpRepository.existsById(helpId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Help not found!"));
        }
        Optional<Help> help = helpRepository.findById(helpId);
        help.get().setIsActive(false);
        helpRepository.save(help.get());
        return ResponseEntity.ok(new MessageResponse("Help deactivated successfully!"));
    }

    @PutMapping("/{helpId}")
    public ResponseEntity<?> updateHelp(@PathVariable Long helpId, @RequestBody HelpRequest helpRequest){
        if (!helpRepository.existsById(helpId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Help not found!"));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        if (authUserId != helpRequest.getUser_id()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can only update a help for yourself!"));
        }

        Optional<Help> help = helpRepository.findById(helpId);
        help.get().setTitle(helpRequest.getTitle());
        help.get().setDescription(helpRequest.getDescription());
        help.get().setCep(helpRequest.getCep());
        help.get().setCity(helpRequest.getCity());
        help.get().setUf(helpRequest.getUf());
        help.get().setUrlImage(helpRequest.getUrlImage());

        helpRepository.save(help.get());
        return ResponseEntity.ok(new MessageResponse("Help updated successfully!"));
    }

}