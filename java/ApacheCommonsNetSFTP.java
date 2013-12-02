/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.smallfoot.filexfer;

import java.io.File;
import org.apache.commons.net.ssh.SSHClient;

/**
 * Using Apache Commons-net-ssh, based on the SFTPUpload example at https://commons-net-ssh.googlecode.com/svn-history/r204/src/main/java/examples/ssh/SFTPUpload.java , upload a file
 */
public class ApacheCommonsNetSFTP extends FileTransferWinch
{

    java.util.Vector<String> uploadNotify = null;

    public static boolean handles (java.net.URL u)
    {
        return (u.getProtocol().equalsIgnoreCase("sftp"));
    };

    public ApacheCommonsNetSFTP(java.net.URL u)
    {
        url = u;
    }

    public String name()
    {
        return super.name() + " ("+getClass().getName().replaceAll("^.*\\.","") + ")";
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
                   url.toString().replaceFirst("^[a-zA-Z0-9]+:","sftp:")
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
        SSHClient ssh = new SSHClient();

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

        String targetPath = getPath();
        if ( (null == targetPath) || (0 == targetPath.length()) )
            targetPath = ".";

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
            ssh.loadKnownHosts();

//System.out.println ("connecting to "+getHost() +" port "+getPort()+ " to transfer "+ file.getName());
            ssh.connect(getHost());
//System.out.println ("authorizing to "+getHost()+" using \""+getUser()+"\", \""+getPass()+"\"");
            if ( (null != getPass()) && (getPass().length() > 0) )
            {
//System.out.println ("authorizing u p");
                /* lacking a java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); the following line simply generates a org.apache.commons.net.ssh.userauth.UserAuthException */
                ssh.authPassword(getUser(),getPass());
//System.out.println ("authorized u p");
            }
            else
            {
//System.out.println ("authorizing u k");
                ssh.authPublickey(getUser());
//System.out.println ("authorized u k");
            }

//System.out.println ("uploading " + file.getName());


            File temp = File.createTempFile(file.getName(), ".tmp");
//System.out.println ("created " + temp.getAbsolutePath());
            java.io.FileOutputStream tempo = new java.io.FileOutputStream(temp);
            tempo.write(checksumnote.getBytes(), 0, checksumnote.length());
            tempo.write(checksumline.getBytes(), 0, checksumline.length());
            tempo.flush();
            ssh.newSFTPClient().put(temp.getAbsolutePath(), targetPath/* getPath()+file.getName()+".sum" */);
            tempo.close();
            if (!temp.delete()) temp.deleteOnExit();
            ssh.newSFTPClient().put(file.getName(), targetPath /* getPath()+file.getName() */ );
            //it.sauronsoftware.ftp4j.FTPReply reply = client.sendCustomCommand("XMD5 \""+file.getName()+"\"");
            ssh.disconnect();

            //System.out.println ("MD5 (XMD5): server offers no checksum, so you're on your own...");
            return false;

        }
        catch (org.apache.commons.net.ssh.userauth.UserAuthException uae)
        {
            throw new FileTransferWinchException("Authentication failure: "+uae);
        }
        catch (java.io.IOException ioe)
        {
            ioe.printStackTrace();
            throw new FileTransferIllegalFSMReplyException("Uploading " + file.getName() + ": IO Error: " + ioe.getMessage(), ioe);
        }
        /*
                catch (it.sauronsoftware.ftp4j.FTPIllegalReplyException fire)
                {
                    throw new FileTransferIllegalFSMReplyException("Uploading " + file.getName() + ": (FSM) Illegal FTP response: " + fire.getMessage(), fire);
                }
                catch (it.sauronsoftware.ftp4j.FTPException fe)
                {
                    throw new FileTransferDataTransferException("Uploading " + file.getName() + ": (xFTE) Illegal FTP response: " + fe.getMessage(), fe);
                }
                catch (it.sauronsoftware.ftp4j.FTPDataTransferException fdte)
                {
                    throw new FileTransferDataTransferException("Uploading " + file.getName() + ": (DTE) Illegal FTP response: " + fdte.getMessage(), fdte);
                }
                catch (it.sauronsoftware.ftp4j.FTPAbortedException fae)
                {
                    throw new FileTransferDataTransferException("Uploading " + file.getName() + ": FTP aborted: " + fae.getMessage(), fae);
                }
        */
        finally
        {
            if (ssh.isConnected())
                try
                {
                    ssh.disconnect();
                }
                catch (java.io.IOException ioe)
                {
                    throw new FileTransferWinchException("tearing down ssh connection under exception", ioe);
                }
        }
    }

    // static
    // {
    // BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%d [%-15.15t] %-5p %-30.30c{1} - %m%n")));
    // }
}



