package website.server.Domain.HealingProgram.AiService.AiSnapShot.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;
import website.server.Domain.HealingProgram.AiService.AiSnapShot.DTO.Response.AiResponse;
import website.server.Domain.HealingProgram.AiService.AiSnapShot.DTO.Response.AiResponseDetail;
import website.server.Domain.HealingProgram.AiService.AiSnapShot.DTO.Response.AiResponseList;
import website.server.Domain.HealingProgram.AiService.AiSnapShot.Mapper.AiSnapShotMapper;
import website.server.Domain.HealingProgram.AiService.AiSnapShot.Util.MessageUtil;
import website.server.Domain.HealingProgram.AiService.AiSnapShot.Util.RedisUtil;
import website.server.Global.JWT.JwtService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiSnapShotServiceImpl implements AiSnapShotService {

    private final AiSnapShotMapper aiSnapShotMapper;
    private final MessageUtil messageUtil;
    private final JwtService jwtService;
    private final RedisUtil redisUtil;
    private final ChatClient chatClient;

    /**
     * 사용자 답변 저장 메서드
     * @param request 사용자 요청
     * @param questionNumber 저장할 질문 번호
     */
    @Override
    public void postAiSnapShot(HttpServletRequest request, int questionNumber,String answer) {

        /* 유저 닉네임 조회 */
        String nickName = jwtService.extractNickNameFromRequest(request);

        /* Redis에 데이터 임시 저장 */
        redisUtil.saveAnswerToRedis(nickName,questionNumber,answer);
    }

    /**
     * 답변 최종 회수 후 AI 연산 메서드
     * @param request 사용자 요청
     * @return 답변 레포트
     */
    @Override
    public String getAiResponse(HttpServletRequest request) {

        /* 유저 닉네임 조회 */
        String nickName = jwtService.extractNickNameFromRequest(request);

        /* Redis에 임시 저장된 유저의 모든 답변(7개) 반환 */
        String[] answerArray = redisUtil.getAnswerFromRedis(nickName);

        /* full message 반환 . full message = { 7개의 질문 + 사용자 답변 }모음 */
        String fullMessage = messageUtil.getFullMessage(answerArray);

        /* full request 반환 . full request = full message + prompt message */
        String fullRequest = messageUtil.addFullMessageAndPromptMessage(fullMessage);

        /* 최종 AI 연산 후 결과를 클라이언트에게 반환 */
        String aiResponse = chatClient.call(fullRequest);

        /* Redis에서 유저의 임시 저장된 모든 답변 삭제 */
        redisUtil.deleteAnswerFromRedis(nickName);

        return aiResponse;
    }

    /**
     * 레포트 저장 메서드
     * @param request 사용자 요청
     * @param aiResponse ai 응답 메시지
     */
    @Override
    public void saveAiReport(HttpServletRequest request, AiResponse aiResponse) {

        /* 유저 고유번호 추출 */
        Long userNumber = jwtService.extractUserNumberFromRequest(request);

        /* aiResponse 추출 */
        String aiResponseToString = aiResponse.toString();

        /* aiResponse에서 불필요한 문자열 정제 작업 */
        String purifiedAiResponse = messageUtil.aiResponsePurifier(aiResponseToString);

        /* 응답 메시지의 제목 생성 */
        String title = chatClient.call(purifiedAiResponse) + " -> 이 메시지에 어울리는 제목만 한줄로 출력하라.";

        /* aiResponse를 DB에 저장 */
        aiSnapShotMapper.saveAiReport(userNumber,title,purifiedAiResponse);
    }

    /**
     * 응답 메시지 리스트 조회 메서드
     * @param request 사용자 요청
     * @return 응답 메시지 리스트 반환
     */
    @Override
    public List<AiResponseList> getAiResponseList(HttpServletRequest request) {

        /* 사용자 고유번호 조회 */
        Long userNumber = jwtService.extractUserNumberFromRequest(request);

        /* 응답 메시지 리스트 반환 */
        return aiSnapShotMapper.getAiResponseList(userNumber);
    }

    /**
     * 응답 메시지 상세 조회 메서드
     * @param aiResponseNumber 상세 조회하고자 하는 응답 메시지 고유 번호
     * @return 응답 메시지 상세 데이터 반환
     */
    @Override
    public AiResponseDetail getAiResponseDetail(Long aiResponseNumber) {

        /* 응답 메시지 상세 조회 */
        return aiSnapShotMapper.getAiResponseDetail(aiResponseNumber);
    }

    /**
     * AI응답 메시지 삭제 메서드
     * @param aiResponseNumber 삭제하고자 하는 응답 메시지
     */
    @Override
    public void deleteAiResponse(Long aiResponseNumber) {

        /* AI응답 메시지 삭제 */
        aiSnapShotMapper.deleteAiResponse(aiResponseNumber);

    }

}
