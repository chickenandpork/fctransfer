/* smallfoot.org is owned by allan.clark */
package org.smallfoot.filexfer.sftp;

//import java.io.*;
//import java.util.*;
//import javax.activation.DataSource;
//import javax.activation.URLDataSource;
//import gnu.getopt.Getopt;
//import gnu.getopt.LongOpt;
import org.apache.commons.net.ssh.SSHClient;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @file
 */

public class SFTPURLConnection extends java.net.URLConnection
{
    SSHClient ssh = null;
    
    public SFTPURLConnection(java.net.URL url)
    {
        super(url);
System.out.println("public SFTPURLConnection(java.net.URL "+url.toString()+")");
    }


    /**
     * Stubbed, because there's no change we want to accept yet.  Sorry, apparently "at this time" is more professional, if you listen to the wordy airport announcements.
     *
     * @param name ignored
     * @param value ignored
     */
    public void setRequestHeader(String name, String value) { }

    /**
     * Stubbed, because there's no change we want to accept yet.  Sorry, apparently "at this time" is more professional, if you listen to the wordy airport announcements.
     *
     * @param ignored ignored
     */
    public void setContentType(String ignored) { }

    /**
     * Stubbed, because there's nothing to send us.  Oh how did I get caught in the "Royal 'We'" ?
     */
    public java.io.OutputStream getOutputStream()
    {
        return null;
    }

    /**
     * Stubbed, because there's nothing to send us.  Oh how did I get caught in the "Royal 'We'" ?
     */
    public java.io.InputStream getInputStream()
    {
        return null;
    }

    public void connect()
    {
//System.out.println("void connect(), connectionInfo=\""+connectionInfo+"\", user=\""+properties.get("user")+"\", password=\""+properties.get("password")+"\"");
System.out.println("void connect(), user/pass=\""+url.getUserInfo()+"\"");

            ssh = new SSHClient();
        try
        {
            ssh.loadKnownHosts();
	}
        catch (java.io.IOException ioe)
        {
System.out.println ("IOException: cannot load known hosts; continuing.");
        }

        try
        {
	    String[] s;

	    if (null == url.getUserInfo())
		s = System.getenv("user.name").split(":");
	    else 
	    {
		s = url.getUserInfo().split(":");
		s[0] = URLDecoder.decode(s[0]);

System.out.println ("connecting to "+url.getHost()+" port "+url.getPort());
         	if (0 < url.getPort())
		    ssh.connect(url.getHost(), url.getPort());
	    	else
		    ssh.connect(url.getHost());

		switch (java.lang.reflect.Array.getLength(s))
		{
		    case 1:
                	ssh.authPublickey(s[0]);
System.out.println ("connecting as "+s[0]);
			break;
		    default:
			s[1] = URLDecoder.decode(s[1]);
                	ssh.authPassword(s[0], s[1]);
System.out.println ("connecting as "+s[0]+" pass "+s[1]);
			break;
		}
	    }
        }
        catch (UnsupportedEncodingException uee)
        {
System.out.println ("UnsupportedEncodingException: cannot decode Authority.");
        }
        catch (java.io.IOException ioe)
        {
System.out.println ("IOException: cannot load known hosts; continuing.");
        }

    }
}
