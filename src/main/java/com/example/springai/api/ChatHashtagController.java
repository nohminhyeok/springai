package com.example.springai.api;
import org.springframework.web.bind.annotation.*;

import com.example.springai.service.ChatHashtagService;
import java.util.List;
import java.util.Map;

@RestController
public class ChatHashtagController {
 private final ChatHashtagService hashtagService;
 public ChatHashtagController(ChatHashtagService hashtagService) {
     this.hashtagService = hashtagService;
 }

 // GET /chat/{no}/hashtags
 @GetMapping("/chat/{no}/hashtags")
 public List<String> getTags(@PathVariable int no) {
     return hashtagService.getTags(no);
 }

 // POST /chat/{no}/hashtags
 @PostMapping("/chat/{no}/hashtags")
 public Map<String,Object> setTags(@PathVariable int no, @RequestBody Map<String,List<String>> body) {
     List<String> tags = body.get("tags");
     try {
         hashtagService.setTags(no, tags);
         return Map.of("success", true);
     } catch(Exception e) {
         return Map.of("success", false, "message", e.getMessage());
     }
 }
}
