package com.lector.lectorexcel;

public class DatosCliente {

	private String fecha;
	private String tratamiento;
	private String nombre;
	private String telefono;
	private String origen;
	private String observacion;
	private String mail;
	
	public DatosCliente() {
		super();
		this.fecha = "";
		this.tratamiento = "";
		this.nombre = "";
		this.telefono = "";
		this.origen = "";
		this.observacion = "";
		this.mail = "";
	}

	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public String getTratamiento() {
		return tratamiento;
	}
	
	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getOrigen() {
		return origen;
	}
	
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	public String getObservacion() {
		return observacion;
	}
	
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
}
