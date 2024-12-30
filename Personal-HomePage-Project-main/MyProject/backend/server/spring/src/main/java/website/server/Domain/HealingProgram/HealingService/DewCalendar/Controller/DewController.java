package website.server.Domain.HealingProgram.HealingService.DewCalendar.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.server.Domain.HealingProgram.HealingService.DewCalendar.DTO.Request.DiaryRequest;
import website.server.Domain.HealingProgram.HealingService.DewCalendar.DTO.Response.DiaryResponse;
import website.server.Domain.HealingProgram.HealingService.DewCalendar.Service.DewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dew")
@Tag(name = "DEW Calendar", description = "Dew 캘린더 서비스 API")
public class DewController {

    private final DewService dewService;

    /**
     * 일기 저장 API
     * @param request 사용자 요청
     * @param diaryRequest 일기 제목,본문
     * @return 응답 DTO (감정,글귀,음악)
     */
    @Operation(summary = " 일기 저장 ", description = " ")
    @PostMapping("/diary")
    public ResponseEntity<DiaryResponse> saveDiary(HttpServletRequest request, @RequestBody DiaryRequest diaryRequest){

        DiaryResponse diaryResponse = dewService.saveDiary(diaryRequest);

        // todo : 날씨 매칭 메서드 - 감정 / (날씨,격려의 메시지)
        // todo : 일기 저장 메서드 -diaryRequest,AiResponse,(날씨,격려의 메시지) / 저장

        // todo : 최종 반환은 AiResponse 와 날씨,격려의 메시지
        return ResponseEntity.ok(diaryResponse);
    }

    /* 일기 조회 */

    /* 일기 수정 */

    /* 일기 삭제 */

}