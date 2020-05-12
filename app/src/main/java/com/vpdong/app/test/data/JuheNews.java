package com.vpdong.app.test.data;

import java.util.List;

public class JuheNews {
	private String stat;
	private List<Data> data;
	
	public String getStat() {
		return stat;
	}
	
	public void setStat(String stat) {
		this.stat = stat;
	}
	
	public List<Data> getData() {
		return data;
	}
	
	public void setData(List<Data> data) {
		this.data = data;
	}
	
	public static class Data {
		private String url;
		private String date;
		private String title;
		private String category;
		private String author_name;
		private String thumbnail_pic_s;
		private String thumbnail_pic_s02;
		private String thumbnail_pic_s03;
		
		public String getUrl() {
			return url;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getDate() {
			return date;
		}
		
		public void setDate(String date) {
			this.date = date;
		}
		
		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getCategory() {
			return category;
		}
		
		public void setCategory(String category) {
			this.category = category;
		}
		
		public String getAuthor_name() {
			return author_name;
		}
		
		public void setAuthor_name(String author_name) {
			this.author_name = author_name;
		}
		
		public String getThumbnail_pic_s() {
			return thumbnail_pic_s;
		}
		
		public void setThumbnail_pic_s(String thumbnail_pic_s) {
			this.thumbnail_pic_s = thumbnail_pic_s;
		}
		
		public String getThumbnail_pic_s02() {
			return thumbnail_pic_s02;
		}
		
		public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
			this.thumbnail_pic_s02 = thumbnail_pic_s02;
		}
		
		public String getThumbnail_pic_s03() {
			return thumbnail_pic_s03;
		}
		
		public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
			this.thumbnail_pic_s03 = thumbnail_pic_s03;
		}
	}
}
