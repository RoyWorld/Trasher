package com.trasher.util;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;


/**
 * limit log count in the file log
 * Created by RoyChan on 2018/2/26.
 */
public class CountingFileAppender<E> extends UnsynchronizedAppenderBase<E> {

    /**
     * All synchronization in this class is done via the lock object.
     */
    protected final ReentrantLock lock = new ReentrantLock(true);

    /**
     * limit log count
     */
    static int DEFAULT_LIMIT = 10;
    int counter = 0;
    int limit = DEFAULT_LIMIT;

    /**
     * The name of the active log file.
     */
    protected String fileName = null;

    /**
     * This is the {@link OutputStream outputStream} where output will be written.
     */
    private OutputStream outputStream;
    protected boolean append = false;

    //format output in a special pattern
    protected Encoder<E> encoder;

    /**
     * logger init
     * safetly check
     */
    public void start() {
        int errors = 0;

        if (getFile() != null) {
            addInfo("File property is set to [" + fileName + "]");

            try {
                openFile(getFile());
            } catch (java.io.IOException e) {
                errors++;
                addError("openFile(" + fileName + ") call failed.", e);
            }
        } else {
            errors++;
            addError("\"File\" property not set for appender named [" + name + "].");
        }

        // only error free appenders should be activated
        if (errors == 0) {
            super.start();
        }
    }

    /**
     * log output
     * core implement method
     * @param evt
     */
    @Override
    protected void append(E evt) {
        if (!isStarted()) {
            return;
        }
        try {
            // the synchronization prevents the OutputStream from being closed while we
            // are writing. It also prevents multiple threads from entering the same
            // converter. Converters assume that they are in a synchronized block.
            lock.lock();
            try {
                if (counter >= limit) {
                    return;
                }
                this.encoder.doEncode(evt);
                counter++;
            } finally {
                lock.unlock();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addStatus(new ErrorStatus("IO failure in appender", this, ex));
        }
    }

    public void openFile(String file_name) throws IOException {
        lock.lock();
        try {
            File file = new File(file_name);
            boolean result = FileUtil.createMissingParentDirectories(file);
            if (!result) {
                addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
            }

            ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(file, append);
            resilientFos.setContext(context);
            setOutputStream(resilientFos);
        } finally {
            lock.unlock();
        }
    }

    public void setOutputStream(OutputStream outputStream) {
        lock.lock();
        try {
            // close any previously opened output stream
            closeOutputStream();

            this.outputStream = outputStream;
            if (encoder == null) {
                addWarn("Encoder has not been set. Cannot invoke its init method.");
                return;
            }

            encoderInit();
        } finally {
            lock.unlock();
        }
    }

    protected void closeOutputStream() {
        if (this.outputStream != null) {
            try {
                // before closing we have to output out layout's footer
                encoderClose();
                this.outputStream.close();
                this.outputStream = null;
            } catch (IOException e) {
                addStatus(new ErrorStatus("Could not close output stream for OutputStreamAppender.", this, e));
            }
        }
    }

    void encoderInit() {
        if (encoder != null && this.outputStream != null) {
            try {
                encoder.init(outputStream);
            } catch (IOException ioe) {
                this.started = false;
                addStatus(new ErrorStatus("Failed to initialize encoder for appender named [" + name + "].", this, ioe));
            }
        }
    }

    void encoderClose() {
        if (encoder != null && this.outputStream != null) {
            try {
                encoder.close();
            } catch (IOException ioe) {
                this.started = false;
                addStatus(new ErrorStatus("Failed to write footer for appender named [" + name + "].", this, ioe));
            }
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getFile() {
        return fileName;
    }

    public void setFile(String file) {
        if (file == null) {
            fileName = file;
        } else {
            // Trim spaces from both ends. The users probably does not want
            // trailing spaces in file names.
            fileName = file.trim();
        }
    }

    public Encoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

}