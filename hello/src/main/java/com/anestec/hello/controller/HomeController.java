package com.anestec.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホーム画面のコントローラー
 */
@Controller
public class HomeController {

    /**
     * ホーム画面
     *
     * @return ホーム画面のビュー名
     */
    @GetMapping("/cardindex")
    public String home() {
        return "cardIndex";
    }
}
