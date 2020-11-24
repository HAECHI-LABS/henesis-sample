package io.haechi.henesis.assignment;

import io.haechi.henesis.assignment.domain.util.Converter;
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
