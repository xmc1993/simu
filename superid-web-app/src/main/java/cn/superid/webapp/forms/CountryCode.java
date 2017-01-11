package cn.superid.webapp.forms;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by njuTms on 16/10/19.
 */
public class CountryCode {
    private static List<CountryCode> countryCodes = null ;
    private static String fileName = "resources/countryCode.xlsx";


    private String name;
    private String englishName;
    private String shortName;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static List<CountryCode> getCountryCodes(){
        if((countryCodes != null))
            return countryCodes;
        countryCodes = new ArrayList<>();
        try {
            InputStream input = CountryCode.class.getClassLoader().getResourceAsStream("countryCode.xlsx");  //建立输入流
            Workbook wb = new XSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            CountryCode countryCode = new CountryCode();
            while (rows.hasNext()) {
                Row row = rows.next();  //获得行数据
                if(row.getRowNum() == 0)
                    continue;
                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cell.getColumnIndex()) {   //根据cell中的类型来输出数据
                        case 1:
                            countryCode.setName(cell.getStringCellValue());
                            break;
                        case 2:
                            countryCode.setEnglishName(cell.getStringCellValue());
                            break;
                        case 3:
                            countryCode.setShortName(cell.getStringCellValue());
                            break;
                        case 4:
                            countryCode.setCode(cell.getStringCellValue());
                            break;
                        default:
                            System.out.println("unsuported sell type");
                            break;
                    }
                }
                countryCodes.add(countryCode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return countryCodes;
    }

    public static String getAddress(String code){
        if((countryCodes == null)){
            getCountryCodes();
        }
        String result = "";
        for(CountryCode c : countryCodes){
            if(c.getCode().equals((code))){
                result = c.getName();
                break;
            }
        }
        return result;
    }
}
