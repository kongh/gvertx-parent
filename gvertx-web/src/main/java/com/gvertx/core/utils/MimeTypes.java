/**
 * Copyright (C) 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gvertx.core.utils;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MimeTypes utils Adapted from play 1.2.4
 */

public class MimeTypes {
    static private final Logger logger = LoggerFactory.getLogger(MimeTypes.class);

    static private final String PROPERTY_MIMETYPE_PREFIX = "mimetype.";
    static private final String DEFAULT_MIMET_TYPE_LOCATIONS = "com/gvertx/core/utils/mime-types.properties";

    static private Properties mimetypes = new Properties();
    static private Pattern extPattern =  Pattern.compile("^.*\\.([^.]+)$");


    static {
        // Load default mimetypes from the framework
        try (InputStream is = MimeTypes.class.getClassLoader().getResourceAsStream(DEFAULT_MIMET_TYPE_LOCATIONS)) {
            mimetypes.load(is);
        } catch (Exception e) {
            logger.error("Failed to load mimetypes", e);
        }
    }


    public static void main(String[] args) {
//        URL url;
//        url.getFile();
//        System.out.println("text/html".toLowerCase());
        String contentType = MimeTypes.getMimeType("/pub/files/foobar.txt?id=123456");
        System.out.println(contentType);
    }
    /**
     * return the mimetype from a file name
     * 
     * @param filename
     *            the file name
     * @return the mimetype or the empty string if not found
     */
    static public String getMimeType(String filename) {
        return getMimeType(filename, "");
    }

    /**
     * return the mimetype from a file name.<br/>
     * 
     * @param filename
     *            the file name
     * @param defaultMimeType
     *            the default mime type to return when no matching mimetype is
     *            found
     * @return the mimetype
     */
    static public String getMimeType(String filename, String defaultMimeType) {
        Matcher matcher = extPattern.matcher(filename.toLowerCase());
        String ext = "";
        if (matcher.matches()) {
            ext = matcher.group(1);
        }
        if (ext.length() > 0) {
            String mimeType = mimetypes.getProperty(ext);
            if (mimeType == null) {
                return defaultMimeType;
            }
            return mimeType;
        }
        return defaultMimeType;
    }

    /**
     * return the content-type from a file name. If none is found returning
     * application/octet-stream<br/>
     * For a text-based content-type, also return the encoding suffix eg.
     * <em>"text/plain; charset=utf-8"</em>
     * 
     * @param filename
     *            the file name
     * @return the content-type deduced from the file extension.
     */
    static public String getContentType( String filename) {
        return getContentType(filename, "application/octet-stream");
    }

    /**
     * return the content-type from a file name.<br/>
     * For a text-based content-type, also return the encoding suffix eg.
     * <em>"text/plain; charset=utf-8"</em>
     * 
     * @param filename
     *            the file name
     * @param defaultContentType
     *            the default content-type to return when no matching
     *            content-type is found
     * @return the content-type deduced from the file extension.
     */
    static public String getContentType(
                                 String filename,
                                 String defaultContentType) {
        String contentType = getMimeType(filename, null);
        if (contentType == null) {
            contentType = defaultContentType;
        }
        if (contentType != null && contentType.startsWith("text/")) {
            // UTF-8 is fixed for now as ninja only supports utf-8 in files...
            return contentType + "; charset=utf-8";
        }
        return contentType;
    }

    /**
     * check the mimetype is referenced in the mimetypes database
     * 
     * @param mimeType
     *            the mimeType to verify
     */
    static public boolean isValidMimeType(String mimeType) {
        if (mimeType == null) {
            return false;
        } else if (mimeType.indexOf(";") != -1) {
            return mimetypes.contains(mimeType.split(";")[0]);
        } else {
            return mimetypes.contains(mimeType);
        }
    }



}
