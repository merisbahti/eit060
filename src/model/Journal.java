package model;
import java.io.Serializable;
public class Journal implements Serializable{

	private int ID;
	private String doctor;
	private String nurse;
	private String patient;
	private String content;
	private String district;
	


public Journal(String doctor, String nurse, String patient, String district, String content){
		this.doctor = doctor;
		this.nurse = nurse;
		this.patient = patient;
		this.content = content;
		this.district = district;
	}
  public String getContent() {
    return content;
  }
  
  public String getDoctor(){
	  return doctor;
  }
  
  public String getNurse(){
	  return nurse;
  }

  public String getPatient(){
	  return patient;
  }
	public String getDistrict() {
		return district;	
	}
	
	public String toString() {
		return "Doc: " + doctor + "; Nurse: " + nurse + "; Patient: " + patient + "\n" + content;	
	
	}
	public void setID(int ID){
		this.ID = ID;
	}
}
