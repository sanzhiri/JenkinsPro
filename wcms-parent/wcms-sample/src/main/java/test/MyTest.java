package test;

import java.io.*;

public class MyTest {
	public static void main(String[] args) throws IOException {
		File data = new File("C:/Mu/data.zip");
		File a = new File("C:/Mu/a.zip");
		File b = new File("C:/Mu/b.zip");
		StringBuilder sb = new StringBuilder();
		long start = System.currentTimeMillis();
		copy(data, a);
		long end = System.currentTimeMillis();

		long start2 = System.currentTimeMillis();
		bufferedCopy(data, b);
		long end2 = System.currentTimeMillis();

		System.out.println("普通字节流耗时：" + (end - start) + " ms");
		System.out.println("缓冲字节流耗时：" + (end2 - start2) + " ms");
	}

	// 普通字节流
	public static void copy(File in, File out) throws IOException {
		// 封装数据源
		InputStream is = new FileInputStream(in);
		// 封装目的地
		OutputStream os = new FileOutputStream(out);
		
		int by = 0;
		while ((by = is.read()) != -1) {
			os.write(by);
		}
		is.close();
		os.close();
	}

	// 缓冲字节流
	public static void bufferedCopy(File in, File out) throws IOException {
		// 封装数据源
		BufferedInputStream bi = new BufferedInputStream(new FileInputStream(in));
		// 封装目的地
		BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(out));
		
		int by = 0;
		while ((by = bi.read()) != -1) {
			bo.write(by);
		}
		bo.close();
		bi.close();
	}
}
