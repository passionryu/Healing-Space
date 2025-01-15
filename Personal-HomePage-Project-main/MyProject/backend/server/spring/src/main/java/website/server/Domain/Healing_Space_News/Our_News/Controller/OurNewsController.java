package website.server.Domain.Healing_Space_News.Our_News.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import website.server.Domain.Healing_Space_News.Our_News.DTO.Request.PostNewsRequest;
import website.server.Domain.Healing_Space_News.Our_News.Service.OurNewsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ournews")
@Tag(name = "Our News", description = "Our News API")
public class OurNewsController {

    private final OurNewsService ourNewsService;

    /* ADMIN FUNCTION */

    /**
     * 게시글 업로드 API
     * @param request 사용자 요청
     * @param file 이미지 파일
     * @param title 게시글 제목
     * @param content 게시글 본문
     * @return 성공 메시지
     */
    @Operation(summary = "", description = "")
    @PostMapping("/")
    public ResponseEntity<String> postNews(HttpServletRequest request,
                                           @RequestParam("file") MultipartFile file, // 이미지 파일 받기
                                           @RequestParam("title") String title,
                                           @RequestParam("content") String content){

        /* DTO 등록 */
        PostNewsRequest postNewsRequest = new PostNewsRequest(title, content, file);

        /* 게시글 업로드 서비스 호출 */
        ourNewsService.postNews(request, postNewsRequest);

        return ResponseEntity.ok("게시글 업로드 완료");
    }

    // 게시글 삭제

    // 게시글 수정 - Ver2 to be continue

    /* USER FUNCTION  */

    // 게시글 조회
    // 게시글 조회 시 조회수 증가
    // 게시글에 댓글 달기 기능
    // 게시글 댓글 수정 기능
    // 게시글 댓글 삭제 기능



}