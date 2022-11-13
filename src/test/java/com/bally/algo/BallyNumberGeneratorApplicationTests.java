package com.bally.algo;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BallyNumberGeneratorApplicationTests {

	@Test
	void testGenerateAndSortRandomNumber() throws IOException {
		GenerateRandomNumbersAndSort.generateAndSaveRandomNumbers();
	}

}
