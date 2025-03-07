package com.anestec.hello.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.anestec.hello.service.CardInfoService;

/**
 * お知らせカードのコントローラー
 */
@Controller
public class CardController {

    @Autowired
    private CardInfoService cardInfoService;

    /**
     * カードページを表示
     *
     * @param model Spring MVC モデル
     * @return カードページのビュー名
     */
    @GetMapping("/cards")
    public String showCardPage(Model model) {
        // すべてのカードの内容を取得
        Map<String, String> cardContents = cardInfoService.getAllCardContents();

        // すべてのカードの詳細内容を取得
        Map<String, String> cardDetails = cardInfoService.getAllCardDetails();

        // カードの内容をモデルに追加
        model.addAttribute("cardContents", cardContents);
        model.addAttribute("cardDetails", cardDetails);

        return "cards"; // templates/cards.htmlと一致
    }
}
