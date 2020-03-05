package com.example.demo.controller;


import com.example.demo.dto.AccessTokenDTO;
import com.example.demo.dto.GithubUserDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.provider.GithubProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    //读取application配置文件中定义的值
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired(required = false)
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response){
        //生成AccessTokenDTO对象，传到githubprovider的getAccesssToken方法中获取accesssToken
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();    //command option +v 快速生成new
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accesssToken = githubProvider.getAccesssToken(accessTokenDTO);
        //根据获取到的accessToken传到githubprovider的getUser方法中获取用户信息
        GithubUserDTO gitHubUser = githubProvider.getUser(accesssToken);
        if(gitHubUser != null && gitHubUser.getId() != null){
            //建立一个用户对象，将获取到数据塞到对象中，然后调用userMapper插入数据库
            User user = new User();
            final String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setAvatarUrl(gitHubUser.getAvatar_url());
            userService.createOrUpdate(user);
            //手动将自定义的token写入cookie中
            response.addCookie(new Cookie("token",token));
            //登陆成功，写cookie和session
            //request.getSession().setAttribute("user",gitHubUser); //将user放入session
            return "redirect:/";
        }else{
            //登陆失败，重新登陆
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    private String logout(HttpServletRequest request,
                          HttpServletResponse response){
        //清除session
        request.getSession().removeAttribute("user");
        //获取cookie在设置只为空
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
