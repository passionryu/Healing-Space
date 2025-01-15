package website.server.Domain.Healing_Space_News.Our_News.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import website.server.Domain.Healing_Space_News.Our_News.DTO.Request.PostNewsRequest;
import website.server.Domain.Healing_Space_News.Our_News.Mapper.OurNewsMapper;
import website.server.Domain.Healing_Space_News.Our_News.Util.OurNews_Set_Image_Path;
import website.server.Global.JWT.JwtService;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OurNewsServiceImpl implements OurNewsService{

    private final OurNewsMapper ourNewsMapper;
    private final JwtService jwtService;
    private final OurNews_Set_Image_Path ourNewsSetImagePath;

    /**
     * 게시글 올리기 API
     * @param request 사용자 요청
     * @param postNewsRequest 게시글 데이터
     */
    @Override
    public void postNews(HttpServletRequest request,
                         PostNewsRequest postNewsRequest) {

        /* 파일 업로드 */
        MultipartFile file = postNewsRequest.file();
        String imgPath = null;

        /* 이미지 파일이 있을 경우 파일을 업로드하고 경로를 저장 */
        if (file != null && !file.isEmpty()) {
            try {
                imgPath = ourNewsSetImagePath.uploadPhoto(file); // 파일 경로를 반환받음
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* 데이터 추출 */
        Long userNumber = jwtService.extractUserNumberFromRequest(request);
        String title = postNewsRequest.title();
        String content = postNewsRequest.content();

        /* 데이터 저장 */
        ourNewsMapper.postNews(userNumber,title,content,imgPath);

    }
}