package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        //given
        Member member = createMember();

        //when
        Member savedMember = memberService.saveMember(member);

        //then
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(savedMember.getName()).isEqualTo(member.getName());
        assertThat(savedMember.getAddress()).isEqualTo(member.getAddress());
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getRole()).isEqualTo(member.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        //when, then
        assertThatThrownBy(() -> memberService.saveMember(member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 가입된 회원입니다.");

    }

    @Test
    @DisplayName("중복 회원 가입 테스트2")
    public void saveDuplicateMemberTest2() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        //when
        Throwable thrown = catchThrowable(() -> memberService.saveMember(member2));

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 가입된 회원입니다.");

    }
}