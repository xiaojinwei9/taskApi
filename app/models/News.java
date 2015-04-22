package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="news")//任务表
public class News extends Model{
	public String title;//标题
	public Date createTime;//创健时间
	public Date modifyTime;//最后修改时间
	public Integer newsGroupId;//所属组
	@Lob
	public String con;//备注
	public Integer userId;//创建者
	public Integer available;//1:正常 2:已删除
	public Integer status;//1未读;2已读
	public News(String title,Date createTime,Date modifyTime,Integer newsGroupId,String con,Integer userId,Integer available){
		this.title=title;
		this.createTime=createTime;
		this.modifyTime=modifyTime;
		this.newsGroupId=newsGroupId;
		this.con=con;
		this.userId=userId;
		this.available=available;
		this.status=1;
	}
}
