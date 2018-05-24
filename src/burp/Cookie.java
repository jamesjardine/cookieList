/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp;

/**
 *
 * @author jamesjardine
 */
public class Cookie {
    public String name = "";
    public String value = "";
    public String domain = "";
    public String path = "";
    public String expires = "";
    public boolean httpOnly = false;
    public boolean secure = false;
    public String sameSite = "";
    public String pageSource = "";
    
    public String toString()
    {
        return name + "," + value + "," + domain + "," + path + ",\"" + expires + "\"," + 
                String.valueOf(httpOnly) + "," + String.valueOf(secure) + "," + sameSite + "," + pageSource + "\n";
    }
    
    public Cookie(String[] parts, String page)
    {
        pageSource = page;
        String[] cookieName = parts[0].split("=");
        name = cookieName[0];
        
        // Check to see if the cookie actually has a value.  If not, leave the default.
        if (cookieName.length > 1)
        {
            value = cookieName[1];
        }
        
        for (int x = 1; x < parts.length; x++)
        {
            String part = parts[x].toLowerCase();
            switch (part.trim())
            {
                case "httponly":
                    httpOnly = true;
                    break;
                case "secure":
                    secure = true;
                    break;
                default:
                  String[] vars = part.split("=");
                  System.out.println(vars[0]);
                  if (vars.length > 0)
                  {
                      switch (vars[0].trim())
                      {
                          case "domain":
                              domain = vars[1];
                              break;
                          case "path":
                              path = vars[1];
                              break;
                          case "expires":
                              expires = vars[1];
                              break;
                          case "samesite":
                              sameSite = vars[1];
                              break;
                      }
                  }
            }
        }
    }
}
