# Cookie List
Simple Burp plugin to help extract all cookies set for URLs in the current scope.

This project is just a sample to show how a simple burp extension can be created to help identify specific information. In this case, the plug-in creates a context menu to export cookies. These cookies are written to a csv file that you choose.

This tool is very basic and I am sure there are better ways to extract this information. 

## Disclaimer
This is an example project only and is not meant for production use. USE AT YOUR OWN RISK!!.  

## Use
Build the Jar file and then in Burp open up the Extender tab. Next, select the Add button and select your new JAR file. Open the Target Tab and right click on a request for your site. Make sure the site is in scope. Select Export cookies menu item and it should create the CSV file of your cookies. The file location is chosen when you run the extension through a file picker. 

## Troubleshooting
The app writes to the default console output. You will see this if you start burp from the command line. 

If you run it, but the file chooser doesn't appear, make sure that you have added the target to the scope. This will only work with items in scope.

