package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="apiLog")
public class ApiLog extends Model{

	public String sysId;
	public Integer status;
	public Date time;
	public String token;
	@Lob
	public String content;
	
	public ApiLog(String sysId,Integer status,Date time,String token,String content){
		this.sysId=sysId;
		this.status=status;
		this.time=time;
		this.token=token;
		this.content=content;
	}
}
