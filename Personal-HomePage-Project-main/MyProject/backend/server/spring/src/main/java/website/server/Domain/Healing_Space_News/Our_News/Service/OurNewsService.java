package website.server.Domain.Healing_Space_News.Our_News.Service;

import jakarta.servlet.http.HttpServletRequest;
import website.server.Domain.Healing_Space_News.Our_News.DTO.Request.PostNewsRequest;
import website.server.Domain.Healing_Space_News.Our_News.DTO.Response.GetNewsResponse;

public interface OurNewsService {

    /**
     * 게시글 업로드 메서드
     * @param request|사용자 요청
     * @param postNewsRequest 게시글 데이터
     */
    void postNews(HttpServletRequest request, PostNewsRequest postNewsRequest);

    /**
     * 게시글 삭제 메서드
     * @param ourNewsNumber 삭제하고자 하는 게시글의 고유번호
     */
    void deleteNews(Long ourNewsNumber);

    /**
     * 게시글 조회 메서드
     * @param ourNewsNumber 조회하고자 하는 게시글의 고유 번호
     * @return 게시글 정보
     */
    GetNewsResponse getNews(Long ourNewsNumber);

}
