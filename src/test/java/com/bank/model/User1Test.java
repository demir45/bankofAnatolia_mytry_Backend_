package com.bank.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class User1Test {
	
	@ParameterizedTest
    @DisplayName("ssn number control")
	@CsvSource({"true, 222 34 5423", "true, 999-22-8723", "true, 434-32 8734",
				"true, 567 76-8953", "true, 356758253", "false, 543/78 3542", "false, 543 78/3542",
				"false, 543_78 3542","false, 213.34.4567", "false, 723.65 7253",
				"true, 39856 8876", "true, 376 579378", "false, 3287356274",
				"false, 367 3 4 6325", "false, 090 39 7123"} )
    void getSsnTest( boolean result, String ssnNum) {

        Pattern pattern = Pattern.compile("[1-9]\\d{2}[- ]?\\d{2}[- ]?\\d{4}$");

        Matcher matcher = pattern.matcher( ssnNum);

        assertEquals(result, matcher.matches());
     //  assertEquals(result, pattern.matcher( ssnNum).matches());

    }
	
	@ParameterizedTest
	@DisplayName("Date of birth control")
	@CsvSource({"true, 1900-12-31", "true, 1999-11-30", "true, 1900.02.29", "true, 1956/04/30", "true, 1972-05-31"
		, "true, 2000.01-31", "true, 2020/03-17", "true, 2021-08-08", "false, 2022-12-31", "false, 1900-13-24"
		, "false, 1880-10-15", "false, 1900-04-31", "false, 1900-02-30", "false, 1900-06-31", "false, 1900*12-31"
		, "false, 19000-12-31", "false, 1900-12-33", "false, 1900 12 31", "false, 1900 12-31", "false, 1980.3.24"})
	void dateOfBirthTest(boolean result, String dob) {
				
		Pattern pattern = Pattern.compile("(19\\d{2}|20[01][0-9]|2020|2021)[-/.]" +
                "(((0[13578]|10|12)[-/.](0[1-9]|[12][0-9]|3[01]))|" + // 31 çeken aylar
                "((02[-/.])(0[1-9]|[12][0-9]))|" +  // şubat ayı
                "((0[469]|11)[-/.](0[1-9]|[12][0-9]|30)))"); // 30 çeken aylar

		
		Matcher matcher = pattern.matcher(dob);
		
		assertEquals(result, matcher.matches());
		
		
	}
	
}


