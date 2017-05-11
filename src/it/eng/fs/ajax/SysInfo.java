package it.eng.fs.ajax;

import java.util.List;

import it.eng.fs.model.Model;

public class SysInfo extends Model {
	public int retCode = 0;
	public List<String> error = null;
	public List<String> warning = null;
}