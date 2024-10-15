package com.idoncare.bank.domain.account.repository.openFeign;

import com.idoncare.bank.domain.account.dto.resp.responseClient.UserInfoDto;
import com.idoncare.bank.global.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "UserApi", url = "https://j11a603.p.ssafy.io/api/user/inner", configuration = FeignConfiguration.class)
public interface UserOpenFeign {

    // 계좌번호를 통한 유저 이름 조회
    @GetMapping("/names")
    String getUserNameById(List<Long> list);
}
