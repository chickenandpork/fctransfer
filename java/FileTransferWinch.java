package org.smallfoot.filexfer;

import java.io.*;
import java.util.Vector;

public abstract class FileTransferWinch
{
    protected java.net.URL url = null;

    public static boolean handles(java.net.URL u)
    {
        return false;
    }

    public String name()
    {
        return getClass().getName().replaceAll("^.*\\.","");
    }

    /** list one or more example URLs showing the available upload/download protocols
     *
     * @param url a sample URL showing user/pass/pathname
     * @returns array of examples using that URL
     */
    public static String[] examples(java.net.URL url)
    {
        return "".split(";");
    }

    /** upload a file using this FileTransferWinch; errors in upload are handled via Exceptions
     *
     * @param file the content to upload
     * @param uploadNotify array of identifiers (email address or jabber contacts) to list as notify recipients in the upload checksum file
     * @returns true if upload was checksum-certified; false is such facility isn't available (error if checksum is available and mismatches)
     */
    public abstract boolean upload(File file, Vector<String> uploadNotify) throws FileTransferWinchException;

    /** upload a file using this FileTransferWinch; errors in upload are handled via Exceptions
     *
     * @param file the content to upload
     * @param uploadNotify array of identifiers (email address or jabber contacts) to list as notify recipients in the upload checksum file
     * @returns true if upload was checksum-certified; false is such facility isn't available (error if checksum is available and mismatches)
     */
    public boolean upload(String file, Vector<String> uploadNotify) throws FileTransferWinchException
    {
        return upload(new java.io.File(file), uploadNotify);
    }

    /** A base collector exception: "there was some exception in the FileTransferWinch class"; typically, more meaning and/or usefulness is reached by catching specific subclasses of this exception */
    public class FileTransferWinchException 		extends Exception
    {
        public FileTransferWinchException(String s, Throwable t)
        {
            super (s,t);
        } public FileTransferWinchException(Throwable t)
        {
            super (t);
        } public FileTransferWinchException(String s)
        {
            super(s);
        } public FileTransferWinchException()
        {
            super();
        }
    };

    /** This exception indicates that the underlying Finite State Machine received an unexpected reply, and has no next-state in the conversation.  Basically: "the other side said something and I didn't know what to do ". */
    public class FileTransferIllegalFSMReplyException 	extends FileTransferWinchException
    {
        public FileTransferIllegalFSMReplyException(String s, Throwable t)
        {
            super (s,t);
        } public FileTransferIllegalFSMReplyException(Throwable t)
        {
            super (t);
        } public FileTransferIllegalFSMReplyException(String s)
        {
            super(s);
        } public FileTransferIllegalFSMReplyException()
        {
            super();
        }
    };

    /** This exception alerts to issues during transfer of data: connection lost, etc ... */
    public class FileTransferDataTransferException 	extends FileTransferWinchException
    {
        public FileTransferDataTransferException(String s, Throwable t)
        {
            super (s,t);
        } public FileTransferDataTransferException(Throwable t)
        {
            super (t);
        } public FileTransferDataTransferException(String s)
        {
            super(s);
        } public FileTransferDataTransferException()
        {
            super();
        }
    };

    /** This exception alerts to the specific Data Transfer issuesuch that the receiver's checksum confirmation did not match the sender ... */
    public class FileTransferChecksumMismatchException 	extends FileTransferDataTransferException
    {
        public FileTransferChecksumMismatchException(String s, Throwable t)
        {
            super (s,t);
        } public FileTransferChecksumMismatchException(Throwable t)
        {
            super (t);
        } public FileTransferChecksumMismatchException(String s)
        {
            super(s);
        } public FileTransferChecksumMismatchException()
        {
            super();
        }
    };

    /** This exception indicates that transfer was aborted; I have not yet clarified in the API whether this exception would take precidence over FileTransferDataTransferException if both occur, or a FileTransferDataTransferException causes a FileTransferAbortedException */
    public class FileTransferAbortedException 		extends FileTransferWinchException
    {
        public FileTransferAbortedException(String s, Throwable t)
        {
            super (s,t);
        } public FileTransferAbortedException(Throwable t)
        {
            super (t);
        } public FileTransferAbortedException(String s)
        {
            super(s);
        } public FileTransferAbortedException()
        {
            super();
        }
    };


