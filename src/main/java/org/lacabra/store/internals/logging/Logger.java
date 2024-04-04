package org.lacabra.store.internals.logging;

import java.io.File;
import java.nio.file.FileSystemException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class Logger {
    private static final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy H:m:s");

    private static Logger instance;
    private java.util.logging.Logger logger;

    private Logger(String name) {
        super();

        Objects.requireNonNull(name);

        java.util.logging.SimpleFormatter fm = new java.util.logging.SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord lr) {
                return String.format("[%s] %s: %s\n", df.format(new Date(lr.getMillis())),
                        lr.getLevel().getName(), lr.getMessage());
            }
        };

        this.logger = java.util.logging.Logger.getLogger(name);

        java.util.logging.ConsoleHandler ch = new java.util.logging.ConsoleHandler();
        ch.setFormatter(fm);

        try {
            final File fd = new File("log");

            if (!(fd.exists() || fd.mkdirs()))
                throw new FileSystemException("Could not create the logfile directory.");

            if (!fd.isDirectory())
                throw new FileSystemException(fd.getPath() + " is not a directory.");

            final File[] ff = fd.listFiles();
            for (int[] i = new int[]{0, (ff == null ? new File[0] : ff).length - 10};
                 i[0] < i[1]; Objects.requireNonNull(ff)[i[0]++].delete())
                ;

            String jar = "";

            try {
                jar = "_" + Paths.get(
                                Logger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                        .getFileName().toString().replace(".jar", "");

                jar = jar.substring(0, 1).toUpperCase() + jar.substring(1);
            } catch (Exception e) {
                jar = "";
            }

            java.util.logging.FileHandler fh = new java.util.logging.FileHandler(
                    "log/" + name + jar + new SimpleDateFormat("_dd-MM-yyyy_H-m-s").format(
                            new Date(System.currentTimeMillis())) + ".log");
            fh.setFormatter(fm);

            this.logger.addHandler(fh);
        } catch (Exception e) {
            this.severe(e);
        }
    }

    public static Logger getLogger() {
        if (Logger.instance == null)
            Logger.instance = new Logger("Strava");

        return Logger.instance;
    }

    public void info(String msg) {
        this.info(msg, null);
    }

    public void info(Throwable thrown) {
        this.info(thrown.getMessage(), thrown);
    }

    public void info(String msg, Throwable thrown) {
        this.log(Level.INFO, msg, thrown);
    }

    public void warning(String msg) {
        this.warning(msg, null);
    }

    public void warning(Throwable thrown) {
        this.warning(thrown.getMessage(), thrown);
    }

    public void warning(String msg, Throwable thrown) {
        this.log(Level.WARNING, msg, thrown);
    }

    public void severe(String msg) {
        this.severe(msg, null);
    }

    public void severe(Throwable thrown) {
        this.severe(thrown.getMessage(), thrown);
    }

    public void severe(String msg, Throwable thrown) {
        this.log(Level.SEVERE, msg, thrown);
    }

    public void log(Level level, String msg) {
        this.log(level, msg, null);
    }

    public void log(Level level, String msg, Throwable thrown) {
        this.logger.log(level, msg, thrown);
    }
}
