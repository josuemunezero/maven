package org.apache.maven.integrationtests;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests that the PluginDescriptor.getArtifacts() call returns all of the dependencies of the plugin,
 * not just those that made it past the filter excluding Maven's core artifacts.
 */
public class MavenITmng3473PluginReportCrash
    extends AbstractMavenIntegrationTestCase
{
    public MavenITmng3473PluginReportCrash()
    {
        super( "(2.0.8,)" ); // >2.0.8
    }

    public void testitMNG3473 ()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3473-PluginReportCrash" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );


        File logFile = new File( testDir, "log.txt" );

        // force the use of the 2.4.1 plugin version via a profile here...
        List cliOptions = new ArrayList();
        cliOptions.add( "-Pplugin-2.4.1" );
        verifier.setCliOptions( cliOptions );

        verifier.executeGoal( "install" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        logFile.renameTo( new File( testDir, "log-2.4.1-preinstall.txt" ) );

        //should succeed with 2.4.1
        verifier.executeGoals( Arrays.asList( new String[]{ "org.apache.maven.plugins:maven-help-plugin:2.0.2:effective-pom", "site" } ) );

        // NOTE: Velocity prints an [ERROR] line pertaining to an incorrect macro usage when run in 2.1, so this doesn't work.
//        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        logFile.renameTo( new File( testDir, "log-2.4.1.txt" ) );

        //should fail with 2.4
        cliOptions.clear();
        cliOptions.add( "-Pplugin-2.4" );
        verifier.setCliOptions( cliOptions );

        try
        {
          verifier.executeGoal( "site" );
        }
        catch (VerificationException e)
        {
          //expected this but don't require it cause some os's don't return the correct error code
        }
        verifier.verifyTextInLog( "org/apache/maven/doxia/module/site/manager/SiteModuleNotFoundException" );
        verifier.resetStreams();

        logFile.renameTo( new File( testDir, "log-2.4.txt" ) );
    }
}
