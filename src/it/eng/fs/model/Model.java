package it.eng.fs.model;

public class Model  {
	
	public Model(){}  // il costruttore senza parametri è obbligatorio

	public String toString() {
		return ModelHelper.json(this);
	}	
}
