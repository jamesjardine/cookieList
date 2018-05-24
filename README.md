# Cookie List
Simple Burp plugin to help extract all cookies set for URLs in the current scope.

This project is just a sample to show how a simple burp extension can be created to help identify specific information. In this case, the plug-in creates a context menu to export cookies. These cookies are written to a csv file in your Documents folder.

This tool is very basic and I am sure there are better ways to extract this information. 

## Use
Build the Jar file and then in Burp open up the Extender tab. Next, select the Add button and select your new JAR file. Open the Target Tab and right click on a request for your site. Make sure the site is in scope. Select Export cookies menu item and it should create the CSV file of your cookies. By default, they are stored in the Documents folder. This can be changed in the code.

