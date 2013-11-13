package org.smallfoot.filexfer;

import it.sauronsoftware.ftp4j.*;
import java.io.*;

public class FTP4J extends FileTransferWinch
{
    FTPClient proxy = null;
    java.util.Vector<String> uploadNotify = null;

    public static boolean handles (java.net.URL u)
    {
        return (u.getProtocol().equalsIgnoreCase("ftp"));
    };

    public FTP4J(java.net.URL u)
    {
        url = u;
        proxy = new FTPClient();
    }

    public String name()
    {
        return super.name() + " ("+proxy.getClass().getName().replaceAll("^.*\\.","") + ")";
    }


    /** list one or more example URLs showing the available upload/download protocols
     *
     * @param url a sample URL showing user/pass/pathname
     * @returns array of examples using that URL
     */
    public static String[] examples(java.net.URL url)
    {
	return new String[]
	{
	    url.toString().replaceFirst("^[a-zA-Z0-9]+:","ftp:")
	};
    }


    /**
     * Attempt an upload to the remote server.  Initially very basic, this can be extended for all the intelligence we need to get data through.
     *
     * This function, given a filename, connects to an FTP server and attempts to store the file.
     * Initially the username and password are defaulted to those usable to upload from PAK_RED, but
     * later (when i can look at URL factories) this can be extended.  The capability will be
     * preserved to give the function a list of statuses and a position so that a later threaded
     * design can try a number of uploads in parallel, ditching all but the most efficient: on
     * connection, so status-indiciates; on successful 1-k upload with a temp filename,
     * status-indicates finished 1k, and checks whether others are -- if others are ahead of it,
     * status-indicates as "losing" and deletes its temp file; others behind it will so-suicide; if
     * it's the non-losing, then this thread "continues" the upload from 1k, or deletes/restarts if
     * continuation is impossible.  In that way, the fastest connection continues, the others give
     * up, timeouts are handled implicitly.
     *
     * Where possible, checksum post-upload is verified
     *
     * Where possible, a checksum file {filename}.sum is uploaded
     * Where possible, a manifest XML file is sent (my hostname, my user ID, any tasks or objectives, etc)
     *
     * @param file filename to upload
     * @return "OK, ##", "FAIL, ##", "UNKNOWN" based on results (where "##" is a line number of variable length)
     */
    public boolean upload(File file) throws FileTransferWinchException
    {
        String checksum = null;

        try
        {
            checksum = checksum(file);
        }
        catch (java.security.NoSuchAlgorithmException nsae)
        {
            throw new FileTransferChecksumMismatchException ("Checksum algorithm \"MD5\" is not available on this platform");
        }
        catch (java.io.FileNotFoundException fnfe)
        {
            throw new FileTransferWinchException ("Local file not readable", fnfe);
        }
        catch (java.io.IOException ioe)
        {
            throw new FileTransferWinchException ("I/O Error reading local file", ioe);
        }

        it.sauronsoftware.ftp4j.FTPClient client = new it.sauronsoftware.ftp4j.FTPClient();
        //File f = new java.io.File(file);
        String checksumnote = "# MD5 checksum (see RFC-1321)\n"
                              + "# Verify using: \n"
                              + "#   Windows:\n"
                              + "#     see http://support.microsoft.com/kb/841290\n"
                              + "#     VICT.BAT -c "+file.getName()+"\n"
                              + "#   MacOSX, BSD-based systems:\n"
                              + "#     md5 "+file.getName()+"\n"
                              + "#   Linux (ie Redhat, CentOS, Debian, Ubuntu):\n"
                              + "#     md5sum "+file.getName()+"\n"
                              + "#     md5sum -c "+file.getName()+".sum\n"
                              + "#   Practically every platform:\n"
                              + "#     java -jar vict.jar -c "+file.getName()+"\n";
        if (null != uploadNotify)
            for (String s: uploadNotify)
                checksumnote += "# NOTIFY: "+s+"\n";

        String checksumline = checksum + "  " + file.getName();

        System.out.println ("connecting to upload " + file.getName() + " (" + checksumline + ")");
        checksumline += "\n";
        try
        {
            client.setPassive(true);
            client.connect(getHost());
            client.login(getUser(),getPass());
            if (getPath().length() > 0)
            {
                System.out.println ("changing to " + getPath());

                client.changeDirectory(getPath());
            }
            System.out.println ("uploading " + file.getName());
            client.upload(file.getName() + ".sum", new java.io.StringBufferInputStream(checksumnote+checksumline), 0L, 0L, null);
            client.upload(file);
            it.sauronsoftware.ftp4j.FTPReply reply = client.sendCustomCommand("XMD5 \""+file.getName()+"\"");
            if (!reply.isSuccessCode())
                //System.out.println ("MD5 (XMD5): server offers no checksum, so you're on your own...");
                return false;
            else if (reply.getMessages()[0].equalsIgnoreCase(checksum)) return true;
            else
                throw new FileTransferChecksumMismatchException ("checksum \""+reply.getMessages()[0].toLowerCase()+"\" does not match expected \""+checksum.toLowerCase()+"\"");
        }
        catch (java.io.IOException ioe)
        {
            throw new FileTransferIllegalFSMReplyException("Uploading " + file.getName() + ": IO Error: " + ioe.getMessage(), ioe);
        }
        catch (it.sauronsoftware.ftp4j.FTPIllegalReplyException fire)
        {
            throw new FileTransferIllegalFSMReplyException("Uploading " + file.getName() + ": (FSM) Illegal FTP response: " + fire.getMessage(), fire);
        }
        catch (it.sauronsoftware.ftp4j.FTPException fe)
        {
            throw new FileTransferDataTransferException("Uploading " + file.getName() + ": (FTE) Illegal FTP response: " + fe.getMessage(), fe);
        }
        catch (it.sauronsoftware.ftp4j.FTPDataTransferException fdte)
        {
            throw new FileTransferDataTransferException("Uploading " + file.getName() + ": (DTE) Illegal FTP response: " + fdte.getMessage(), fdte);
        }
        catch (it.sauronsoftware.ftp4j.FTPAbortedException fae)
        {
            throw new FileTransferDataTransferException("Uploading " + file.getName() + ": FTP aborted: " + fae.getMessage(), fae);
        }
    }
}



