package io.haechi.henesis.assignment;

import io.haechi.henesis.assignment.domain.ExchangeService;
import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.domain.arguments.FlushArguments;
import io.haechi.henesis.assignment.domain.util.Converter;
import io.haechi.henesis.assignment.infra.ExchangeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AssignmentApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(Converter.DoubleToHexString(0.15));
		System.out.println(Converter.hexStringToDouble("0x214e8348c4f0000"));


	}

}
