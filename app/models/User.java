package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="user")//用户表
public class User extends Model{
	public String mobile;//手机号,登录：手机号+密码
	public String password;
	public String name;
	public String email;
	public String phone;
	public String image;//全路径http://www.xxx.com/xxx/xxx.jpg
	public Integer type;//1:管理员,2:内部员工(可属多个任务组),3:一般用户(只属一个任务组)
	public String taskGroupIds;//所属任务组ID:1,2,3(一个成员可属于多个项目组)
	@Lob
	public String des;//备注
	public Integer logins;//登录次数
	public Date lastTime;//最后登录时间
	public Integer isComment;//0不可评价,1:可评价
	public Integer available;//1:正常 2:已删除
	
	public User(String mobile,String password,String name,String email,String phone,String image,Integer type,String taskGroupIds,String des,Integer logins,Date lastTime,Integer isComment,Integer available){
		this.mobile=mobile;
		this.password=password;
		this.name=name;
		this.email=email;
		this.phone=phone;
		this.image=image;
		this.type=type;
		this.taskGroupIds=taskGroupIds;
		this.des=des;
		this.available=available;
		this.logins=logins;
		this.lastTime=lastTime;
		this.isComment=isComment;
		}

}
