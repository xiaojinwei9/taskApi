package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="taskLog")//任务日志表
public class TaskLog extends Model{
	public Integer taskId;//所属任务id
	public String cont;//内容 如:客户反馈了意见，准备修改
	public String files;
	//文件如:log初版.jpg-http://www.xxx.xxx.com/xxx/xxx.jpg|会议记录.doc-http://www.xxx.com/xxx/xx.doc
	public Date time;//添加时间
	public Integer userId;//用户ID号
	public Integer available;//1:正常 2:已删除
	
	public TaskLog(Integer taskId,String cont,String files,Date time,Integer userId,Integer available){
		this.taskId=taskId;
		this.cont=cont;
		this.files=files;
		this.time=time;
		this.userId=userId;
		this.available=available;
	}
}
