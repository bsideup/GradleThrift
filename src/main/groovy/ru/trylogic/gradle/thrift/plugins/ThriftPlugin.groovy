package ru.trylogic.gradle.thrift.plugins

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import ru.trylogic.gradle.thrift.extensions.ThriftExtension

class ThriftPlugin implements Plugin<ProjectInternal> {
    
    static final String THRIFT_CONFIGURATION_NAME = "thrift";
    
    @Override
    void apply(ProjectInternal project) {
        project.configurations.create(THRIFT_CONFIGURATION_NAME);

        project.extensions.create(ThriftExtension.NAME, ThriftExtension, project);
    }
}
