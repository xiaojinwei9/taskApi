package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="privateKey")
public class PrivateKey extends Model{

	public String sysId;
	public String privateKey;
	public PrivateKey(String sysId,String privateKey){
		this.sysId=sysId;
		this.privateKey=privateKey;
	}
}
