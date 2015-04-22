package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="newsToUser")//任务表
public class NewsToUser extends Model{
public Integer newsGroupId;
public Integer newsId;
public Integer userId;


public NewsToUser(Integer newsGroupId,Integer newsId,Integer userId){
	this.newsGroupId=newsGroupId;
	this.newsId=newsId;
	this.userId=userId;
}
}
