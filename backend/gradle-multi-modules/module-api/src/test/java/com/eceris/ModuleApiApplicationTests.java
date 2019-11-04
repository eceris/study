package com.eceris;

import com.eceris.domain.Member;
import com.eceris.service.MemberServiceCustom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModuleApiApplicationTests {

	@Autowired
	private MemberServiceCustom memberServiceCustom;

	@Test
	public void save() {
		Member member = new Member(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		Long id = memberServiceCustom.signup(member);
		assertThat(id, is(1L));
	}
}
