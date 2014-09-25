package ru.trylogic.gradle.thrift.tasks

import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.*
import org.gradle.process.internal.ExecActionFactory
import ru.trylogic.gradle.thrift.extensions.ThriftExtension

import javax.inject.Inject

class ThriftCompileTask extends SourceTask {

    @OutputDirectory
    File destinationDir;
    
    @Input
    String generator;
    
    protected ExecActionFactory execActionFactory;

    @Inject
    ThriftCompileTask(ExecActionFactory execActionFactory) {
        this.execActionFactory = execActionFactory;
        group = BasePlugin.BUILD_GROUP

        include "**/*.thrift"
        source "src/main/thrift"
    }
    
    def destinationDir(Object value) {
        destinationDir = project.file(value)
    }

    @TaskAction
    protected void compile() {
        def extension = project.getExtensions().findByType(ThriftExtension);

        def thriftCompilerFile = extension.thriftArtifact.file
        
        project.ant.chmod(file : thriftCompilerFile, perm: "+x")

        getSource().getFiles().each { File file ->
            
            def execAction = execActionFactory.newExecAction();
            execAction.standardOutput = new ByteArrayOutputStream();
            execAction.errorOutput = new ByteArrayOutputStream(); 
            
            execAction.executable = thriftCompilerFile.absolutePath;

            execAction.args = [
                    "--out",
                    "${getDestinationDir()}",
                    "--gen",
                    generator,
                    file.absolutePath
            ]
            
            try {
                execAction.execute()

                execAction.standardOutput.toString().eachLine {
                    if(!it.empty) {
                        logger.warn(it);
                    }
                }
            } catch(Exception e) {
                throw new TaskExecutionException(this, new Exception(execAction.errorOutput.toString(), e));
            }
        }
    }
}