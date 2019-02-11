package com.snag.excelparse

/**
  * Job Name : ReadExcel
  * Purpose  : Read the spread sheet and display data as per cell location based using Spark Dataframes.
  *
  * @author : Madan
  *
  */

import org.apache.spark.sql.{Column, SparkSession}
import org.apache.spark.sql.functions._

object ExcelParser {

  def main(args:Array[String]): Unit ={

    val spark=SparkSession
      .builder()
      .master("local[*]")
      .getOrCreate()

    var filename=""
    var strArg:Array[String]=Array()
    var locations:Map[String,String]=Map()
    var cond=""

    if(args.length == 0) {
      println("There is no file argument to read :")
      System.exit(0);
    }
    else if(args.length == 1) {
      filename = args(0)
    }
    else{
      filename = args(0)
      var i=1;
      strArg=args.slice(1,args.length)
      for (elem <- strArg) {
        val parseArg=elem.split("=")
        val key=parseArg(0);
        val value=parseArg(1)
        if(i==1)
          cond+=s"$key=\'$value\'"
        else
          cond+=s" AND $key=\'$value\'"
        println("Condition:"+cond)
        locations+=(parseArg(0)->parseArg(1))
        i+=1
      }

    }

    traverseExcel(spark,filename,locations,cond)

  }

  def traverseExcel(spark:SparkSession,fileName:String,locations:Map[String,String],cond:String): Unit ={

    val df=spark.read.format("com.crealytics.spark.excel")
      .option("location",fileName)
      .option("useHeader", "true")
      .option("treatEmptyValuesAsNulls", "true")
      .option("inferSchema", "true")
      .option("addColorColumns", "False")
      .load(fileName)

    println("columns"+locations.keys.toList)

    if(locations.keys.size == 0 )
      df.show()
    else {
      df.createTempView("spreadsheet")
      val df1=spark.sql(s"SELECT * FROM spreadsheet where $cond ")
      df1.select(locations.keys.toList.head, locations.keys.toList.tail: _*).show()

    }

  }

}
