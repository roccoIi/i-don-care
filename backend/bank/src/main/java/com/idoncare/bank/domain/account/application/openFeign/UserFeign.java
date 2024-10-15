package com.idoncare.bank.domain.account.application.openFeign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.bank.domain.account.dto.comm.RequestHeader;
import com.idoncare.bank.domain.account.dto.req.requestServer.TransactionHistoryListRequest;
import com.idoncare.bank.domain.account.dto.resp.responseClient.UserInfoDto;
import com.idoncare.bank.domain.account.dto.resp.responseServer.TransactionHistoryList;
import com.idoncare.bank.domain.account.repository.openFeign.UserOpenFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFeign {

    private final UserOpenFeign userOpenFeign;
    private final ObjectMapper objectMapper;

    /**
     * list내 UserId와 매칭되는 UserName을 반환한다.
     * @param list UserId 모음
     * @return UserInfoDto
     */
    public UserInfoDto getUserInfoDtoByUserId(List<Long> list) {
        // Header
        //-----;

        // openFeign
        String responseJson = userOpenFeign.getUserNameById(list);

        // return 값
        UserInfoDto responseDto;
        try{
            responseDto = objectMapper.readValue(responseJson, UserInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON processing error", e);
        }

        return responseDto;
    }
}
