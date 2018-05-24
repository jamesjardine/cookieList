/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp;

import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 * @author jamesjardine
 */
public class BurpExtender implements IBurpExtender, IContextMenuFactory
{
    IBurpExtenderCallbacks callbacks = null;
    
    
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        this.callbacks = callbacks;
        this.callbacks.registerContextMenuFactory(this);
        this.callbacks.setExtensionName("Cookie Parser");
    }
    
    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation iContextMenuInvocation)
    {
        List<JMenuItem> menus = new ArrayList<>();
        
        IHttpRequestResponse[] messages = iContextMenuInvocation.getSelectedMessages();
        JMenuItem menu = new JMenuItem("Export Cookies");
        MenuItemListener listener = new MenuItemListener();
        menu.addActionListener(listener);
        menus.add(menu);
        
        return menus;
    }
    
    class MenuItemListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e){
            GetCookies();
        }
    }
    
    // Separated this out in case we wanted to use a Directory Chooser.
    // Currently, requires a Documents folder in the users home folder.
    String GetSavePath()
    {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        String filePath = Paths.get(currentPath.toString(), "Documents", "cookieReport.csv").toString();

        System.out.println("The Path: " + filePath);
        return filePath;
    }
    
    // Write out the cookies to a CSV file.
    // Make sure to set the csvFile to a path on your system.
    public void WriteCSV(List<Cookie> cookies) throws IOException
    {
        String csvFile = GetSavePath();
        //Write out the cookies to a CSV File
        System.out.println("Writing CSV");
        FileWriter writer = new FileWriter(csvFile);
        String header = "Name,Value,Domain,Path,Expires,HTTPOnly,Secure,SameSite,URL\n";
        writer.append(header);
        for (Cookie cookie : cookies)
        {
            writer.append(cookie.toString());
        }
        writer.flush();
        writer.close();
        System.out.println("Done");
    }
    
    // Loop through the responses in the current scope to retrieve all the cookies.
    public void GetCookies()
    {
        System.out.println("Starting the analysis");
        IExtensionHelpers helpers = callbacks.getHelpers();
        IHttpRequestResponse[] messages = callbacks.getProxyHistory();
        List<Cookie> cookies = new ArrayList<Cookie>();
        if (messages.length > 0)
        {

            for (int i = 0; i < messages.length; i++)
            {
                messages[i].getHttpService();
                IRequestInfo requestInfo = helpers.analyzeRequest(messages[i]);
                if(callbacks.isInScope(requestInfo.getUrl()))
                {
                    byte[] responseBytes = messages[i].getResponse();
                    if (responseBytes != null){
                        IResponseInfo response = helpers.analyzeResponse(responseBytes);
                        List<String> headers = response.getHeaders();
                        for (String header : headers)
                        {
                            //System.out.println(header);
                            if(header.toLowerCase().startsWith("set-cookie:"))
                            {
                                String coo = "Set-Cookie:";
                                String[] parts = header.substring(coo.length()).split(";");
                                if (parts.length > 0)
                                {
                                    Cookie cookie = new Cookie(parts, requestInfo.getUrl().toString());
                                    cookies.add(cookie);
                                    System.out.println("Cookie Added" + cookie.name);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(cookies.size() > 0)
        {
            //Write out the cookies to a CSV File
            try
            {
                WriteCSV(cookies);
            }
            catch (IOException e)
            {
                System.out.println(e.toString());
            }
        }
    }
    

}

