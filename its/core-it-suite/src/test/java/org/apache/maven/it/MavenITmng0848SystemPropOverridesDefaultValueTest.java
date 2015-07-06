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
import java.util.Properties;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-848">MNG-848</a>.
 * 
 * @author Benjamin Bentmann
 * @version $Id$
 */
public class MavenITmng0848SystemPropOverridesDefaultValueTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng0848SystemPropOverridesDefaultValueTest()
    {
        super( ALL_MAVEN_VERSIONS );
    }

    /**
     * Test that execution/system properties take precedence over default value of plugin parameters.
     */
    public void testitMNG848()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-0848" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.setSystemProperty( "config.aliasDefaultExpressionParam", "PASSED" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties configProps = verifier.loadProperties( "target/config.properties" );
        assertEquals( "maven-core-it", configProps.getProperty( "defaultParam" ) );
        assertEquals( "PASSED", configProps.getProperty( "aliasDefaultExpressionParam" ) );
    }

}