    String getHost()
    {
        return getHost(url);
    }
    static String getHost (java.net.URL u)
    {
        if (null == u) return null;
        else return u.getHost();
    }

    String getPass()
    {
        return getPass(url);
    }
    static String getPass(java.net.URL u)
    {
        if (null == u) return null;
        else if (null == u.getUserInfo()) return null;
        else
        {
            String[] x = u.getUserInfo().split(":");

            try
            {
                if (1 >= java.lang.reflect.Array.getLength(x))
                    return null;
                else
                    return java.net.URLDecoder.decode(x[1],"UTF-8");
            }
            catch (java.io.UnsupportedEncodingException uee)
            {
                System.out.println ("WARNING: URL UserInfo (user/pass) didn't decode; using non-url-decoded version");
                if (1 >= java.lang.reflect.Array.getLength(x)) return x[1];
                else return null;
            }
        }
    }

    String getUser()
    {
        return getUser(url);
    }
    static String getUser(java.net.URL u)
    {
        if (null == u) return null;
        else if (null == u.getUserInfo()) return null;
        else
        {
            String[] x = u.getUserInfo().split(":");

            try
            {
                return java.net.URLDecoder.decode(x[0],"UTF-8");
            }
            catch (java.io.UnsupportedEncodingException uee)
            {
                System.out.println ("WARNING: URL UserInfo (user/pass) didn't decode; using non-url-decoded version");
                return x[0];
            }
        }
    }

    String getPath()
    {
        return getPath(url);
    }
    static String getPath (java.net.URL u)
    {
        if (null == u) return null;
        String p =  u.getPath();
        if (p.startsWith("/")) p = p.substring(1);

        try
        {
            return java.net.URLDecoder.decode(p,"UTF-8");
        }
        catch (java.io.UnsupportedEncodingException uee)
        {
            System.out.println ("WARNING: URL Path (\""+p+"\") didn't decode; using non-url-decoded version");
            return p;
        }
    }

    int getPort()
    {
        return getPort(url);
    }
    static int getPort (java.net.URL u)
    {
        if (null == u) return -1;
        return u.getPort();
    }

    /**
     * convert a MD5 digest to a printable string.
     *
     * Part of "hey, why not include a checksum piece with everything?"
     *
     * Public/Static because, hey, it might be useful elsewhere, and needs no state
     *
     * @param digest the resulting MD5 digest to convert to a string
     * @return string representation of the digest
     */
    static public String MD5toString (byte[] digest)
    {
        String result = "";

        for (byte B:digest)
        {
            result += String.format("%02x",B);
        }

        return result;
    }



    /**
     * Calculate an MD5 checksum
     *
     * In converting this file from VIFieldTool code, the checksum stuff was already here, so my
     * logic was "why make a user search for checksumming tools?"  The resulting algorithm generates
     * a checksum compatible with the "md5" and "md5sum" tools available in everything non-Windows
     * everywhere.  It's like "hey, why not include a checksum piece with everything?"
     *
     * So in this function, given a filename, opens it up and reads in data blocks, incorporating
     * each against the MD5 digest: stirring the pot-of-MD5 with blocks of bytes to generate the
     * final checksum digest.
     *
     * This file itself may have no testcases, but this function is confirmed against the build
     * platform's md5 or md5sum program for accuracy.
     *
     * @param file filename of the file to calculate
     * @return checksum, as a string
     */
    public static String checksum(File file)
    throws java.security.NoSuchAlgorithmException, java.io.FileNotFoundException, java.io.IOException
    {
        byte[] buff = new byte[8192];
        int length;

        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        InputStream in = new FileInputStream (file);

        /* OK, obviously we have the file open and ready, let's stir that MD5 pot */
        while (0 < (length = in.read(buff)))
        {
            md.update(buff, 0, length);
        }
        in.close();

        /* job's done, grab it as a string and run for the hills! */
        return MD5toString(md.digest());
    }


    /**
     * Calculate an MD5 checksum
     *
     * So in this function, given a filename, it passes execution to the checksum(File) function do do the actual work.
     *
     * @param file filename of the file to calculate
     * @return checksum, as a string
     */
    public static String checksum(String file)
    throws java.security.NoSuchAlgorithmException, java.io.FileNotFoundException, java.io.IOException
    {
        return checksum(new File(file));
    }




}



