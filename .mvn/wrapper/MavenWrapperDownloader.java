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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

public class MavenWrapperDownloader {

    /**
     * Default URL to download the maven-wrapper.jar from, if not specified in the properties file.
     */
    private static final String WRAPPER_URL = "https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar";

    public static void main(String args[]) {
        System.out.println("- Downloader started");
        File baseDir = new File(args[0]);
        System.out.println("- Using base directory: " + baseDir.getAbsolutePath());

        // If the maven-wrapper.properties exists, read the wrapperUrl from it.
        File wrapperPropertiesFile = new File(baseDir, ".mvn/wrapper/maven-wrapper.properties");
        String url = WRAPPER_URL;
        if (wrapperPropertiesFile.exists()) {
            System.out.println("- Reading wrapper properties from " + wrapperPropertiesFile.getAbsolutePath());
            try (FileInputStream wrapperPropertiesStream = new FileInputStream(wrapperPropertiesFile)) {
                Properties wrapperProperties = new Properties();
                wrapperProperties.load(wrapperPropertiesStream);
                url = wrapperProperties.getProperty("wrapperUrl", url);
            } catch (IOException e) {
                System.out.println("- ERROR reading " + wrapperPropertiesFile.getAbsolutePath());
            }
        }
        System.out.println("- Downloading from: " + url);

        File outputFile = new File(baseDir.getAbsolutePath(), ".mvn/wrapper/maven-wrapper.jar");
        if (!outputFile.getParentFile().exists()) {
            if (!outputFile.getParentFile().mkdirs()) {
                System.out.println(
                        "- ERROR creating output directory: " + outputFile.getParentFile().getAbsolutePath());
            }
        }
        System.out.println("- Downloading to: " + outputFile.getAbsolutePath());
        try {
            downloadFileFromURL(url, outputFile);
            System.out.println("Done");
            System.exit(0);
        } catch (Throwable e) {
            System.out.println("- Error downloading");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void downloadFileFromURL(String urlString, File destination) throws Exception {
        URL website = new URL(urlString);
        ReadableByteChannel rbc;
        rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(destination);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

}

