import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import cn.newstrength.wtdf.plugin.util.AESUtils;

public class LicenseCreator {
	// 勿动
	private static final String KEY = "nsms@wcms@dlwcms";
	public static void main(String[] args) {
		JSONObject params = new JSONObject();
		// 过期时间 yyyy-MM-dd HH:mm:ss
		params.put("expiredTime", "2023-02-08 21:30:00");
		// 过期后允许创建的信息数量
		params.put("number", 2);
		// 生成
		String license = AESUtils.encrypt(params.toJSONString(), KEY);
		System.out.println(StringUtils.leftPad("", 50, '*'));
		System.out.println("请将以下内容粘贴到 application-prod.properties 文件中");
		System.out.println(String.format("wcms.key = %s", license));
		System.out.println(StringUtils.leftPad("", 50, '*'));
	}
}
