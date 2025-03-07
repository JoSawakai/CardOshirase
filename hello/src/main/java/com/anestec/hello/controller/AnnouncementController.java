package com.anestec.hello.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anestec.hello.model.Announcement;
import com.anestec.hello.model.Category;
import com.anestec.hello.service.AnnouncementService;

@Controller
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/")
    public String startPage(@RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password) {
        return "login";
    }

    @GetMapping("/oshirase")
    public String getUsers(Model model, //画面から以下のパラメーターをリクエスト
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "registrationDate", required = false) String registrationDate,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "page", defaultValue = "0") int page, // デフォルト：0ページ
            @RequestParam(value = "size", defaultValue = "10") int size) {// 1ページに10件レコード

        Pageable pageable = PageRequest.of(page, size);
        Page<Announcement> oshiraselist;

        // 年月日をフォーマット
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String regDate = null;
        if (registrationDate != null && !registrationDate.isEmpty()) {
            LocalDate parsedDate = LocalDate.parse(registrationDate, inputDateFormatter);
            regDate = parsedDate.format(outputDateFormatter);
        }
        String sDate = null;
        if (startDate != null && !startDate.isEmpty()) {
            LocalDate parsedDate = LocalDate.parse(startDate, inputDateFormatter);
            sDate = parsedDate.format(outputDateFormatter);
        }
        String eDate = null;
        if (endDate != null && !endDate.isEmpty()) {
            LocalDate parsedDate = LocalDate.parse(endDate, inputDateFormatter);
            eDate = parsedDate.format(outputDateFormatter);
        }

        if ((title == null || title.isBlank()) && (category == null || category.isBlank())
                && regDate == null && sDate == null && eDate == null) {
            oshiraselist = announcementService.getAllOshirase(pageable);
        } else {
            oshiraselist = announcementService.searchAnnouncement(title, category, regDate, sDate, eDate, pageable);
        }

        List<Category> kubunList = announcementService.getAllKubun();

        // 年月日を yyyyMMdd から yyyy-MM-dd に変換
        String regDay = formatBackendDate(regDate);
        String startDay = formatBackendDate(sDate);
        String endDay = formatBackendDate(eDate);

        model.addAttribute("oshirase", oshiraselist.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", oshiraselist.getTotalPages());
        model.addAttribute("totalItems", oshiraselist.getTotalElements());

        model.addAttribute("title", title);
        model.addAttribute("size", size);
        model.addAttribute("category", category); //漢字表記
        model.addAttribute("registrationDate", regDay);
        model.addAttribute("startDate", startDay);
        model.addAttribute("endDate", endDay);

        model.addAttribute("regDayforTable", regDate);
        model.addAttribute("startDayforTable", sDate);
        model.addAttribute("endDayforTable", eDate);

        model.addAttribute("allCategory", kubunList); // セレクトボックス内容
        return "oshiraselist";
    }

    @PostMapping("/oshirase/register")
    public String registerAnnouncement(
            @RequestParam("dialogTitle") String title,
            @RequestParam("dialogCategory") String dialogCategory,
            @RequestParam("dialogInfomessage") String infomessage,
            @RequestParam("dialogRegistrationDate") String dialogRegistrationDate,
            @RequestParam("dialogStartDate") String dialogStartDate,
            @RequestParam("dialogEndDate") String dialogEndDate,
            @RequestParam(value = "searchTitle", required = false) String searchTitle,
            @RequestParam(value = "searchCategory", required = false) String searchCategory,
            @RequestParam(value = "searchRegistrationDate", required = false) String searchRegistrationDate,
            @RequestParam(value = "searchStartDate", required = false) String searchStartDate,
            @RequestParam(value = "searchEndDate", required = false) String searchEndDate,
            RedirectAttributes redirectAttributes) {

        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String regDate = LocalDate.parse(dialogRegistrationDate, inputDateFormatter).format(outputDateFormatter);
        String sDate = LocalDate.parse(dialogStartDate, inputDateFormatter).format(outputDateFormatter);
        String eDate = LocalDate.parse(dialogEndDate, inputDateFormatter).format(outputDateFormatter);

        //固定値
        // String deleteFlg = "0";
        String createUser = "admin";
        String updateUser = "admin";

        // データベースにデータを保存　announcementService。saveAnnouncemen.saveAnnouncementを呼び出し
        announcementService.saveAnnouncement(title, dialogCategory, regDate, sDate, eDate,
                infomessage, createUser, updateUser);
        // announcementService.saveAnnouncement(title, dialogCategory, regDate, sDate, eDate,
        // infomessage, deleteFlg, createUser, updateUser);
        // System.out.println("Title: " + "  " + title);
        // System.out.println("Category: " + "  " + dialogCategory);
        // System.out.println("RegistrationDate: " + "  " + regDate);
        // System.out.println("StartDate: " + "  " + sDate);
        // System.out.println("EndDate: " + "  " + eDate);

        // リダイレクト
        redirectAttributes.addAttribute("title", searchTitle);
        redirectAttributes.addAttribute("category", searchCategory);
        redirectAttributes.addAttribute("registrationDate", searchRegistrationDate);
        redirectAttributes.addAttribute("startDate", searchStartDate);
        redirectAttributes.addAttribute("endDate", searchEndDate);
        return "redirect:/oshirase";
    }

    @PostMapping("/oshirase/update")
    public String updateAnnouncement(
            @RequestParam("id_upd") Long id,
            @RequestParam("dialogTitle_upd") String title,
            @RequestParam("dialogCategory_upd") String category_upd,
            @RequestParam("dialogInfomessage_upd") String infomessage_upd,
            @RequestParam("dialogRegistrationDate_upd") String registrationDate_upd,
            @RequestParam("dialogStartDate_upd") String startDate_upd,
            @RequestParam("dialogEndDate_upd") String endDate_upd,
            @RequestParam(value = "searchTitle", required = false) String searchTitle,
            @RequestParam(value = "searchCategory", required = false) String searchCategory,
            @RequestParam(value = "searchRegistrationDate", required = false) String searchRegistrationDate,
            @RequestParam(value = "searchStartDate", required = false) String searchStartDate,
            @RequestParam(value = "searchEndDate", required = false) String searchEndDate,
            RedirectAttributes redirectAttributes) {

        // System.out.println("Title: " + "  " + title);
        // System.out.println("Category: " + "  " + category_upd);
        // System.out.println("RegistrationDate: " + "  " + registrationDate_upd);
        // System.out.println("StartDate: " + "  " + startDate_upd);
        // System.out.println("EndDate: " + "  " + endDate_upd);
        // 年月日をフォーマット
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String regDate = LocalDate.parse(registrationDate_upd, inputDateFormatter).format(outputDateFormatter);
        String sDate = LocalDate.parse(startDate_upd, inputDateFormatter).format(outputDateFormatter);
        String eDate = LocalDate.parse(endDate_upd, inputDateFormatter).format(outputDateFormatter);

        announcementService.updateAnnouncement(id, title, category_upd, infomessage_upd, regDate, sDate, eDate);

        // System.out.println("Title: " + "  " + title);
        // System.out.println("Category: " + "  " + category_upd);
        // System.out.println("RegistrationDate: " + "  " + regDate);
        // System.out.println("StartDate: " + "  " + sDate);
        // System.out.println("EndDate: " + "  " + eDate);
        // リダイレクト
        redirectAttributes.addAttribute("title", searchTitle);
        redirectAttributes.addAttribute("category", searchCategory);
        redirectAttributes.addAttribute("registrationDate", searchRegistrationDate);
        redirectAttributes.addAttribute("startDate", searchStartDate);
        redirectAttributes.addAttribute("endDate", searchEndDate);
        return "redirect:/oshirase";
    }

    @PostMapping("/oshirase/delete")
    public String deleteAnnouncement(
            @RequestParam("id_del") Long id_del,
            @RequestParam(value = "searchTitle", required = false) String searchTitle,
            @RequestParam(value = "searchCategory", required = false) String searchCategory,
            @RequestParam(value = "searchRegistrationDate", required = false) String searchRegistrationDate,
            @RequestParam(value = "searchStartDate", required = false) String searchStartDate,
            @RequestParam(value = "searchEndDate", required = false) String searchEndDate,
            RedirectAttributes redirectAttributes) {
        // 画面上選択したレコードを論理削除
        announcementService.deleteAnnouncement(id_del);

        // // リダイレクト
        redirectAttributes.addAttribute("title", searchTitle);
        redirectAttributes.addAttribute("category", searchCategory);
        redirectAttributes.addAttribute("registrationDate", searchRegistrationDate);
        redirectAttributes.addAttribute("startDate", searchStartDate);
        redirectAttributes.addAttribute("endDate", searchEndDate);
        return "redirect:/oshirase";
    }

    String formatBackendDate(String date) {
        if (date != null && !date.isEmpty()) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        }
        return null;
    }

}
