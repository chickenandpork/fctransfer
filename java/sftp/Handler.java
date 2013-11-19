/* smallfoot.org is owned by allan.clark */
package org.smallfoot.filexfer.sftp;

/**
 * @file
 */

public class Handler extends java.net.URLStreamHandler
{
    /**
     * openConnection(java.net.URL) overrides java.net.URLStreamHandler.openConnection(URL) by wrapping a apache-commons-net-sftp client
     *
     * @return populated connection as org.smallfoot.filexfer.sftp.SFTPURLConnection(Connection)
     * @param url URL to connect to
     */
    protected java.net.URLConnection openConnection(java.net.URL url)
    {
        return new org.smallfoot.filexfer.sftp.SFTPURLConnection(url);
    }
}
