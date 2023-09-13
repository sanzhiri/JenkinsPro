package cn.newstrength.wcms.sample.site.obj;

import java.io.Serializable;
import java.util.Date;

public class SampleObj implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9171055789400843446L;
	private Long id;
	private String code;
	private Date createTime;

	public SampleObj() {

	}

	public SampleObj(Long id, String code) {
		this.id = id;
		this.code = code;
		this.createTime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
