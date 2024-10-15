package com.idoncare.bank.domain.account.dto.resp.responseClient;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String code;
    private String message;
    private List<UserInfo> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo{
        private Long userId;
        private String userName;
    }


}