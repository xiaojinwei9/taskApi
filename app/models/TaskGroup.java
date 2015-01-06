package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="taskGroup")//任务组表
public class TaskGroup extends Model{
 public String name;//如 ：天河区项目组/xxx公司/
 public Date time;//添加时间
 public Integer userId;
 public Integer available;//1:正常 2:已删除
 public TaskGroup(String name,Date time,Integer userId,Integer available){
	 this.name=name;//白云区项目组,天河区项目组,xxx公司,xxx事业部
	 this.time=time;
	 this.userId=userId;
	 this.available=available;
 }
}
