package cn.newstrength.wcms.sysadmin.fs.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FilePart implements Serializable {
	private static final long serialVersionUID = -584156350996232612L;
	/**
	 * 文件id
	 */
	private String id;
	/**
	 * 文件名
	 */
	private String filename;
	/**
	 * 文件扩展名
	 */
	private String extension;
	/**
	 * 完整路径
	 */
	private String path;
	/**
	 * 原始文件大小
	 */
	@JsonIgnore
	private Long originalSize = 0l;
	/**
	 * 显示文件大小
	 */
	private String size = "0";
	/**
	 * 是否目录
	 */
	private boolean directory = false;
	/**
	 * 是否隐藏
	 */
	private boolean hidden = false;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 拥有人
	 */
	private String owner;
	/**
	 * 文件最后修改时间
	 */
	private Date lastModifiedTime;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getOriginalSize() {
		return originalSize;
	}

	public void setOriginalSize(Long originalSize) {
		this.originalSize = originalSize;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "FilePart [id=" + id + ", filename=" + filename + ", extension=" + extension + ", path=" + path
				+ ", originalSize=" + originalSize + ", size=" + size + ", directory=" + directory + ", hidden="
				+ hidden + ", status=" + status + ", remark=" + remark + ", owner=" + owner + ", lastModifiedTime="
				+ lastModifiedTime + "]";
	}
}
