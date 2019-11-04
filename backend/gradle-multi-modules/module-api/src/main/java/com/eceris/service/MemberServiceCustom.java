package com.eceris.service;

import com.eceris.domain.Member;
import com.eceris.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceCustom {

    private MemberRepository memberRepository;

    public MemberServiceCustom(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long signup (Member member) {
        return memberRepository.save(member).getId();
    }
}
