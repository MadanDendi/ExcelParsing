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
            reader = new CSVReader(new FileReader(fileName),',');

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
                    /**
                     * 1. Take headers from CSV file
                     * 2. If filter columns  are not passed as arguments, display whole data.
                     */
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

                    /**
                     * 1. Mapping data to record and header based.
                     *
                     */
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

            /**
             * 1. Display the data based on User filter.
             * 2. If User not passing any filter, get whole data from file.
             *
             */
            for (int recordNumber : headerData.keySet()) {
                HashMap<String, String> storedData = headerData.get(recordNumber);
                for(String requiredCol:cellLocations.keySet()){
                    if(storedData.containsKey(requiredCol)){
                        if(storedData.get(requiredCol).equals(cellLocations.get(requiredCol)))
                            System.out.print(storedData.get(requiredCol) + "\t");
                        else if (cellLocations.get(requiredCol).equals(""))
                            System.out.print(storedData.get(requiredCol) + "\t");
                    }

                }

                System.out.println();
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }



    }
}
