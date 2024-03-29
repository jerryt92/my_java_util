/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.output;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * FileWriter that will create and honor lock files to allow simple
 * cross thread file lock handling.
 * <p>
 * This class provides a simple alternative to {@code FileWriter}
 * that will use a lock file to prevent duplicate writes.
 * </p>
 * <p>
 * <b>Note:</b> The lock file is deleted when {@link #close()} is called
 * - or if the main file cannot be opened initially.
 * In the (unlikely) event that the lock file cannot be deleted,
 * an exception is thrown.
 * </p>
 * <p>
 * By default, the file will be overwritten, but this may be changed to append.
 * The lock directory may be specified, but defaults to the system property
 * {@code java.io.tmpdir}.
 * The encoding may also be specified, and defaults to the platform default.
 * </p>
 */
public class LockableFileWriter extends Writer {
    // Cannot extend ProxyWriter, as requires writer to be
    // known when super() is called

    /** The extension for the lock file. */
    private static final String LCK = ".lck";

    /** The writer to decorate. */
    private final Writer out;

    /** The lock file. */
    private final File lockFile;

    /**
     * Constructs a LockableFileWriter.
     * If the file exists, it is overwritten.
     *
     * @param fileName  the file to write to, not null
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     */
    public LockableFileWriter(final String fileName) throws IOException {
        this(fileName, false, null);
    }

    /**
     * Constructs a LockableFileWriter.
     *
     * @param fileName  file to write to, not null
     * @param append  true if content should be appended, false to overwrite
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     */
    public LockableFileWriter(final String fileName, final boolean append) throws IOException {
        this(fileName, append, null);
    }

    /**
     * Constructs a LockableFileWriter.
     *
     * @param fileName  the file to write to, not null
     * @param append  true if content should be appended, false to overwrite
     * @param lockDir  the directory in which the lock file should be held
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     */
    public LockableFileWriter(final String fileName, final boolean append, final String lockDir) throws IOException {
        this(new File(fileName), append, lockDir);
    }

    /**
     * Constructs a LockableFileWriter.
     * If the file exists, it is overwritten.
     *
     * @param file  the file to write to, not null
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     */
    public LockableFileWriter(final File file) throws IOException {
        this(file, false, null);
    }

    /**
     * Constructs a LockableFileWriter.
     *
     * @param file  the file to write to, not null
     * @param append  true if content should be appended, false to overwrite
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     */
    public LockableFileWriter(final File file, final boolean append) throws IOException {
        this(file, append, null);
    }

    /**
     * Constructs a LockableFileWriter.
     *
     * @param file  the file to write to, not null
     * @param append  true if content should be appended, false to overwrite
     * @param lockDir  the directory in which the lock file should be held
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     * @deprecated 2.5 use {@link #LockableFileWriter(File, Charset, boolean, String)} instead
     */
    @Deprecated
    public LockableFileWriter(final File file, final boolean append, final String lockDir) throws IOException {
        this(file, Charset.defaultCharset(), append, lockDir);
    }

    /**
     * Constructs a LockableFileWriter with a file encoding.
     *
     * @param file  the file to write to, not null
     * @param charset  the charset to use, null means platform default
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     * @since 2.3
     */
    public LockableFileWriter(final File file, final Charset charset) throws IOException {
        this(file, charset, false, null);
    }

    /**
     * Constructs a LockableFileWriter with a file encoding.
     *
     * @param file  the file to write to, not null
     * @param charsetName  the name of the requested charset, null means platform default
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     * @throws java.nio.charset.UnsupportedCharsetException
     *             thrown instead of {@link java.io.UnsupportedEncodingException} in version 2.2 if the encoding is not
     *             supported.
     */
    public LockableFileWriter(final File file, final String charsetName) throws IOException {
        this(file, charsetName, false, null);
    }

    /**
     * Constructs a LockableFileWriter with a file encoding.
     *
     * @param file  the file to write to, not null
     * @param charset  the name of the requested charset, null means platform default
     * @param append  true if content should be appended, false to overwrite
     * @param lockDir  the directory in which the lock file should be held
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     * @since 2.3
     */
    public LockableFileWriter(File file, final Charset charset, final boolean append,
            String lockDir) throws IOException {
        // init file to create/append
        file = file.getAbsoluteFile();
        if (file.getParentFile() != null) {
            FileUtils.forceMkdir(file.getParentFile());
        }
        if (file.isDirectory()) {
            throw new IOException("File specified is a directory");
        }

        // init lock file
        if (lockDir == null) {
            lockDir = System.getProperty("java.io.tmpdir");
        }
        final File lockDirFile = new File(lockDir);
        FileUtils.forceMkdir(lockDirFile);
        testLockDir(lockDirFile);
        lockFile = new File(lockDirFile, file.getName() + LCK);

        // check if locked
        createLock();

        // init wrapped writer
        out = initWriter(file, charset, append);
    }

    /**
     * Constructs a LockableFileWriter with a file encoding.
     *
     * @param file  the file to write to, not null
     * @param charsetName  the encoding to use, null means platform default
     * @param append  true if content should be appended, false to overwrite
     * @param lockDir  the directory in which the lock file should be held
     * @throws NullPointerException if the file is null
     * @throws IOException in case of an I/O error
     * @throws java.nio.charset.UnsupportedCharsetException
     *             thrown instead of {@link java.io.UnsupportedEncodingException} in version 2.2 if the encoding is not
     *             supported.
     */
    public LockableFileWriter(final File file, final String charsetName, final boolean append,
            final String lockDir) throws IOException {
        this(file, Charsets.toCharset(charsetName), append, lockDir);
    }

    /**
     * Tests that we can write to the lock directory.
     *
     * @param lockDir  the File representing the lock directory
     * @throws IOException if we cannot write to the lock directory
     * @throws IOException if we cannot find the lock file
     */
    private void testLockDir(final File lockDir) throws IOException {
        if (!lockDir.exists()) {
            throw new IOException(
                    "Could not find lockDir: " + lockDir.getAbsolutePath());
        }
        if (!lockDir.canWrite()) {
            throw new IOException(
                    "Could not write to lockDir: " + lockDir.getAbsolutePath());
        }
    }

    /**
     * Creates the lock file.
     *
     * @throws IOException if we cannot create the file
     */
    private void createLock() throws IOException {
        synchronized (LockableFileWriter.class) {
            if (!lockFile.createNewFile()) {
                throw new IOException("Can't write file, lock " +
                        lockFile.getAbsolutePath() + " exists");
            }
            lockFile.deleteOnExit();
        }
    }

    /**
     * Initializes the wrapped file writer.
     * Ensure that a cleanup occurs if the writer creation fails.
     *
     * @param file  the file to be accessed
     * @param charset  the charset to use
     * @param append  true to append
     * @return The initialized writer
     * @throws IOException if an error occurs
     */
    private Writer initWriter(final File file, final Charset charset, final boolean append) throws IOException {
        final boolean fileExistedAlready = file.exists();
        try {
            return new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(), append),
                                          Charsets.toCharset(charset));

        } catch (final IOException | RuntimeException ex) {
            FileUtils.deleteQuietly(lockFile);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
    }

    /**
     * Closes the file writer and deletes the lock file.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        try {
            out.close();
        } finally {
            FileUtils.delete(lockFile);
        }
    }

    /**
     * Writes a character.
     * @param c the character to write
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final int c) throws IOException {
        out.write(c);
    }

    /**
     * Writes the characters from an array.
     * @param cbuf the characters to write
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final char[] cbuf) throws IOException {
        out.write(cbuf);
    }

    /**
     * Writes the specified characters from an array.
     * @param cbuf the characters to write
     * @param off The start offset
     * @param len The number of characters to write
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        out.write(cbuf, off, len);
    }

    /**
     * Writes the characters from a string.
     * @param str the string to write
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final String str) throws IOException {
        out.write(str);
    }

    /**
     * Writes the specified characters from a string.
     * @param str the string to write
     * @param off The start offset
     * @param len The number of characters to write
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(final String str, final int off, final int len) throws IOException {
        out.write(str, off, len);
    }

    /**
     * Flushes the stream.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        out.flush();
    }

}
