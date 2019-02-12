

package com.snag.excelparse;

/**
 * Job Name : ReadExcel
 * Purpose  : Read the spread sheet and display data as per cell location based.
 *
 * @author : Madan
 *
 */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class ReadExcel {

    public static void main(String args[]){
        ReadExcel rd=new ReadExcel();
        String fileName="";
        Set<String> columns=new HashSet<>();
        Map<String,String> argsMap=new HashMap<String,String>();
        try {

            if(args.length == 0)
            {
                System.out.println("File argument was not exist:");
                System.exit(1);
            }
            else if (args.length == 1)
            {
                fileName=args[0];
            }
            else {
                fileName=args[0];
                columns=new HashSet<>(Arrays.asList(args).subList(1, args.length));
                for(String arg:columns)
                {
                    String keyValues[]=arg.split("=");
                    argsMap.put(keyValues[0],keyValues[1]);
                }


            }
            FileInputStream file = new FileInputStream(new File(fileName));

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(0);

            int totalRows = sheet.getPhysicalNumberOfRows();
            System.out.println("Number of  successful rows in sheet:"+totalRows);
            rd.locationRead(sheet, argsMap);
            file.close();
        }

        catch (FileNotFoundException fe){
            fe.printStackTrace();

        }
        catch (Exception e){
            e.printStackTrace();

        }


    }

    void locationRead(XSSFSheet sheet,Map<String,String> cellLocations){
        Map<String, Integer> cellMap = new HashMap<>(); //Create map
        Map<Integer,String> requiredCells=new HashMap<>();

        /**
         * Get all spread sheet record into Row iterator
         */

        Iterator<Row> rowIterator = sheet.iterator();
        HashMap<Integer,Row> finalData=new HashMap<>();
        while (((Iterator) rowIterator).hasNext()) {
            Row row = rowIterator.next();
            int rowIndex = row.getRowNum();

            /**
             * 1. Get Size of the columns
             * 2. Map first row columns Names to Indexes.
             * 3. Select all column in spread sheet Default.
             * 3. Display records as location based.
             */
            if (rowIndex == 0) {
                short minColID = row.getFirstCellNum();
                short maxColID = row.getLastCellNum();
                for(short colID=minColID; colID<maxColID; colID++) {
                    Cell cell = row.getCell(colID);
                    //  System.out.println("Cell:"+cell.getStringCellValue()+" "+cell.getColumnIndex());
                    cellMap.put(cell.getStringCellValue(),cell.getColumnIndex()) ;//add the cell contents (name of column) and cell index to the map
                }

                if(cellLocations.size() == 0)
                {
                    cellLocations.clear();

                    for (String key : cellMap.keySet()) {
                        requiredCells.put(cellMap.get(key),"")  ;
                    }

                }
                else
                {
                    for (String key : cellLocations.keySet()){
                        for (String actualKey : cellMap.keySet())
                        {
                            if(key.equals(actualKey))
                                requiredCells.put(cellMap.get(actualKey),cellLocations.get(key));
                        }

                    }
                }


            }
            else{

                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int cellIndex=cell.getColumnIndex();
                    for(int i :requiredCells.keySet()){
                        if (cellIndex == i){
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    if(cell.getNumericCellValue() == Integer.parseInt(requiredCells.get(i)))
                                        finalData.put(rowIndex,row);
                                    else if(requiredCells.get(i).equals(""))
                                        finalData.put(rowIndex,row);
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    if(cell.getStringCellValue().trim().equals(requiredCells.get(i)))
                                        finalData.put(rowIndex, row);
                                    else if(requiredCells.get(i).equals(""))
                                        finalData.put(rowIndex,row);
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                    if(cell.getBooleanCellValue() == Boolean.parseBoolean(requiredCells.get(i)))
                                        finalData.put(rowIndex,row);
                                    break;
                                case Cell.CELL_TYPE_ERROR:
                                    finalData.put(rowIndex,row);
                                    break;

                            }
                        }
                    }

                }

            }

        }

        for(Integer rownum:finalData.keySet()){
            Row row=finalData.get(rownum);
            short minColID = row.getFirstCellNum();
            short maxColID = row.getLastCellNum();
            for(short colID=minColID; colID<maxColID; colID++) {
                System.out.print(row.getCell(colID)+"\t");
            }
            System.out.println();
        }


    }

}
