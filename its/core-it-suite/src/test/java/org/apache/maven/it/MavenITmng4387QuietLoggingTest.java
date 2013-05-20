package org.apache.maven.it;

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

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4387">MNG-4387</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng4387QuietLoggingTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4387QuietLoggingTest()
    {
        super( "[2.0.5,)" );
    }

    /**
     * Test that the CLI flag -q enables quiet logging, i.e. suppresses log levels below ERROR.
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4387" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.getCliOptions().add( "-q" );
        verifier.setLogFileName( "log.txt" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        List<String> lines = verifier.loadLines( "log.txt", "UTF-8" );
        
        for ( Iterator<String> it = lines.iterator(); it.hasNext(); )
        {
            String line = it.next();
            if ( line.startsWith( "+" ) || line.startsWith( "EMMA" ) )
            {
                it.remove();
            }
        }

        assertEquals( Collections.EMPTY_LIST, lines );
    }

}
