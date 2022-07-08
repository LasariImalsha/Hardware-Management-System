package util;

import database.DatabaseConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;

public class JasperUtil {
    /*{
        try{
            JasperDesign design=JRXmlLoader.load(getClass().getResourceAsStream("path.jrxml"));
            JasperReport compileReport=JasperCompileManager.compileReport(design);
            JasperPrint fillReport=JasperFillManager.fillReport(compileReport, null, new JREmptyDataSource(1));
            JasperViewer.viewReport(fillReport,false);
        }catch(JRException e){
            e.printStackTrace();
        }
    }*/
    public void loadReport(String report){
        try {
            JasperDesign design = JRXmlLoader.load(getClass().getResourceAsStream("/reports/"+report+".jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DatabaseConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
