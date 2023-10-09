package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request) {
        AccessTokenDTO dto = new AccessTokenDTO();
        dto.setCode(code);
        dto.setState(state);
        dto.setClient_id(clientId);
        dto.setClient_secret(clientSecret);
        dto.setRedirect_url(redirectUri);
        String accessToken = githubProvider.getAccessToken(dto);
        GithubUser user = githubProvider.getUser(accessToken);

        if (user != null){
            // 登录成功，写cookie 和 session
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        } else {
            // 登录失败，重新登录
            return "redirect:/";
        }
    }
}
