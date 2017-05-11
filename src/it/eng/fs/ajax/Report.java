package it.eng.fs.ajax;

import java.util.ArrayList;
import java.util.List;

import it.eng.fs.ajax.Session.Conf;
import it.eng.fs.model.Configuration;
import it.eng.fs.model.ConfigurationHelper;
import it.eng.fs.model.Model;

public class Report  extends AjaxHandler {
	public static class ReportRow extends Model {
		public String nome;
		public String descrizione;
		public double x;
		public double y;
	}
	
	
	public static class GetData extends AjaxFunction {
		
		public int n = 5;
		public List<ReportRow> data = new ArrayList<Report.ReportRow>();

		private static String words[] = "Lorem ipsum dolor sit amet consectetuer adipiscing elit Aenean commodo ligula eget dolor Aenean massa Cum sociis natoque penatibus et magnis dis parturient montes".toLowerCase().split(" ");
		private static String w() {
			return words[(int) (Math.random()*words.length)];
		}
		
		@Override
		public void run() throws Exception {
			for(int i=0; i<n; i++) {
				ReportRow row = new ReportRow();
				row.nome =	w()+" "+w();
				row.descrizione = w()+" "+w()+" "+w()+" "+w()+" "+w()+" "+w();
				row.x = Math.random()*1000; 
				row.y = Math.random()*1000; 
				data.add(row);
			}
		}
	}

	
}
