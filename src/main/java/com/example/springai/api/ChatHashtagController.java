package com.example.springai.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.service.ChatHashtagService;

// 해시태그 관련 REST API 컨트롤러
@RestController
public class ChatHashtagController {
	// 해시태그 서비스 의존성 주입
	private final ChatHashtagService hashtagService;

	/**
	 * 생성자: 해시태그 서비스 주입
	 * @param hashtagService 해시태그 관련 서비스
	 */
	public ChatHashtagController(ChatHashtagService hashtagService) {
		this.hashtagService = hashtagService;
	}

	/**
	 * 특정 대화(chatNo)의 해시태그 목록 조회
	 */
	@GetMapping("/chat/{no}/hashtags")
	public List<String> getTags(@PathVariable int no) {
		return hashtagService.getTags(no);
	}

	/**
	 * 특정 대화(chatNo)에 해시태그 추가/저장
	 */
	@PostMapping("/chat/{no}/hashtags")
	public Map<String, Object> setTags(@PathVariable int no, @RequestBody Map<String, List<String>> body) {
		List<String> tags = body.get("tags");
		try {
			hashtagService.setTags(no, tags);
			return Map.of("success", true);
		} catch (Exception e) {
			return Map.of("success", false, "message", e.getMessage());
		}
	}
	
    /**
     * 인기 해시태그 TOP10 조회
     */
    @GetMapping("/hashtags/popular")
    public List<Map<String,Object>> getPopularTags() {
        return hashtagService.selectPopularTags();
    }

    /**
     * 전체 해시태그 목록 조회
     */
    @GetMapping("/hashtags/list")
    public List<Map<String,Object>> getAllTags() {
        return hashtagService.selectAllTags();
    }

    /**
     * 특정 해시태그가 달린 채팅 목록 조회
     */
    @GetMapping("/chat/list/by-tag")
    public List<Map<String, Object>> getChatsByTag(@RequestParam("tag") String tagText) {
        List<Map<String, Object>> list = hashtagService.getChatsByTag(tagText);
        for (Map<String, Object> row : list) {
            Object hashtags = row.get("hashtags");
            if (hashtags instanceof String && !((String) hashtags).isEmpty()) {
                row.put("hashtags", Arrays.asList(((String) hashtags).split(",")));
            } else {
                row.put("hashtags", List.of());
            }
        }
        return list;
    }

    /**
     * 전체 채팅 목록(해시태그 포함) 조회
     */
    @GetMapping("/chat/list/all")
    public List<Map<String, Object>> getAllChatsWithTags() {
        List<Map<String, Object>> list = hashtagService.getAllChatsWithTags();
        for (Map<String, Object> row : list) {
            Object hashtags = row.get("hashtags");
            if (hashtags instanceof String && !((String) hashtags).isEmpty()) {
                row.put("hashtags", Arrays.asList(((String) hashtags).split("\\s*,\\s*")));
            } else {
                row.put("hashtags", List.of());
            }
        }
        return list;
    }
}
