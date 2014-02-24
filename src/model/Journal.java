package model;


public class Journal {

	private int ID;
	private String doctor;
	private String nurse;
	private String patient;
	private String content;
	private String district;
	
	class Journal(String doctor, String nurse, String patient, String content, String district){
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

  public String getDistrict(){
	  return district;
  }
}
