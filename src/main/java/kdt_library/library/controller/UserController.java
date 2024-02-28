package kdt_library.library.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kdt_library.library.service.UserService;
import kdt_library.library.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class UserController {
    @Autowired
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) { // 인증된 사용자의 정보를 보여줌
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String id = (String) authentication.getPrincipal();
            // token에 저장되어 있는 인증된 사용자의 id 값

            UserVo userVo = userService.getUserById(id);
            if (userVo != null) {
                userVo.setPw(null); // password는 보이지 않도록 null로 설정
                model.addAttribute("user", userVo);
                return "home";
            }
        }
        // 사용자가 인증되지 않은 경우 또는 사용자 정보를 가져올 수 없는 경우
        return "redirect:/login"; // 로그인 페이지로 리다이렉트 또는 다른 처리
    }

    @GetMapping("/userList")
    public String getUserList(Model model) { // User 테이블의 전체 정보를 보여줌
        List<UserVo> userList = userService.getUserList();
        model.addAttribute("list", userList);
        return "userListPage";
    }

    @GetMapping("/login")
    public String loginPage() { // 로그인되지 않은 상태이면 로그인 페이지를, 로그인된 상태이면 home 페이지를 보여줌
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return "loginPage";
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signupPage() {  // 회원 가입 페이지
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return "signupPage";
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String signup(UserVo userVo) { // 회원 가입
        try {
            userService.signup(userVo);
        } catch (DuplicateKeyException e) {
            return "redirect:/signup?error_code=-1";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/signup?error_code=-99";
        }
        return "redirect:/login";
    }

    /*
    @GetMapping("/update")
    public String editPage(Model model) { // 회원 정보 수정 페이지
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserVo userVo = userService.getUserById(id);
        model.addAttribute("user", userVo);
        return "editPage";
    }

    @PostMapping("/update")
    public String edit(UserVo userVo) { // 회원 정보 수정
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userVo.setId(id);
        userService.edit(userVo);
        return "redirect:/";
    }*/
    @PostMapping("/update")
    public String updateUser(UserVo userVo, RedirectAttributes redirectAttributes) { // 회원 정보 수정
        if (userVo.getPw().isEmpty()) {
            // 비밀번호를 입력하지 않았을 경우
            redirectAttributes.addFlashAttribute("error", "비밀번호를 입력하세요.");
            return "redirect:/update";
        }
        userService.edit(userVo);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String editPage(Model model) { // 회원 정보 수정 페이지
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserVo userVo = userService.getUserById(id);
        model.addAttribute("user", userVo);
        return "editPage";
    }
    /*
    @PostMapping("/delete")
    public String withdraw(HttpSession session) { // 회원 탈퇴
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (id != null) {
            userService.withdraw(id);
        }
        SecurityContextHolder.clearContext(); // SecurityContextHolder에 남아있는 token 삭제
        return "redirect:/";
    }*/


    @PostMapping("/delete")
    public String withdraw(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (id != null) {
            userService.withdraw(id);
        }
        SecurityContextHolder.clearContext();

        // 사용자를 로그아웃하고 로그인 페이지로 리다이렉트
        new SecurityContextLogoutHandler().logout(request, response, null);

        redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

}