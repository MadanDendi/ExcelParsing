name := "excel-parser"

version := "0.1"

scalaVersion := "2.11.12"

val sparkVersion="2.3.1"

libraryDependencies+="org.apache.poi" % "poi-ooxml" % "3.9"
libraryDependencies+="org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies+="org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies+="com.crealytics" %% "spark-excel" % "0.9.17"
libraryDependencies+="com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.11"
