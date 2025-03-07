package website.server.Domain.HealingProgram.AiService.AiRecommend.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import website.server.Domain.HealingProgram.AiService.AiRecommend.DTO.Response.AiResponse;
import website.server.Domain.HealingProgram.AiService.AiRecommend.DTO.Response.AiResponseDetail;
import website.server.Domain.HealingProgram.AiService.AiRecommend.DTO.Response.AiResponseList;
import website.server.Domain.HealingProgram.AiService.AiRecommend.Mapper.AiRecommendMapper;
import website.server.Global.JWT.JwtService;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiRecommendServiceImpl implements AiRecommendService{

    private final ChatClient chatClient;
    private final JwtService jwtService;
    private final AiRecommendMapper aiRecommendMapper;;

    /**
     * AI Letter 메서드
     * @param request 사용자 요청
     * @param content 사용자 메시지
     * @return AI 추천 메시지
     */
    @Override
    public AiResponse postAiRecommend(HttpServletRequest request, String content) {

        /* 사용자 고유번호 반환 */
        Long userNumber = jwtService.extractUserNumberFromRequest(request);

        /* 프롬프트 메시지 작성 */
        String prompt = "너는 사용자의 따뜻한 친구이다.\n" +
                        "현재 사용자의 사연&고민을 듣고 사용자에게 현재 어떠한 행동이 어울리며 도움이 될지를 조언해주는 것이 너의 역할이다.\n" +
                        "사용자를 존중하는 마음으로 12줄 ~15줄 가량 친절하고 평안한 느낌의 존댓말로 답변하라.\n" +
                        "\n" +
                        "사용자의 스토리는 다음과 같다. => ";

        /* AI 연산 */
        String response = chatClient.call(prompt + content);

        /* 제목 추출 */
        String title = chatClient.call(response + " -> 이 상담 내용의 제목을 한줄로 짧게 요약하라. ");

        /* 사용자 고유번호 + AI 추천 메시지 DB에 저장  + 메시지 고유 번호 조회*/
        aiRecommendMapper.saveAiRecommend(userNumber,title,content,response);

        return new AiResponse(userNumber,title,response);
    }

    /**
     * 추천 메시지 리스트 조회 메서드
     * @param request 사용자 요청
     * @return AI 답변 썸네일 리스트
     */
    @Override
    public List<AiResponseList> getAiRecommendList(HttpServletRequest request) {

        /* 사용자 고유번호 반환 */
        Long userNumber = jwtService.extractUserNumberFromRequest(request);

        /* DB에서 AI 답변 썸네일 리스트 반환 */
        return aiRecommendMapper.getAiRecommendList(userNumber);
    }

    /**
     * 추천 메시지 상세 조회 메서드
     * @param AiRecommendMessageId 조회하고자 하는 메시지 고유번호
     * @return 추천 메시지 상세 정보
     */
    @Override
    public AiResponseDetail getAiRecommendDetail(Long AiRecommendMessageId) {

        /* 상세 정보 조회 */
        return aiRecommendMapper.getAiRecommendDetail(AiRecommendMessageId);
    }

    /**
     * 추천 메시지 삭제 메서드
     * @param AiRecommendMessageId 삭제하고자 하는 추천 메시지 고유번호
     */
    @Override
    public void deleteAiRecommend(Long AiRecommendMessageId) {

        /* 추천 메시지 삭제 */
        aiRecommendMapper.deleteAiRecommend(AiRecommendMessageId);

    }


    ////////////////
    /* DUPLICATED */
    ////////////////

    /**
     * 추천 메시지 저장 메서드
     * @param response AI 추천 메시지 + 사용자 고유 번호
     */
//    @Override
//    public void saveAiRecommend(AiResponse response) {
//
//        /* 사용자 고유번호 + AI 추천 메시지 DB에 저장 */
//        aiRecommendMapper.saveAiRecommend(response.userNumber(),
//                                          response.title(),
//                                          response.content());
//
//    }

}
