package com.anestec.hello.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anestec.hello.model.Announcement;
import com.anestec.hello.repository.AnnouncementRepository;

/**
 * カード情報実装クラス
 */
@Service
public class CardInfoServiceImpl implements CardInfoService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    public Map<String, String> getAllCardContents() {
        Map<String, String> cardContents = new HashMap<>();

        // すべてのお知らせ情報を取得
        List<Announcement> allAnnouncements = announcementRepository.findAll();

        // 論理削除されていないお知らせを取得
        List<Announcement> activeAnnouncements = allAnnouncements.stream()
                .filter(a -> a.getDeleteFlg() == null || !a.getDeleteFlg().equals("1"))
                .collect(Collectors.toList());

        // 前4件論理削除されていないお知らせを取得
        int count = Math.min(4, activeAnnouncements.size());

        // 4つのカード位置があることを確認
        for (int i = 1; i <= 4; i++) {
            String cardId = String.valueOf(i);

            if (i <= count) {
                // 論理削除されていないお知らせデータを使用
                Announcement announcement = activeAnnouncements.get(i - 1);
                String content = getContentFromAnnouncement(announcement);
                cardContents.put(cardId, content);
            } else {
                // 論理削除されていないお知らせ不足4件の場合、デフォルト内容を使用
                cardContents.put(cardId, "カード " + i);
            }
        }

        return cardContents;
    }

    @Override
    public Map<String, String> getAllCardDetails() {
        Map<String, String> cardDetails = new HashMap<>();

        // すべてのお知らせ情報を取得
        List<Announcement> allAnnouncements = announcementRepository.findAll();

        // 論理削除されていないお知らせを取得
        List<Announcement> activeAnnouncements = allAnnouncements.stream()
                .filter(a -> a.getDeleteFlg() == null || !a.getDeleteFlg().equals("1"))
                .collect(Collectors.toList());

        // 前4件論理削除されていないお知らせを取得
        int count = Math.min(4, activeAnnouncements.size());

        // 4つのカード位置があることを確認
        for (int i = 1; i <= 4; i++) {
            String cardId = String.valueOf(i);

            if (i <= count) {
                // 論理削除されていないお知らせデータを使用
                Announcement announcement = activeAnnouncements.get(i - 1);
                String detail = getDetailFromAnnouncement(announcement);
                cardDetails.put(cardId, detail);
            } else {
                // 論理削除されていないお知らせ不足4件の場合、デフォルト内容を使用
                cardDetails.put(cardId, "なし");
            }
        }

        return cardDetails;
    }

    /**
     * お知らせオブジェクトから内容テキストを抽出
     */
    private String getContentFromAnnouncement(Announcement announcement) {
        // タイトルを設定、タイトルがない場合は内容を設定
        if (announcement.getTitle() != null && !announcement.getTitle().isEmpty()) {
            return announcement.getTitle();
        } else if (announcement.getInfomessage() != null && !announcement.getInfomessage().isEmpty()) {
            return announcement.getInfomessage();
        } else {
            return "お知らせ " + announcement.getId();
        }
    }

    /**
     * お知らせオブジェクトから詳細内容を抽出
     */
    private String getDetailFromAnnouncement(Announcement announcement) {
        // INFORMATION_NAIYO字段を使用
        if (announcement.getInfomessage() != null && !announcement.getInfomessage().isEmpty()) {
            return announcement.getInfomessage();
        } else {
            return "なし";
        }
    }

    @Override
    public String getCardContent(String cardId) {
        try {
            // Long型に変換、findByIdはLong型のパラメータが必要
            Long id = Long.parseLong(cardId);

            // 指定IDのお知らせ情報を検索
            Optional<Announcement> announcementOpt = announcementRepository.findById(id);

            if (announcementOpt.isPresent()) {
                Announcement announcement = announcementOpt.get();

                // 削除フラグを確認
                String deleteFlg = announcement.getDeleteFlg();
                if (deleteFlg != null && deleteFlg.equals("1")) {
                    return "カード " + cardId; // デフォルトテキストを返す、レコードが論理削除されているため
                }

                // お知らせから内容を抽出
                return getContentFromAnnouncement(announcement);
            }
        } catch (NumberFormatException e) {
            // ID変換例外を処理
            return "カード " + cardId;
        }

        // 指定IDのお知らせが見つからない場合、デフォルトテキストを返す
        return "カード " + cardId;
    }
}
