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
import java.util.Scanner;

public class GenerateRandomNumbersAndSort {
	static String fileNameForSortedNum = "sorted";
	static String fileNameForRandomNum = "randomNumbers";
	static String fileNameFortempData = "tempFile";

	public static long getAvailableMemory() {
		long availableMemory = Runtime.getRuntime().freeMemory();
		return availableMemory;
	}

	public static List<File> createTempFile(File file) throws IOException {
		List<File> listOfFiles = new ArrayList<File>();
		BufferedReader fbr = new BufferedReader(new FileReader(file));
		try {
			List<Integer> listOfTempData = new ArrayList<Integer>();
			long blocksize = getAvailableMemory();
			String line = "";
			int i = 0;
			try {
				while (line != null) {
					long currentBlock = 0;
					while ((currentBlock < blocksize) && ((line = fbr.readLine()) != null)) {
						listOfTempData.add(Integer.parseInt(line));
						currentBlock += line.length();
					}
					File tempFile = saveListOfIntegerToFile(listOfTempData, fileNameFortempData + i);
					listOfFiles.add(tempFile);
					listOfTempData.clear();
					i++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			fbr.close();
		}
		return listOfFiles;
	}

	public static void mergeFile(List<File> listOfFiles, String fileName) throws IOException {
		System.out.println("merge file::" + fileName);
		List<BufferedReader> listOfBufferReader = new ArrayList<BufferedReader>();
		BufferedWriter fbw = new BufferedWriter(new FileWriter(new File(fileName)));
		try {

			for (File file : listOfFiles) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				listOfBufferReader.add(br);
				file.deleteOnExit();
			}

			for (BufferedReader fileReader : listOfBufferReader) {
				String line = "";
				while ((line = fileReader.readLine()) != null) {
					fbw.write(line + "\n");
				}
			}

		} finally {
			fbw.close();
			for (BufferedReader br : listOfBufferReader) {
				br.close();
			}
		}

	}

	public static File saveListOfIntegerToFile(List<Integer> listOfInteger, String fileName) throws IOException {
		Collections.sort(listOfInteger);
		File file = new File(fileName);
		BufferedWriter fw = new BufferedWriter(new FileWriter(file));
		listOfInteger.forEach(e -> {
			try {
				fw.write(e + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		fw.close();

		return file;

	}

	static String generateInput(long maxNumber) throws IOException {
		long max = maxNumber;
		int min = 1;
		File file = new File(fileNameForRandomNum);
		BufferedWriter fw = new BufferedWriter(new FileWriter(file));
		for (int i = 1; i <= max; i++) {
			int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
			try {
				fw.write(random_int + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		fw.close();
		if (file.length() < getAvailableMemory()) {
			List<Integer> listOfRandomData = new ArrayList<Integer>();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				listOfRandomData.add(Integer.parseInt(line));
			}
			saveListOfIntegerToFile(listOfRandomData, fileNameForSortedNum);
		} else {
			List<File> listOfFiles = createTempFile(file);
			mergeFile(listOfFiles, fileNameForSortedNum);
		}

		return fileNameForRandomNum;
	}

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Number:: ");
		int input = sc.nextInt();

		LocalDateTime startTime = LocalDateTime.now();
		System.out.println("Start Time:: " + startTime);

		String fileName = generateInput(input);

		LocalDateTime endTime = LocalDateTime.now();
		System.out.println("End Time:: " + endTime);

		Duration duiration = Duration.between(startTime, endTime);
		System.out.println("Time:: " + duiration.getSeconds() + " Seconds");
	}

}
