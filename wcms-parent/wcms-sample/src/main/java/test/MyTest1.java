package test;

import java.io.*;

public class MyTest1 {
	public static void main(String[] args) throws IOException {
		File file = new File("F:/test.txt");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3000000; i++) {
			sb.append("abcdefghigklmnopqrstuvwsyz");
		}
		byte[] bytes = sb.toString().getBytes();

		long start = System.currentTimeMillis();
		write(file, bytes);
		long end = System.currentTimeMillis();

		long start2 = System.currentTimeMillis();
		bufferedWrite(file, bytes);
		long end2 = System.currentTimeMillis();

		System.out.println("普通字节流耗时：" + (end - start) + " ms");
		System.out.println("缓冲字节流耗时：" + (end2 - start2) + " ms");

	}

	// 普通字节流
	public static void write(File file, byte[] bytes) throws IOException {
		OutputStream os = new FileOutputStream(file);
		os.write(bytes);
		os.close();
	}

	// 缓冲字节流
	public static void bufferedWrite(File file, byte[] bytes) throws IOException {
		BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(file));
		bo.write(bytes);
		bo.close();
	}
}
