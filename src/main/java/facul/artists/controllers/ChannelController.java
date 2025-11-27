package facul.artists.controllers;

import facul.artists.models.Channel;
import facul.artists.models.User;
import facul.artists.repositories.ChannelRepository;
import facul.artists.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/channels")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChannelController {



    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @GetMapping("/open")
    public ResponseEntity<String> openChannel(
            @RequestParam Long userId,
            @RequestParam String otherUserEmail) {
        

        Optional<User> otherUser = userRepository.findByEmail(otherUserEmail);

        if (otherUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found with this email.");
        }

        Long otherUserId = otherUser.get().getId();

        Long lowerId = Math.min(userId, otherUserId);
        Long higherId = Math.max(userId, otherUserId);

        Optional<Channel> existingChannel = channelRepository.findExistingChannel(lowerId, higherId);

        if (existingChannel.isPresent()) {
            return ResponseEntity.ok(existingChannel.get().getChannelName());
        }

        String newChannelName = "chat_" + lowerId + "_" + higherId;

        Channel newChannel = new Channel();
        newChannel.setUser1Id(lowerId);
        newChannel.setUser2Id(higherId);
        newChannel.setChannelName(newChannelName);

        channelRepository.save(newChannel);

        return ResponseEntity.ok(newChannelName);
    }


   @GetMapping("/list/{userId}")
    public ResponseEntity<List<Map<String, Object>>> listUserChannels(@PathVariable Long userId) {
        
       
        List<Channel> channels = channelRepository.findByUser1IdOrUser2Id(userId, userId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Channel channel : channels) {
            Long partnerId = channel.getUser1Id().equals(userId) ? channel.getUser2Id() : channel.getUser1Id();
            Optional<User> partnerOpt = userRepository.findById(partnerId);

            if (partnerOpt.isPresent()) {
                User partner = partnerOpt.get();
                
                Map<String, Object> channelData = new HashMap<>();
                channelData.put("channelId", channel.getChannelName()); 
                channelData.put("partnerName", partner.getNome());
                channelData.put("partnerEmail", partner.getEmail());
                
                response.add(channelData);
            }
        }

        return ResponseEntity.ok(response);
    }
}