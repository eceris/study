package com.eceris.member;

import com.eceris.domain.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MemberController {

    @GetMapping("/")
    public Member get() {
        return new Member(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "");
    }
}
