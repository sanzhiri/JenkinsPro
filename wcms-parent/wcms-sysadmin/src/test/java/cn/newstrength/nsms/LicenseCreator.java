package cn.newstrength.nsms;

import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;

import cn.newstrength.wcms.core.definition.Key;
import cn.newstrength.wtdf.plugin.util.AESUtils;

public class LicenseCreator {
	public static void main(String[] args) {
		JSONObject params = new JSONObject();
		/*
		 * 过期时间 yyyy-MM-dd HH:mm:ss
		 */
		params.put("expiredTime", "2023-10-15 23:59:59");
		/*
		 * 过期后允许创建的信息数量 当 license 过期以后，允许用户继续创建的信息数量
		 */
		params.put("number", 2);
		/*
		 * license 检查失败后动作 ： 
		 * shutdown：关闭应用 
		 * warn：提示并且无法创建信息
		 */
		// params.put("action", "warn");
		params.put("action", "shutdown");
		// 生成
		String license = AESUtils.encrypt(params.toJSONString(), Key.WCMS_KEY);
		System.out.println(StringUtils.leftPad("", 50, '*'));
		System.out.println("请将以下内容粘贴到 application-prod.properties 文件中");
		System.out.println(String.format("wcms.key = %s", license));
		System.out.println(StringUtils.leftPad("", 50, '*'));
		//
		System.out.println(AESUtils.decrypt(license, Key.WCMS_KEY));
	}
}
