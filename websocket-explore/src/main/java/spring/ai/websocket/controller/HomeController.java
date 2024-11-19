package spring.ai.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @since  2023/10
 * @Des
 */
@Controller
public class HomeController {
    /**
     * 跳转到websocketDemo.html页面，携带自定义的cid信息。
     * http://localhost:8300/demo/toWebSocketDemo/user
     *
     * @param cid
     * @param model
     * @return
     */
    @GetMapping("/demo/toWebSocketDemo/{cid}")
    public String toWebSocketDemo(@PathVariable String cid, Model model) {
        model.addAttribute("cid", cid);
        return "index";
    }

    @GetMapping("hello")
    @ResponseBody
    public String hi(HttpServletResponse response) {
        return "Hi";
    }
}