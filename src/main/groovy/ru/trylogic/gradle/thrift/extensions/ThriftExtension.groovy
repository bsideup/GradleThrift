package ru.trylogic.gradle.thrift.extensions

import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.internal.project.ProjectInternal
import ru.trylogic.gradle.thrift.plugins.ThriftPlugin

class ThriftExtension {
    static final String NAME = "thrift";

    private ProjectInternal project;
    
    ThriftExtension(ProjectInternal project) {
        this.project = project;
    }

    ResolvedArtifact getThriftArtifact() {
        return getResolvedArtifactFromConfiguration(ThriftPlugin.THRIFT_CONFIGURATION_NAME);
    }

    protected ResolvedArtifact getResolvedArtifactFromConfiguration(String configurationName) {
        def configuration = project.configurations.findByName(configurationName)

        assert configuration != null
        assert configuration.dependencies.size() == 1, "You should specify only 1 ${configuration.name} dependency"

        def dependency = configuration.dependencies.iterator().next()

        assert dependency instanceof ModuleDependency
        assert dependency.artifacts.size() == 1

        return configuration.resolvedConfiguration.resolvedArtifacts.iterator().next()
    }
}
