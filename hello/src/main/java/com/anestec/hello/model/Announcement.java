package com.anestec.hello.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ASMS_INFORMATION")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INFORMATION_ID")
    private Long id;

    @Column(name = "INFORMATION_TITLE")
    private String title;

    @Column(name = "INFORMATION_KBN")
    private String category;

    @Column(name = "KEISAI_YMD")
    private String registrationDate;

    @Column(name = "ENABLE_START_YMD")
    private String startDate;

    @Column(name = "ENABLE_END_YMD")
    private String endDate;

    @Column(name = "INFORMATION_NAIYO")
    private String infomessage;

    @Column(name = "DELETE_FLG")
    private String deleteFlg;

    @Column(name = "CREATE_USER_CD")
    private String createUser;

    @CreationTimestamp
    @Column(name = "CREATE_TIME", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "UPDATE_USER_CD")
    private String updateUser;

    @CreationTimestamp
    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;

    @ManyToOne
    @JoinColumn(name = "INFORMATION_KBN", referencedColumnName = "kubun", insertable = false, updatable = false)
    private Category categoryEntity;

    @Transient
    private String regDayforTable;

    @Transient
    private String startDayforTable;

    @Transient
    private String endDayforTable;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInfomessage() {
        return infomessage;
    }

    public void setInfomessage(String infomessage) {
        this.infomessage = infomessage;
    }

    public String getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(String deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    // kubun から displayName　取得
    public String getDisplayName() {
        return categoryEntity != null ? categoryEntity.getDisplayName() : null;
    }

    public String getRegDayforTable() {
        // 日時フォーマット
        if (this.registrationDate != null) {
            return this.registrationDate.substring(0, 4) + "/" + this.registrationDate.substring(4, 6) + "/" + this.registrationDate.substring(6, 8);
        }
        return null;
    }

    public void setRegDayforTable(String regDayforTable) {
        this.regDayforTable = regDayforTable;
    }

    // Getter for startDayforTable
    public String getStartDayforTable() {
        if (this.startDate != null) {
            return this.startDate.substring(0, 4) + "/" + this.startDate.substring(4, 6) + "/" + this.startDate.substring(6, 8);
        }
        return null;
    }

    public void setStartDayforTable(String startDayforTable) {
        this.startDayforTable = startDayforTable;
    }

    // Getter for endDayforTable
    public String getEndDayforTable() {
        if (this.endDate != null) {
            return this.endDate.substring(0, 4) + "/" + this.endDate.substring(4, 6) + "/" + this.endDate.substring(6, 8);
        }
        return null;
    }

    public void setEndDayforTable(String endDayforTable) {
        this.endDayforTable = endDayforTable;
    }

}
