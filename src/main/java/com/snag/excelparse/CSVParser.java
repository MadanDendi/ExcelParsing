package com.snag.excelparse;

/**
 * Job Name : CSVParser
 * Purpose  : Read the spread sheet and display data as per cell location based which is separated by ",".
 *
 * @author : Madan
 *
 */

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class CSVParser {

    public static void main(String args[]){
        CSVReader reader = null;
        String fileName="";
        Set<String> columns=new HashSet<>();
        Map<String,String> argsMap=new HashMap<String,String>();
        CSVParser cp=new CSVParser();

        try{
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
            reader = new CSVReader(new FileReader(fileName),'|');

            cp.parseCSV(reader, argsMap);
            reader.close();
        }
        catch(FileNotFoundException fe){
            fe.printStackTrace();

        }
        catch(Exception ie){
            ie.printStackTrace();

        }
    }

    void parseCSV(CSVReader reader,Map<String,String> cellLocations)
    {
        Map<Integer,String> headers=new HashMap<>();
        Map<Integer,HashMap> headerData=new HashMap<>();

        try {
            String[] nextLine;
            int recordCount = 0;
            while ((nextLine = reader.readNext()) != null) {
                if (recordCount == 0) {
                    int j = 1;
                    for (String token : nextLine) {
                        // System.out.print(token+"\t");
                        headers.put(j, token);
                        j++;
                    }
                    if(cellLocations.size() == 0)
                    {
                        cellLocations.clear();

                        for (Integer key : headers.keySet()) {
                            cellLocations.put(headers.get(key),"")  ;
                        }

                    }
                } else {
                    int header = 1;
                    HashMap<String, String> data = new HashMap<>();
                    for (String token : nextLine) {
                        // System.out.print(headers.get(header)+":"+token+"\t");
                        data.put(headers.get(header), token);
                        headerData.put(recordCount, data);
                        header++;
                    }

                }
                recordCount++;
            }

            System.out.println("Number of records in sheet: "+recordCount);

            HashMap<Integer,HashMap<String,String>> finalData=new HashMap<>();
            for (int recordNumber : headerData.keySet()) {
                HashMap<String, String> storedData = headerData.get(recordNumber);
                for(String requiredCol:cellLocations.keySet()){
                    if(storedData.containsKey(requiredCol)){
                        if(storedData.get(requiredCol).equals(cellLocations.get(requiredCol)))
                            finalData.put(recordNumber,headerData.get(recordNumber));
                        else if (cellLocations.get(requiredCol).equals(""))
                            finalData.put(recordNumber,headerData.get(recordNumber));
                    }

                }
            }
            for (int recordNumber : finalData.keySet())
            {
                System.out.println(finalData.get(recordNumber));
            }

        }
        catch (Exception e){
            e.printStackTrace();

        }


    }
}
