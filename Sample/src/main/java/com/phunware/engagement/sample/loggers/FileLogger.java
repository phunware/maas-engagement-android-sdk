package com.phunware.engagement.sample.loggers;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.phunware.engagement.internal.utils.Preconditions;
import com.phunware.engagement.log.Logger;
import com.phunware.engagement.log.Logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Writes log messages as text to a file.  This can support log rotation based on size or time,
 * and writes to the file on a background thread so as not to hinder performance.
 * This will be on by default in dev.
 */
public class FileLogger implements Logger {
    private static final String TAG = "FileLogger";
    /**
     * Priority constant for the println method; use Log.v.
     */
    private static final String VERBOSE = "VERBOSE";

    /**
     * Priority constant for the println method; use Log.d.
     */
    private static final String DEBUG = "DEBUG";

    /**
     * Priority constant for the println method; use Log.i.
     */
    private static final String INFO = "INFO";

    /**
     * Priority constant for the println method; use Log.w.
     */
    private static final String WARN = "WARN";

    /**
     * Priority constant for the println method; use Log.e.
     */
    private static final String ERROR = "ERROR";

    /**
     * Priority constant for the println method; use Log.wtf.
     */
    private static final String TERRIBLE_FAILURE = "WTF";

    /**
     * The log file name
     */
    private static final long LOG_FILE_MAXIMUM_SIZE = 2048000;
    private static final long LOG_FILE_MAXIMUM_COUNT = 2;
    private static final String LOG_FILE_NAME_PREFIX = "engagement_";
    private static final String LOG_FILE_NAME_SUFFIX = ".log";
    private static final String LOG_FILE_NAME = LOG_FILE_NAME_PREFIX
            + "%s_%d" + LOG_FILE_NAME_SUFFIX;

    boolean isLogWritable = true;
    Handler mHandler;
    File mLogFile;

    Long mAppId;

    public FileLogger(@NonNull Context context, @NonNull Long appId) {
        this(context, appId, null);
    }

    FileLogger(@NonNull Context context, @NonNull Long appId, @Nullable Handler handler) {
        Preconditions.checkNotNull(context, "context == null");
        Preconditions.checkNotNull(appId, "appId == null");

        if (!isLogWritable) {
            return;
        }

        mHandler = handler;
        mAppId = appId;

        if (mHandler == null) {
            HandlerThread thread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();
            mHandler = new Handler(thread.getLooper());
        }

        checkLogFile(context.getExternalFilesDir(null));
    }

    public File getLogFile() {
        return mLogFile;
    }

    private void writeLogMessage(final LogMessage msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handleWriteLogMessage(msg);
            }
        });
    }

    private void handleWriteLogMessage(LogMessage msg) {
        String formattedMessage = format(msg);
        if (formattedMessage == null || formattedMessage.isEmpty())
            return;

        if (mLogFile == null) {
            Logging.e(TAG, "Attempting to write log message with null file.", new Exception());
            return;
        }

        // Check if it's necessary to create a new file
        checkLogFile(mLogFile.getParentFile());

        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(mLogFile, true), "utf-8");
            writer.append(formattedMessage);
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {
            Log.e(TAG, "Failed to write log", ex);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to close BufferWriter", e);
            }
        }
    }

    void checkLogFile(File logFileDir) {
        if (mLogFile != null && mLogFile.length() < LOG_FILE_MAXIMUM_SIZE) {
            // Use the current log file because is not exceeded
            return;
        }

        mLogFile = null;
        if (logFileDir != null) {
            try {
                String[] logFileArray = logFileDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return (filename.startsWith(LOG_FILE_NAME_PREFIX + mAppId)
                                && filename.endsWith(LOG_FILE_NAME_SUFFIX));
                    }
                });

                // Try to find the log file which the size is not exceeded
                if (logFileArray != null) {
                    File tmpFile;
                    for (String filePath : logFileArray) {
                        tmpFile = new File(logFileDir, filePath);
                        if (tmpFile.length() < LOG_FILE_MAXIMUM_SIZE) {
                            mLogFile = tmpFile;
                            break;
                        }
                    }
                }

                // To create a new one
                if (mLogFile == null) {
                    if (logFileArray.length >= LOG_FILE_MAXIMUM_COUNT) {
                        // delete the oldest one
                        Arrays.sort(logFileArray);
                        File fileToDelete = new File(logFileArray[0]);
                        boolean deleteStatus = fileToDelete.delete();
                        if (!deleteStatus) {
                            Log.e(TAG, "Failed to delete the oldest log file.");
                        }
                    }
                    mLogFile = new File(logFileDir, String.format(Locale.US, LOG_FILE_NAME, mAppId,
                            Calendar.getInstance().getTimeInMillis()));
                    boolean createStatus = mLogFile.createNewFile();
                    if (!createStatus) {
                        isLogWritable = false;
                        Log.e(TAG, "Failed to delete the oldest log file.");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to process log file", e);
            }
        }
    }

    /**
     * Format the log message
     *
     * @param msg The message to format
     * @return The formatted log message
     */
    String format(LogMessage msg) {
        if (msg == null)
            return null;

        String dateStr = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss S", Locale.US)
                .format(new Date());

        if (msg.exception == null) {
            return String.format("%s :: [%s] :: %s :: %s", dateStr, msg.level, msg.tag,
                    msg.message);
        } else {
            StringWriter sw = null;
            PrintWriter pw = null;
            try {
                sw = new StringWriter();
                pw = new PrintWriter(sw);
                msg.exception.printStackTrace(pw);
                String tMessage = msg.message + '\n' + sw.toString();
                return String.format("%s :: [%s] :: %s :: %s", dateStr, msg.level, msg.tag,
                        tMessage);
            } catch (Exception e) {
                return msg.exception.toString();
            } finally {
                try {
                    if (sw != null) sw.close();
                    if (pw != null) pw.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close the writers", e);
                }
            }
        }
    }

    @Override
    public void v(String tag, String message, Throwable t) {
        if (isLogWritable) {
            LogMessage msg = new LogMessage(VERBOSE, tag, message, t);
            writeLogMessage(msg);
        }
    }

    @Override
    public void i(String tag, String message, Throwable t) {
        if (isLogWritable) {
            LogMessage msg = new LogMessage(INFO, tag, message, t);
            writeLogMessage(msg);
        }
    }

    @Override
    public void d(String tag, String message, Throwable t) {
        if (isLogWritable) {
            LogMessage msg = new LogMessage(DEBUG, tag, message, t);
            writeLogMessage(msg);
        }
    }

    @Override
    public void w(String tag, String message, Throwable t) {
        if (isLogWritable) {
            LogMessage msg = new LogMessage(WARN, tag, message, t);
            writeLogMessage(msg);
        }
    }

    @Override
    public void e(String tag, String message, Throwable t) {
        if (isLogWritable) {
            LogMessage msg = new LogMessage(ERROR, tag, message, t);
            writeLogMessage(msg);
        }
    }

    @Override
    public void wtf(String tag, String message, Throwable t) {
        if (isLogWritable) {
            LogMessage msg = new LogMessage(TERRIBLE_FAILURE, tag, message, t);
            writeLogMessage(msg);
        }
    }

    /**
     * Internal class
     */
    static final class LogMessage {
        String level;
        String tag;
        String message;
        Throwable exception;

        public LogMessage(String l, String t, String m, Throwable e) {
            this.level = l;
            this.tag = t;
            this.message = m;
            this.exception = e;
        }
    }
}
