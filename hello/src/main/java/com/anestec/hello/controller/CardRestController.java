package com.anestec.hello.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anestec.hello.model.CardPosition;
import com.anestec.hello.service.CardService;

/**
 * カードREST APIリクエストを処理するコントローラー
 */
@RestController
@RequestMapping("/api/cards")
public class CardRestController {

    @Autowired
    private CardService cardService;

    /**
     * カード位置を保存するAPI
     *
     * @param positionData カードIDと位置のデータを含む
     * @return 結果
     */
    @PostMapping("/position")
    public ResponseEntity<Map<String, Object>> saveCardPosition(@RequestBody Map<String, Object> positionData) {
        // データリクエスト
        String cardId = (String) positionData.get("cardId");
        Double positionX = ((Number) positionData.get("positionX")).doubleValue();
        Double positionY = ((Number) positionData.get("positionY")).doubleValue();

        // サービス層で位置を保存
        CardPosition savedPosition = cardService.savePosition(cardId, positionX, positionY);

        // リスポンス成功
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "カード位置を保存しました");
        response.put("data", savedPosition);

        return ResponseEntity.ok(response);
    }

    /**
     * カード位置を取得するAPI
     *
     * @param cardId カードID
     * @return カード位置情報
     */
    @GetMapping("/position/{cardId}")
    public ResponseEntity<?> getCardPosition(@PathVariable String cardId) {
        CardPosition position = cardService.getCardPosition(cardId);

        if (position == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "カード位置情報が見つかりません");
            return ResponseEntity.ok(error);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", position);

        return ResponseEntity.ok(response);
    }

    /**
     * カード位置を削除するAPI
     *
     * @param cardId カードID
     * @return 結果
     */
    @DeleteMapping("/position/{cardId}")
    public ResponseEntity<Map<String, Object>> deleteCardPosition(@PathVariable String cardId) {
        boolean deleted = cardService.deleteCardPosition(cardId);

        Map<String, Object> response = new HashMap<>();

        if (deleted) {
            response.put("status", "success");
            response.put("message", "カード位置を削除しました");
        } else {
            response.put("status", "error");
            response.put("message", "カード位置を削除できませんでした，存在しないかもしれません");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * カードの位置をリセット
     *
     * @return 位置リセット結果
     */
    @PostMapping("/reset-all")
    public ResponseEntity<Map<String, Object>> resetAllCardPositions() {
        cardService.resetAllCardPositions();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "すべてのカードの位置をリセットしました");

        return ResponseEntity.ok(response);
    }
}
