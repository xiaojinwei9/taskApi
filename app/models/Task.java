package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="task")//任务表
public class Task extends Model{

	public String name;//任务名称
	public String image;//全路径http://www.xxx.com/xxx/xxx.jpg
	public Date createTime;//创健时间
	public Date modifyTime;//最后修改时间
	public Integer status;//状态 1:待初稿,2:待审核,3:待确稿,4:待评价,5:已完成
	public Integer taskGroupId;//所属任务组
	public Integer available;//1:正常 2:已删除
	public Task(String name,String image,Date createTime,Date modifyTime,Integer status,Integer taskGroupId,Integer available){
		this.name=name;
		this.image=image;
		this.createTime=createTime;
		this.modifyTime=modifyTime;
		this.status=status;
		this.taskGroupId=taskGroupId;
		this.available=available;
	}
}
