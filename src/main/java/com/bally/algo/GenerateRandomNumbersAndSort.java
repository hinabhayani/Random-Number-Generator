package com.bally.algo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenerateRandomNumbersAndSort {
	private static final String FILE_NAME_FOR_SORT_NUMBERS = "sorted";
	private static final String FILE_NAME_FOR_RANDOM_NUMBERS = "randomNumbers";
	private static final String FILE_NAME_FOR_TEMP_FILE = "tempFile";
	private static final int MAX_VALUE = 1000000;

	private static long getAvailableMemory() {
		long availableMemory = Runtime.getRuntime().freeMemory();
		return availableMemory;
	}

	private static List<File> createTempFile(File file) throws IOException {
		List<File> listOfFiles = new ArrayList<File>();
		BufferedReader fbr = new BufferedReader(new FileReader(file));
		try {
			List<Integer> listOfTempData = new ArrayList<Integer>();
			long blocksize = getAvailableMemory();
			String line = "";
			int i = 0;
			while (line != null) {
				long currentBlock = 0;
				while ((currentBlock < blocksize) && ((line = fbr.readLine()) != null)) {
					listOfTempData.add(Integer.parseInt(line));
					currentBlock += line.length();
				}
				File tempFile = saveListOfIntegerToFile(listOfTempData, FILE_NAME_FOR_TEMP_FILE + i);
				listOfFiles.add(tempFile);
				listOfTempData.clear();
				i++;
			}
		} finally {
			fbr.close();
		}
		return listOfFiles;
	}

	private static void mergeFile(List<File> listOfFiles, String fileName) throws IOException {
		List<BufferedReader> listOfBufferReader = new ArrayList<BufferedReader>();
		BufferedWriter fbw = new BufferedWriter(new FileWriter(new File(fileName)));
		try {

			for (File file : listOfFiles) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				listOfBufferReader.add(br);
				file.deleteOnExit();
			}
			int[] topNumber = new int[listOfFiles.size()];
			for (int i = 0; i < listOfFiles.size(); i++) {
				String line = "";
				if ((line = listOfBufferReader.get(i).readLine()) != null) {
					topNumber[i] = Integer.parseInt(line);
				}
			}

			for (int j = 0; j < MAX_VALUE; j++) {
				int minNumber = Integer.MAX_VALUE;
				int readerIndex = -1;
				for (int i = 0; i < listOfFiles.size(); i++) {
					if (topNumber[i] < minNumber) {
						minNumber = topNumber[i];
						readerIndex = i;
					}
				}
				fbw.write(minNumber + "\n");
				String nextNumber = listOfBufferReader.get(readerIndex).readLine();
				if (nextNumber != null) {
					topNumber[readerIndex] = Integer.parseInt(nextNumber);
				} else {
					topNumber[readerIndex] = Integer.MAX_VALUE;
				}

			}

		} finally {
			fbw.close();
			for (BufferedReader br : listOfBufferReader) {
				br.close();
			}
		}

	}

	private static File saveListOfIntegerToFile(List<Integer> listOfInteger, String fileName) throws IOException {
		Collections.sort(listOfInteger);
		File file = new File(fileName);
		BufferedWriter fw = new BufferedWriter(new FileWriter(file));
		for (Integer value : listOfInteger) {
			fw.write(value + "\n");
		}
		fw.close();
		return file;

	}

	private static void sortRandomNumbers() throws IOException {
		File file = new File(FILE_NAME_FOR_RANDOM_NUMBERS);
		if (file.length() < getAvailableMemory()) {
			List<Integer> listOfRandomData = new ArrayList<Integer>();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				listOfRandomData.add(Integer.parseInt(line));
			}
			br.close();
			saveListOfIntegerToFile(listOfRandomData, FILE_NAME_FOR_SORT_NUMBERS);
		} else {
			List<File> listOfFiles = createTempFile(file);
			mergeFile(listOfFiles, FILE_NAME_FOR_SORT_NUMBERS);
		}
	}

	private static void generateRandomNumbers() throws IOException {
		File file = new File(FILE_NAME_FOR_RANDOM_NUMBERS);
		BufferedWriter fw = new BufferedWriter(new FileWriter(file));
		Random random = new Random();
		for (int i = 1; i <= MAX_VALUE; i++) {
			int randomNumber = random.nextInt(MAX_VALUE);
			fw.write(randomNumber + "\n");
		}
		fw.close();
	}

	public static void generateAndSaveRandomNumbers() throws IOException {
		LocalDateTime startTime = LocalDateTime.now();
		System.out.println("Start Time:: " + startTime);
		generateRandomNumbers();
		sortRandomNumbers();
		LocalDateTime endTime = LocalDateTime.now();
		System.out.println("End Time:: " + endTime);

		Duration duiration = Duration.between(startTime, endTime);
		System.out.println("Time:: " + duiration.getSeconds() + " Seconds");
	}

}
