package org.smallfoot.filexfer;

import java.io.*;

public abstract class FileTransferWinch
{
    public abstract void upload(File file) throws FileTransferWinchException;

    public void upload(String file) throws FileTransferWinchException { upload(new java.io.File(file)); }

    public class FileTransferWinchException 		extends Exception { public FileTransferWinchException(Throwable t) { super (t); } public FileTransferWinchException(String s) { super(s); } public FileTransferWinchException() { super(); } };

    public class FileTransferIllegalFSMReplyException 	extends FileTransferWinchException { public FileTransferIllegalFSMReplyException(Throwable t) { super (t); } public FileTransferIllegalFSMReplyException(String s) { super(s); } public FileTransferIllegalFSMReplyException() { super(); } };

    public class FileTransferDataTransferException 	extends FileTransferWinchException { public FileTransferDataTransferException(Throwable t) { super (t); } public FileTransferDataTransferException(String s) { super(s); } public FileTransferDataTransferException() { super(); } };

    public class FileTransferAbortedException 		extends FileTransferWinchException { public FileTransferAbortedException(Throwable t) { super (t); } public FileTransferAbortedException(String s) { super(s); } public FileTransferAbortedException() { super(); } };
}



