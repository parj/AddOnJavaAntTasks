![Build Status](https://travis-ci.org/parj/AddOnJavaAntTasks.png)
# The addon Ant Tasks
* AntDav - To upload and download from WebDav Servers
* AntCsvtoExcel - To convert a set of csv files into 1 Excel file. Each csv file is a sheet within
* AntXMLUnit - To compare two directory of xml files using XMLUnit and produce a csv file report for each
* AntXPath - For modifying xml files using xpaths, sometimes for comparison you want to physically strip out timestamp elements, etc. AntXPath is capable of doing that.

## AntDav
Ensure you have downloaded the *AntDav jar* and placed that in your $ANT_HOME/lib

Requires JackRabbit Stand Alone jar - http://jackrabbit.apache.org/downloads.html if you are doing development and would like to run the unit tests.

In your ant build.xml declare the custom tasks:

    <taskdef name="pull" classname="uk.co.firstzero.webdav.Pull" />
    <taskdef name="push" classname="uk.co.firstzero.webdav.Push" />

To use:

    <!-- Example of pushing file(s) to webdav -->
    <push url="http://localhost:9090/repository/default"
          user="admin"
          password="admin"
          overwrite="true">
          <fileset dir="." includes="README"/>
    </push>
                
    <!-- Example of pulling files from webdav -->
    <pull url="http://localhost:9090/repository/default"
          user="admin"
          password="admin"
          file="README"
          overwrite="true"/>
    </pull>

If you prefer to use gradle so that dependencies are setup - clone my repository and you can run `gradle push` and `gradle pull` directly. Otherwise - here is a trimmed script - [Gradle Snippet](https://gist.github.com/3306722)

    apply plugin: 'java'


    List compileAll = [ "org.apache.ant:ant:1.8.1", "junit:junit:4.0", "log4j:log4j:1.2.16", //All
				    "xmlunit:xmlunit:1.3", "net.sourceforge.jexcelapi:jxl:2.6.12", //uk.co.firstzero.xml.AntXMLUnit
				    "net.sf.opencsv:opencsv:2.0", //uk.co.firstzero.csv.diff
				    "net.sourceforge.jexcelapi:jxl:2.6.10", //uk.co.firstzero.csv.AntCsvToExcel
                    "commons-codec:commons-codec:1.6",
                    "commons-httpclient:commons-httpclient:3.0",
                    "commons-logging:commons-logging:1.1.1",
                    "org.apache.jackrabbit:jackrabbit-webdav:2.1.1",
                    "org.slf4j:slf4j-api:1.5.8",
                    "org.slf4j:slf4j-log4j12:1.5.2"  //uk.co.firstzero.AntDav
				   ]
     dependencies {
        compile compileAll
        runtime fileTree(dir: 'build/libs', include: '*.jar')
     }

     task push << {
        ant.taskdef(name: 'push', classname: 'uk.co.firstzero.webdav.Push', classpath: configurations.runtime.asPath)
        ant.push(user: 'admin', password: "admin", url: "http://localhost:8080/repository/default", overwrite: true) {
        fileset(dir: 'src/test/resources/webdav', includes: '*.csv')
        }
     }

     task pull << {
          ant.taskdef(name: 'pull', classname: 'uk.co.firstzero.webdav.Pull', classpath: configurations.runtime.asPath)
          ant.pull(user: 'admin', password: "admin", url: "http://localhost:8080/repository/default",
            file: "output.csv",
            outFile: "src/test/resources/webdav/output.csv",
            overwrite: true)
     }

## AntCsvToExcel

Ensure you have downloaded the *AntCsvToExcel jar* and placed that in your $ANT_HOME/lib

Requires JExcelApi - http://sourceforge.net/projects/jexcelapi/files/jexcelapi/2.6.12/jexcelapi_2_6_12.zip/download

    <target name="declare-tasks">		
        <!-- Dependency on JExcelApi - 
         http://sourceforge.net/projects/jexcelapi/files/jexcelapi/2.6.12/jexcelapi_2_6_12.zip/download
	-->
		<taskdef name="csvToexcel" classname="uk.co.firstzero.csv.AntCsvToExcel" />
    </target>

To use:

	<!-- Example of combining csv files to an excel file -->
	<target name="csvToexcel" depends="declare-tasks">
		<csvToexcel outputFile="report.xls" separator=",">
			<fileset dir="." includes="*.csv"/>
		</csvToexcel>
	</target>

## AntXMLUnit

Ensure you have downloaded the *AntCsvToExcel jar* and placed that in your $ANT_HOME/lib

Requires XMLUnit (xmlunit-bin) - http://sourceforge.net/projects/xmlunit/files/xmlunit%20for%20Java/XMLUnit%20for%20Java%201.3/

    <target name="declare-tasks">
        <taskdef name="diffxml" classname="uk.co.firstzero.xml.AntXMLUnit"/>
    </target>

To use: 

    <target name="diff" depends="jar, declare-tasks">
        <diffxml testDirectory="2" resultDirectory="." verbose="True">
            <fileset dir="1" includes="*.xml"/>
        </diffxml>
    </target>

## AntXPath

Ensure you have downloaded the *AntXPath jar* and placed that in your $ANT_HOME/lib

Requires XALAN (for JAVA 1.4 and below, JAVA 1.5 and above nothing is required) - http://xml.apache.org/xalan-j/downloads.html#latest-release

    <target name="declare-tasks">
        <taskdef name="xpath" classname="uk.co.firstzero.xml.AntXPath"/>
        <taskdef name="modifyPath" classname="uk.co.firstzero.xml.ModifyPath"/>
    </target>

To use: 
Rename Pattern - Is the pattern in which the files you should be renamed - The value is picked up from the xml using xpaths. This makes sense, when you remove/change values, rename the files and then do a comparison


    <target name="modify" depends="jar, declare-tasks">
       <xpath outputDirectory="out" 
	      renamePattern="//publish_date[position() = 1]#_#//price[position() = 1]" 
	      verbose="True">
            <fileset dir="." includes="input.xml"/>
	        <modifyPath path="//title" delete="True"/>
	        <modifyPath path="//author" value="ToTo"/>
       </xpath>
    </target>
